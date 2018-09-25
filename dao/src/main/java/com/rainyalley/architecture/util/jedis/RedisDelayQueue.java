package com.rainyalley.architecture.util.jedis;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;

public class RedisDelayQueue {

    private RedisTemplate<String,String> redisTemplate;

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
    private String jobIdKey = "";


    public boolean push(Job job){
        Long jobId = redisTemplate.opsForValue().increment(jobIdKey, 1);
        job.setJobId(String.valueOf(jobId));
        redisTemplate.opsForHash().put(jobPoolKey, String.valueOf(jobId), job);
        return redisTemplate.opsForZSet().add(delayBucketKey, String.valueOf(jobId), job.getCreate() + job.getDelay());
    }

    public Job peek(){
        while (true){
            Set<String> readySet  = peekWithLock();
            if(readySet.size() != 0){
                String  ready = readySet.iterator().next();
                return toJob(ready);
            } else {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) { }
            }
        }
    }


    private Set<String> peekWithLock(){
        return  redisTemplate.opsForZSet().range(readyQueueKey, 0, 0);
    }


    private Job toJob(String txt){
        return new Job();
    }
}
