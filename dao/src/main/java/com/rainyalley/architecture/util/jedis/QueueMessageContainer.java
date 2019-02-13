package com.rainyalley.architecture.util.jedis;


import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author bin.zhang
 */
public class QueueMessageContainer {

    private RedisDelayQueue redisDelayQueue;

    private Map<String, TopicJob> observableMap;

    private ScheduleTask task;

    public QueueMessageContainer(RedisDelayQueue redisDelayQueue, Map<String, Observer> observerMap,
                                 long initialDelay,
                                 long delay,
                                 TimeUnit unit) {
        this.task = new ScheduleTask();
        this.redisDelayQueue = redisDelayQueue;

        this.observableMap = new HashMap<>(observerMap.size());
        for (Map.Entry<String, Observer> entry : observerMap.entrySet()) {
            TopicJob observable = new TopicJob();
            observable.addObserver(entry.getValue());
            observableMap.put(entry.getKey(), observable);
        }

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(
                1,
                new CustomizableThreadFactory("queue-msg-"));
        executor.scheduleWithFixedDelay(this.task, initialDelay, delay, unit);
    }

    public void runTask(){
        task.run();
    }


    private class ScheduleTask implements Runnable{
        @Override
        public void run() {
            Job job = redisDelayQueue.peek();
            if(Objects.isNull(job)){
                return;
            }

            TopicJob observable =  observableMap.get(job.getTopic());
            if(Objects.isNull(observable)){
                redisDelayQueue.remove(job);
                return;
            }
            observable.setChanged();
            observable.notifyObservers(job);
            observable.clearChanged();

            redisDelayQueue.remove(job);
        }
    }

    private static class TopicJob extends Observable{

        @Override
        public synchronized void setChanged() {
            super.setChanged();
        }

        @Override
        public synchronized void clearChanged() {
            super.clearChanged();
        }
    }
}
