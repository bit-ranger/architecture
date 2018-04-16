package com.rainyalley.architecture.boot.filter;

import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import static com.rainyalley.architecture.boot.filter.LimitFilter.RejectReason.*;

public class LimitFilter extends OncePerRequestFilter {

    private LimitStrategy limitStrategy;

    private StatisticsStrategy statisticsStrategy;

    /**
     * 全局并发
     */
    private AtomicInteger globalConcurrency = new AtomicInteger(0);

    /**
     * 目标并发表
     */
    private HashMap<String, AtomicInteger> targetConcurrency = new HashMap<>(32);

    /**
     * 调用者并发表
     */
    private HashMap<String, AtomicInteger> callerConcurrency = new HashMap<>(32);

    /**
     * 目标调用者并发表
     */
    private HashMap<String, AtomicInteger> targetCallerConcurrency = new HashMap<>(32);

    /**
     * 目标并发表锁
     */
    private ReentrantLock callerConcurrencyLock = new ReentrantLock();

    /**
     * 调用者并发表锁
     */
    private ReentrantLock targetConcurrencyLock = new ReentrantLock();

    /**
     * 目标调用者并发表锁
     */
    private ReentrantLock targetCallerConcurrencyLock = new ReentrantLock();


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String target = determineTarget(request);
        String caller = determineCaller(request);

        if(limitStrategy.isValidCall(request)){
            statisticsStrategy.increaseInvalidTimes(caller);
            reject(INVALID, request, response);
            return;
        }

        statisticsStrategy.increaseTimes(target, caller);

        //判断权限
        if(!limitStrategy.hasAuth(target, caller)){
            reject(NO_AUTH, request, response);
            return;
        }

        //申请并发量
        AcquireConcurrencySnapshot acs = new AcquireConcurrencySnapshot();
        if(!tryAcquireConcurrency(target, caller, acs)){
            reject(NO_CONCURRENCY, request, response);
            return;
        }

