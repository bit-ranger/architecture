package com.rainyalley.architecture.boot.filter;

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
     * 目标并发表锁
     */
    private ReentrantLock targetConcurrencyLock = new ReentrantLock();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String target = determineTarget(request);
        String callerId = determineCallerId(request);

        if(limitStrategy.isValidCall(request)){
            statisticsStrategy.increaseInvaidCallTimes(callerId);
            reject(INVALID, request, response);
            return;
        }

        statisticsStrategy.increaseCallTimes(target, callerId);

        if(!tryAcquireConcurrency(target)){
            reject(NO_CONCURRENCY, request, response);
            return;
        }

        try{
            //判断权限
            if(!limitStrategy.hasAuth(target, callerId)){
                reject(NO_AUTH, request, response);
                return;
            }

            try {
                //申请吞吐量
                if(!tryAcquireRate(target)){
                    reject(NO_RATE, request, response);
                    return;
                }

                if(!tryAcquireCallerRate(target, callerId)){
                    reject(NO_CALLER_RATE, request, response);
                    return;
                }

                filterChain.doFilter(request, response);
            } finally {
                getOrInstantTargetConcurrency(target).decrementAndGet();
            }
        } finally {
            globalConcurrency.decrementAndGet();
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


    protected String determineCallerId(HttpServletRequest request){
        return request.getParameter("userId");
    }

    protected String determineTarget(HttpServletRequest request){
        return request.getParameter("target");
    }

    protected void reject(RejectReason reason, HttpServletRequest request, HttpServletResponse response){

    }


    /**
     *
     * @return 申请并发量
     */
    private boolean tryAcquireConcurrency(String target){
        if(globalConcurrency.get() >= limitStrategy.getMaxConcurrency()){
            return false;
        }

        AtomicInteger tarCon = getOrInstantTargetConcurrency(target);
        if(tarCon.get() >= limitStrategy.getMaxConcurrency(target)){
            return false;
        }

        int newCon = globalConcurrency.incrementAndGet();
        int newTarCon = tarCon.incrementAndGet();

        if(newCon > limitStrategy.getMaxConcurrency() || newTarCon > limitStrategy.getMaxConcurrency(target)){
            globalConcurrency.decrementAndGet();
            tarCon.decrementAndGet();
            return false;
        }

        return true;
    }

    /**
     * 申请吞吐量
     * @param target 目标
     * @return
     */
    private boolean tryAcquireRate(String target){
        if(!limitStrategy.getGlobalRateLimiter().tryAcquire()){
            return false;
        }

        if(!limitStrategy.getTargetRateLimiter(target).tryAcquire()){
            return false;
        }
        return true;
    }

    /**
     * 申请吞吐量
     * @param target 目标
     * @param callerId 调用者
     * @return
     */
    private boolean tryAcquireCallerRate(String target, String callerId){
        if(!limitStrategy.getGlobalRateLimiter(callerId).tryAcquire()){
            return false;
        }

        if(!limitStrategy.getTargetRateLimiter(target, callerId).tryAcquire()){
            return false;
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
        NO_RATE,

        /**
         * 无调用者专属吞吐量
         */
        NO_CALLER_RATE
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
}
