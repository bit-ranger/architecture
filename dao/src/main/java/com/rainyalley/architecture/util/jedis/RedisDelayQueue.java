package com.rainyalley.architecture.util.jedis;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisOperations;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;

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
     * 执行中队列, zset
     */
    private String runningQueueKey = "";

    /**
     * jobId生成器
     */
    private String jobIdKey = "";

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @NotNull
    @Override
    public Iterator<Job> iterator() {
        return null;
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        return null;
    }

    @Override
    public boolean add(Job job) {
        return offer(job);
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends Job> c) {
        return false;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean offer(Job job) {
        Long jobId = redisTemplate.opsForValue().increment(jobIdKey, 1);
        job.setJobId(String.valueOf(jobId));
        redisTemplate.opsForHash().put(jobPoolKey, String.valueOf(jobId), job);
        return redisTemplate.opsForZSet().add(delayBucketKey, String.valueOf(jobId), job.getCreate() + job.getDelay());
    }

    @Override
    public Job remove() {
        return null;
    }

    @Override
    public Job poll() {
        return null;
    }

    @Override
    public Job element() {
        return null;
    }

    @Override
    public Job peek(){
        Set<String> readySet  = peekWithLock();
        if(readySet.size() != 0){
            String  ready = readySet.iterator().next();
            return toJob(ready);
        } else {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) { }
        }
        return null;
    }


    private Set<String> peekWithLock(){
        return  redisTemplate.opsForZSet().range(readyQueueKey, 0, 0);
    }


    private Job toJob(String txt){
        return new Job();
    }
}
