package com.rainyalley.architecture.boot.filter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import static com.rainyalley.architecture.boot.filter.LimitFilter.RejectReason.*;

public class LimitFilter extends OncePerRequestFilter {

    private LimitStrategy limitConfig;

    private LimitStatisticsStrategy limitStatistics;

    /**
     * 并发量
     */
    private AtomicInteger concurrency = new AtomicInteger(0);

    private ConcurrentHashMap<String, AtomicInteger> targetConcurrency = new ConcurrentHashMap<>(32);

    private ReentrantLock targetConcurrencyLock = new ReentrantLock();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String target = determineTarget(request);
        String callerId = determineCallerId(request);

        if(limitConfig.isValidCall(request)){
            limitStatistics.increaseInvaidCallTimes(target, callerId);
            reject(INVALID, request, response);
            return;
        }

        limitStatistics.increaseCallTimes(target, callerId);

        if(!tryAcquireConcurrency(target)){
            reject(NO_CONCURRENCY, request, response);
            return;
        }

        try{
            //判断权限
            if(!limitConfig.hasAuth(target, callerId)){
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
            concurrency.decrementAndGet();
        }
    }



    /**
     *
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
        if(concurrency.get() >= limitConfig.getMaxConcurrency()){
            return false;
        }

        AtomicInteger tarCon = getOrInstantTargetConcurrency(target);
        if(tarCon.get() >= limitConfig.getMaxConcurrency(target)){
            return false;
        }

        int newCon = concurrency.incrementAndGet();
        int newTarCon = tarCon.incrementAndGet();

        if(newCon > limitConfig.getMaxConcurrency() || newTarCon > limitConfig.getMaxConcurrency(target)){
            concurrency.decrementAndGet();
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
        if(!limitConfig.getRateLimiter().tryAcquire()){
            return false;
        }

        if(!limitConfig.getTargetRateLimiter(target).tryAcquire()){
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
        if(!limitConfig.getRateLimiter(callerId).tryAcquire()){
            return false;
        }

        if(!limitConfig.getTargetRateLimiter(target, callerId).tryAcquire()){
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

}
