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

public class LimitFilter extends OncePerRequestFilter {

    private LimitConfig limitConfig;

    private LimitStatistics limitStatistics;

    /**
     * 并发量
     */
    private AtomicInteger concurrency = new AtomicInteger(0);

    private ConcurrentHashMap<String, AtomicInteger> targetConcurrency = new ConcurrentHashMap<>(32);

    private ReentrantLock targetConcurrencyLock = new ReentrantLock();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String callerId = determineCallerId(request);
        String target = determineTarget(request);

        //判断权限
        if(!limitConfig.hasAuth(target, callerId)){
            reject(request, response);
            return;
        }

        //申请目标并发量，
        //该方法必须在hasAuth之后执行，以确保target是有效的，
        //否则恶意的调动可能使target数量膨胀引发内存泄露
        if(!tryAcquireConcurrency(target)){
            reject(request, response);
            return;
        }

        try{




            try {
                //申请吞吐量
                boolean tryRate = tryAcquireRate(target, callerId);
                if(!tryRate){
                    reject(request, response);
                    return;
                }

                limitStatistics.increaseCallTimes(target, callerId);

            } finally {
                instantTargetConcurrency(target).decrementAndGet();
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
    private AtomicInteger instantTargetConcurrency(String target){
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

    protected void reject(HttpServletRequest request, HttpServletResponse response){

    }

    /**
     *
     * @return 申请并发量
     */
    private boolean tryAcquireConcurrency(String target){
        if(concurrency.get() >= limitConfig.getMaxConcurrency()){
            return false;
        }

        AtomicInteger tarCon = instantTargetConcurrency(target);
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
     * @param callerId 调用者
     * @return
     */
    private boolean tryAcquireRate(String target, String callerId){
        if(!limitConfig.getRateLimiter().tryAcquire()){
            return false;
        }

        if(!limitConfig.getRateLimiter(callerId).tryAcquire()){
            return false;
        }

        if(!limitConfig.getTargetRateLimiter(target).tryAcquire()){
            return false;
        }

        if(!limitConfig.getTargetRateLimiter(target, callerId).tryAcquire()){
            return false;
        }
        return true;
    }
}
