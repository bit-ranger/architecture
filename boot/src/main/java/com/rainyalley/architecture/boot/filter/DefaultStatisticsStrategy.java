package com.rainyalley.architecture.boot.filter;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultStatisticsStrategy implements StatisticsStrategy {

    /**
     * 全局并发
     */
    private AtomicInteger globalConcurrency = new AtomicInteger(0);

    /**
     * 目标并发表
     */
    private MapValueInitial<String,AtomicInteger> targetConcurrency = new MapValueInitial<>(new HashMap<>(32), () -> new AtomicInteger(0));

    /**
     * 调用者并发表
     */
    private MapValueInitial<String,AtomicInteger> callerConcurrency = new MapValueInitial<>(new HashMap<>(32), () -> new AtomicInteger(0));

    /**
     * 目标调用者并发表
     */
    private MapValueInitial<String,AtomicInteger> targetCallerConcurrency = new MapValueInitial<>(new HashMap<>(32), () -> new AtomicInteger(0));

    /**
     * 调用者调用目标的次数表
     */
    private MapValueInitial<String,AtomicInteger> targetCallerTimes = new MapValueInitial<>(new HashMap<>(32), () -> new AtomicInteger(0));

    /**
     * 无效调用次数
     */
    private AtomicInteger invalidTimes = new AtomicInteger(0);

    /**
     * 调用者无效调用次数
     */
    private MapValueInitial<String,AtomicInteger> callerInvalidTimes = new MapValueInitial<>(new HashMap<>(32), () -> new AtomicInteger(0));

    @Override
    public long getTimes(String target) {
        return 0;
    }

    @Override
    public long getTimes(String target, String caller) {
        return targetCallerTimes.get(toKey(target, caller)).get();
    }

    @Override
    public long getInvalidTimes() {
        return invalidTimes.get();
    }

    @Override
    public long getInvalidTimes(String callerId) {
        return callerInvalidTimes.get(callerId).get();
    }

    @Override
    public long incTimes(String target, String caller) {
        return targetCallerTimes.get(toKey(target, caller)).incrementAndGet();
    }

    @Override
    public long incInvalidTimes(String callerId) {
        invalidTimes.incrementAndGet();
        return callerInvalidTimes.get(callerId).incrementAndGet();
    }

    @Override
    public int getGlobalConcurrency() {
        return globalConcurrency.get();
    }

    @Override
    public int incGlobalConcurrency() {
        return globalConcurrency.incrementAndGet();
    }

    @Override
    public int decGlobalConcurrency() {
        return globalConcurrency.decrementAndGet();
    }

    @Override
    public int getTargetConcurrency(String target) {
        return targetConcurrency.get(target).get();
    }

    @Override
    public int incTargetConcurrency(String target) {
        return targetConcurrency.get(target).incrementAndGet();
    }

    @Override
    public int decTargetConcurrency(String target) {
        return targetConcurrency.get(target).decrementAndGet();
    }

    @Override
    public int getCallerConcurrency(String caller) {
        return callerConcurrency.get(caller).get();
    }

    @Override
    public int incCallerConcurrency(String caller) {
        return callerConcurrency.get(caller).incrementAndGet();
    }

    @Override
    public int decCallerConcurrency(String caller) {
        return callerConcurrency.get(caller).decrementAndGet();
    }

    @Override
    public int getTargetCallerConcurrency(String target, String caller) {
        return targetCallerConcurrency.get(toKey(target, caller)).get();
    }

    @Override
    public int incTargetCallerConcurrency(String target, String caller) {
        return targetCallerConcurrency.get(toKey(target, caller)).incrementAndGet();
    }

    @Override
    public int decTargetCallerConcurrency(String target, String caller) {
        return targetCallerConcurrency.get(toKey(target, caller)).decrementAndGet();
    }


    private String toKey(String target, String caller){
        return target + ":" + caller;
    }
}
