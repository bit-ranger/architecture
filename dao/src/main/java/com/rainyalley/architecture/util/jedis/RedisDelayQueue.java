package com.rainyalley.architecture.util.jedis;

import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisOperations;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

/**
 * @author bin.zhang
 */
public class RedisDelayQueue implements Queue<Job> {

    private RedisOperations<String,String> redisTemplate;

    /**
     * job池
     */
    private String jobPoolKey = "";

    /**
     * 延迟桶,  zset
     */
    private String delayBucketKey = "";

    /**
     * 预备执行队列, zset
     */
    private String readyQueueKey = "";

    /**
     * jobId生成器
     */
    private String jobIdGenKey = "";

    /**
     * 锁
     */
    private String lockKey = "";

    private Lock lock;

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
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean offer(Job job) {
        Long jobId = redisTemplate.opsForValue().increment(jobIdGenKey, 1);
        job.setJobId(String.valueOf(jobId));

        redisTemplate.opsForZSet().add(delayBucketKey, String.valueOf(jobId), job.getCreate() + job.getDelay());
        redisTemplate.opsForHash().put(jobPoolKey, String.valueOf(jobId), job);
        return true;
    }

    @Override
    public Job remove() {
        Job job = poll();
        if(job == null){
            throw new NoSuchElementException();
        } else {
            return job;
        }
    }

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

    @Override
    public Job element() {
        Job job = peek();
        if(job == null){
            throw new NoSuchElementException();
        } else {
            return job;
        }
    }

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


    private Job toJob(String txt){
        return new Job();
    }


    private List<Job> jobList(){
        List<Object> values = redisTemplate.opsForHash().values(jobPoolKey);
        List<Job> jobs = values.stream().map(val -> toJob(String.valueOf(val))).collect(Collectors.toList());
        return jobs;
    }
}
