package com.rainyalley.architecture.util.jedis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

/**
 * 延迟队列
 * @author bin.zhang
 */
public class RedisDelayQueue implements Queue<Job> {

    private Logger logger = LoggerFactory.getLogger(RedisDelayQueue.class);

    private RedisOperations<String,String> redisTemplate;

    /**
     * job池
     */
    private String jobPoolKey;

    /**
     * 延迟桶,  zset
     */
    private String delayBucketKey;

    /**
     * 预备执行队列, zset
     */
    private String readyQueueKey;

    /**
     * jobId生成器
     */
    private String jobIdGenKey;

    /**
     * 分布式锁
     */
    private Lock lock;

    /**
     * 异步任务
     */
    private ScheduleTask task;


    /**
     * @param redisTemplate redisTemplate
     * @param keyPrefix key前缀
     * @param initialDelay 初始化延迟
     * @param delay 执行间隔
     * @param unit 时间单位
     */
    public RedisDelayQueue(RedisOperations<String,String> redisTemplate,
                           String keyPrefix,
                           long initialDelay,
                           long delay,
                           TimeUnit unit) {
        this.jobPoolKey = keyPrefix + "pool";
        this.delayBucketKey = keyPrefix + "delay";
        this.readyQueueKey = keyPrefix + "ready";
        this.jobIdGenKey = keyPrefix + "id";
        String lockKey = keyPrefix + "lock";
        this.redisTemplate = redisTemplate;
        this.lock = new JedisReentrantLock(lockKey, redisTemplate);
        this.task = new ScheduleTask();
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(
                1,
                new CustomizableThreadFactory("redis-delay-"));
        executor.scheduleWithFixedDelay(this.task, initialDelay, delay, unit);
    }

    public void destroy(){
        redisTemplate.delete(Arrays.asList(jobPoolKey, delayBucketKey, readyQueueKey, jobIdGenKey));
    }

    public void runTask(){
        task.run();
    }


    @Override
    public int size() {
        return redisTemplate.opsForHash().size(jobPoolKey).intValue();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        if(Objects.isNull(o)){
            return false;
        }
        if(!(o instanceof Job)){
            return false;
        }
        Job job = (Job) o;
        return  redisTemplate.opsForHash().hasKey(jobPoolKey, job.getJobId());
    }

    @NotNull
    @Override
    public Iterator<Job> iterator() {
        return jobList().iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return jobList().toArray();
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        return jobList().toArray(a);
    }

    @Override
    public boolean add(Job job) {
        return offer(job);
    }

    @Override
    public boolean remove(Object o) {
        if (Objects.isNull(o)){
            return false;
        }
        if(!(o instanceof Job)){
            return false;
        }

        Job job = (Job)o;
        Long deleted = redisTemplate.opsForHash().delete(jobPoolKey, job.getJobId());
        return deleted > 0;
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        boolean containsAll = true;
        for (Object o : c) {
            containsAll &= contains(o);
        }
        return containsAll;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends Job> c) {
        boolean added = true;
        for (Job job : c) {
            added &= add(job);
        }
        return added;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        boolean removed = true;
        for (Object o : c) {
            removed &= remove(o);
        }
        return removed;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        Set<Object> keys = redisTemplate.opsForHash().keys(jobPoolKey);
        if(CollectionUtils.isEmpty(keys)){
            return;
        }
        redisTemplate.opsForHash().delete(jobPoolKey, keys.toArray());
    }

    @Override
    public boolean offer(Job job) {
        Long jobId = redisTemplate.opsForValue().increment(jobIdGenKey, 1);
        job.setJobId(String.valueOf(jobId));

        redisTemplate.opsForZSet().add(delayBucketKey, String.valueOf(jobId), job.getCreate() + job.getDelay());
        redisTemplate.opsForHash().put(jobPoolKey, String.valueOf(jobId), toString(job));
        return true;
    }


    /**
     * 弹出一个已到达延迟时间的元素
     * 没有则抛出异常
     * @throws NoSuchElementException
     */
    @Override
    public Job remove() throws NoSuchElementException{
        Job job = poll();
        if(job == null){
            throw new NoSuchElementException();
        } else {
            return job;
        }
    }

    /**
     * 弹出一个已到达延迟时间的元素
     */
    @Override
    public Job poll() {
        boolean loked =  lock.tryLock();
        if(!loked){
            return null;
        }
        try {
            Job job = peek();
            if(job == null){
                return null;
            } else {
                redisTemplate.opsForHash().delete(jobPoolKey, job.getJobId());
                redisTemplate.opsForZSet().remove(readyQueueKey, job.getJobId());
                return job;
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 查看一个已到达延迟时间的元素
     * @return 没有则抛出异常
     * @throws NoSuchElementException
     */
    @Override
    public Job element() throws NoSuchElementException{
        Job job = peek();
        if(job == null){
            throw new NoSuchElementException();
        } else {
            return job;
        }
    }

    /**
     * 查看一个已到达延迟时间的元素
     * @return 没有则返回null
     */
    @Override
    public Job peek(){
        Set<String> readySet = redisTemplate.opsForZSet().range(readyQueueKey, 0, 0);
        if(CollectionUtils.isEmpty(readySet)){
            return null;
        } else {
            String jobId = readySet.iterator().next();
            Object objVal = redisTemplate.opsForHash().get(jobPoolKey, jobId);
            if(Objects.isNull(objVal)){
                return null;
            } else {
                return toJob(String.valueOf(objVal));
            }
        }
    }


    private class ScheduleTask implements Runnable{

        @Override
        public void run()  {
            boolean locked = lock.tryLock();
            if(!locked){
                return;
            }
            try {
                double max = System.currentTimeMillis();
                Set<String> jobIds = redisTemplate.opsForZSet().rangeByScore(delayBucketKey,0, max,0, 100);
                if(CollectionUtils.isEmpty(jobIds)){
                    return;
                }
                Object[] idArr = jobIds.toArray(new Object[0]);
                Set<ZSetOperations.TypedTuple<String>> typedTuples = jobIds.stream().map(id -> new DefaultTypedTuple<String>(id, Double.valueOf("0"))).collect(Collectors.toSet());
                redisTemplate.opsForZSet().add(readyQueueKey, typedTuples);
                redisTemplate.opsForZSet().remove(delayBucketKey, (Object[])idArr);
                if(logger.isDebugEnabled()){
                    logger.debug("ScheduleTask jobId size: " + jobIds.size());
                }
            } finally {
                lock.unlock();
            }
        }
    }


    private Job toJob(String txt){
        try {
            return new ObjectMapper().readValue(txt, Job.class);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private String toString(Job job){
        try {
            return new ObjectMapper().writeValueAsString(job);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }


    private List<Job> jobList(){
        List<Object> values = redisTemplate.opsForHash().values(jobPoolKey);
        return values.stream().map(val -> toJob(String.valueOf(val))).collect(Collectors.toList());
    }

}
