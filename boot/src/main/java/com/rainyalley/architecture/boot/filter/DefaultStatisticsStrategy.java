package com.rainyalley.architecture.boot.filter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultStatisticsStrategy implements StatisticsStrategy {

    private Map<String, AtomicInteger> targetCallerTimes = new HashMap<>();

    private ReentrantLock targetCallerTimesLock = new ReentrantLock();

    private AtomicInteger invalidTimes = new AtomicInteger(0);

    @Override
    public long getTimes(String target) {
        return 0;
    }

    @Override
    public long getTimes(String target, String caller) {
        return getOrInstantTargetCallerTimes(toKey(target, caller)).get();
    }

    @Override
    public long getInvalidTimes() {
        return invalidTimes.get();
    }

    @Override
    public long getInvalidTimes(String callerId) {
        return 0;
    }

    @Override
    public long increaseTimes(String target, String caller) {
        return getOrInstantTargetCallerTimes(toKey(target, caller)).incrementAndGet();
    }

    @Override
    public long increaseInvalidTimes(String callerId) {
        return invalidTimes.incrementAndGet();
    }



    private AtomicInteger getOrInstantTargetCallerTimes(String targetCaller){
        AtomicInteger tarCallCon = targetCallerTimes.get(targetCaller);
        if(tarCallCon != null){
            return tarCallCon;
        }

        targetCallerTimesLock.lock();
        try{
            AtomicInteger tarCallConOld = targetCallerTimes.get(targetCaller);
            if(tarCallConOld != null){
                return tarCallConOld;
            }
            AtomicInteger tarCallConNew = new AtomicInteger(0);
            targetCallerTimes.put(targetCaller, tarCallConNew);
            return tarCallConNew;
        } finally {
            targetCallerTimesLock.unlock();
        }
    }

    private String toKey(String target, String caller){
        return target + ":" + caller;
    }
}