        try{
            //申请吞吐量
            if(!tryAcquireRate(target, caller)){
                reject(NO_RATE, request, response);
                return;
            }

            filterChain.doFilter(request, response);
        } finally {
            releaseConcurrency(target, caller, acs);
        }
    }



    /**
     * @param target 目标
     * @return 获取或者实例化目标并发量
     */
    private AtomicInteger getOrInstantTargetConcurrency(String target){
        AtomicInteger tarCon = targetConcurrency.get(target);
        if(tarCon != null){
            return tarCon;
        }

        targetConcurrencyLock.lock();
        try{
            AtomicInteger tarConOld = targetConcurrency.get(target);
            if(tarConOld != null){
                return tarConOld;
            }
            AtomicInteger tarConNew = new AtomicInteger(0);
            targetConcurrency.put(target, tarConNew);
            return tarConNew;
        } finally {
            targetConcurrencyLock.unlock();
        }
    }

    /**
     * @param target 目标
     * @return 获取或者实例化目标并发量
     */
    private AtomicInteger getOrInstantCallerConcurrency(String target){
        AtomicInteger callCon = callerConcurrency.get(target);
        if(callCon != null){
            return callCon;
        }

        callerConcurrencyLock.lock();
        try{
            AtomicInteger callConOld = callerConcurrency.get(target);
            if(callConOld != null){
                return callConOld;
            }
            AtomicInteger callConNew = new AtomicInteger(0);
            callerConcurrency.put(target, callConNew);
            return callConNew;
        } finally {
            callerConcurrencyLock.unlock();
        }
    }


    /**
     * @param targetCaller 目标调用者
     * @return 获取或者实例化目标并发量
     */
    private AtomicInteger getOrInstantTargetCallerConcurrency(String targetCaller){
        AtomicInteger tarCallCon = targetCallerConcurrency.get(targetCaller);
        if(tarCallCon != null){
            return tarCallCon;
        }

        targetCallerConcurrencyLock.lock();
        try{
            AtomicInteger tarCallConOld = targetCallerConcurrency.get(targetCaller);
            if(tarCallConOld != null){
                return tarCallConOld;
            }
            AtomicInteger tarCallConNew = new AtomicInteger(0);
            targetCallerConcurrency.put(targetCaller, tarCallConNew);
            return tarCallConNew;
        } finally {
            targetCallerConcurrencyLock.unlock();
        }
    }


    protected String determineCaller(HttpServletRequest request){
        return request.getParameter("userId");
    }

    protected String determineTarget(HttpServletRequest request){
        return request.getParameter("target");
    }

    protected void reject(RejectReason reason, HttpServletRequest request, HttpServletResponse response){}


    /**
     *
     * @return 申请并发量
     */
    private boolean tryAcquireConcurrency(String target, String caller, AcquireConcurrencySnapshot acs){
        int gloMaxCon = limitStrategy.getGlobalLimit() != null ? limitStrategy.getGlobalLimit().getMaxConcurrency() : 0;
        gloMaxCon = Integer.max(gloMaxCon, 0);
        if(gloMaxCon > 0){
            if(globalConcurrency.get() >= gloMaxCon){
                return false;
            }
        }

        int tarMaxCon = limitStrategy.getTargetLimit(target) != null ? limitStrategy.getTargetLimit(target).getMaxConcurrency() : 0;
        tarMaxCon = Integer.max(tarMaxCon, 0);
        AtomicInteger tarCon = null;
        if(tarMaxCon > 0){
            tarCon = getOrInstantTargetConcurrency(target);
            if(tarCon.get() >= tarMaxCon){
                return false;
            }
        }

        int callMaxCon = limitStrategy.getCallerLimit(caller) != null ? limitStrategy.getCallerLimit(caller).getMaxConcurrency() : 0;
        callMaxCon = Integer.max(callMaxCon, 0);
        AtomicInteger callCon = null;
        if(callMaxCon > 0) {
            callCon = getOrInstantTargetConcurrency(target);
            if (callCon.get() >= callMaxCon) {
                return false;
            }
        }

        int tarCallMaxCon = limitStrategy.getTargetCallerLimit(target, caller) != null ? limitStrategy.getTargetCallerLimit(target, caller).getMaxConcurrency() : 0;
        tarCallMaxCon = Integer.max(tarCallMaxCon, 0);
        AtomicInteger tarCallCon = null;
        if(tarCallMaxCon > 0) {
            tarCallCon = getOrInstantTargetCallerConcurrency(target + ":" + caller);
            if (tarCallCon.get() >= tarCallMaxCon) {
                return false;
            }
        }

        int gloNewCon = Integer.MIN_VALUE;
        int tarNewCon = Integer.MIN_VALUE;
        int callNewCon = Integer.MIN_VALUE;
        int tarCallNewCon = Integer.MIN_VALUE;

        if(gloMaxCon > 0){
            gloNewCon = globalConcurrency.incrementAndGet();
        }

        if(tarMaxCon > 0){
            tarNewCon = tarCon.incrementAndGet();
        }


        if(callMaxCon > 0){
            callNewCon = callCon.incrementAndGet();
        }

        if(tarCallMaxCon > 0){
            tarCallNewCon = tarCallCon.incrementAndGet();
        }

        boolean targetCallerFail = tarCallNewCon > tarCallMaxCon;
        if(targetCallerFail){
            tarCallCon.decrementAndGet();
        }

        boolean callerFail = callNewCon > callMaxCon;
        if(callerFail){
            callCon.decrementAndGet();
        }

        boolean targetFail = tarNewCon > tarMaxCon;
        if(targetFail){
            tarCon.decrementAndGet();
        }

        boolean globalFail = gloNewCon > gloMaxCon;
        if(globalFail){
            globalConcurrency.decrementAndGet();
        }

        if(callerFail || targetFail || globalFail){
            return false;
        }

        acs.globalMax = gloMaxCon;
        acs.targetMax = tarMaxCon;
        acs.callerMax = callMaxCon;
        acs.targetCallerMax = tarCallMaxCon;
        return true;
    }

    private void releaseConcurrency(String target, String caller, AcquireConcurrencySnapshot acs){
        if(acs.targetCallerMax > 0){
            getOrInstantTargetCallerConcurrency(caller).decrementAndGet();
        }
        if(acs.callerMax > 0){
            getOrInstantCallerConcurrency(caller).decrementAndGet();
        }
        if(acs.targetMax > 0){
            getOrInstantTargetConcurrency(target);
        }
        if(acs.globalMax > 0){
            globalConcurrency.decrementAndGet();
        }
    }

    /**
     * 申请吞吐量, 若申请失败, 则表示系统负载过高
     * @param target 目标
     * @return
     */
    private boolean tryAcquireRate(String target, String caller){

        if(limitStrategy.getGlobalLimit() != null && limitStrategy.getGlobalLimit().getRateLimiter() != null){
            if(!limitStrategy.getGlobalLimit().getRateLimiter().tryAcquire()){
                return false;
            }
        }

        if(limitStrategy.getTargetLimit(target) != null && limitStrategy.getTargetLimit(target).getRateLimiter() != null){
            if(!limitStrategy.getTargetLimit(target).getRateLimiter().tryAcquire()){
                return false;
            }
        }

        if(limitStrategy.getCallerLimit(caller) != null && limitStrategy.getCallerLimit(caller).getRateLimiter()!= null){
            if(!limitStrategy.getCallerLimit(caller).getRateLimiter().tryAcquire()){
                return false;
            }
        }

        if(limitStrategy.getTargetCallerLimit(target, caller) != null && limitStrategy.getTargetCallerLimit(target, caller).getRateLimiter() != null){
            if(!limitStrategy.getTargetCallerLimit(target, caller).getRateLimiter().tryAcquire()){
                return false;
            }
        }

        return true;
    }


    protected enum  RejectReason{
        /**
         * 非法访问
         */
        INVALID,

        /**
         * 无权访问
         */
        NO_AUTH,

        /**
         * 无并发量
         */
        NO_CONCURRENCY,

        /**
         * 无吞吐量
         */
        NO_RATE
    }

    public LimitStrategy getLimitStrategy() {
        return limitStrategy;
    }

    public void setLimitStrategy(LimitStrategy limitStrategy) {
        this.limitStrategy = limitStrategy;
    }

    public StatisticsStrategy getStatisticsStrategy() {
        return statisticsStrategy;
    }

    public void setStatisticsStrategy(StatisticsStrategy statisticsStrategy) {
        this.statisticsStrategy = statisticsStrategy;
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        Assert.notNull(limitStrategy, "limitStrategy can not be null");
        Assert.notNull(statisticsStrategy, "statisticsStrategy can not be null");
    }

    private static class AcquireConcurrencySnapshot{
        private int globalMax;
        private int targetMax;
        private int callerMax;
        private int targetCallerMax;
    }
}
