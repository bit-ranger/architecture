package com.rainyalley.architecture.boot.filter;

import com.rainyalley.architecture.core.util.AtomicExecutor;
import com.rainyalley.architecture.core.util.AtomicRunnerAdapter;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.rainyalley.architecture.boot.filter.LimitFilter.RejectReason.*;

public class LimitFilter extends OncePerRequestFilter {

    private LimitStrategy limitStrategy;

    private StatisticsStrategy statisticsStrategy;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String target = determineTarget(request);
        String caller = determineCaller(request);

        if(limitStrategy.isValidCall(request)){
            statisticsStrategy.incInvalidTimes(caller);
            reject(INVALID, request, response);
            return;
        }

        statisticsStrategy.incTimes(target, caller);

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
        boolean useGlobal = gloMaxCon > 0;
        if(useGlobal){
            if(statisticsStrategy.getGlobalConcurrency() >= gloMaxCon){
                return false;
            }
        }

        int tarMaxCon = limitStrategy.getTargetLimit(target) != null ? limitStrategy.getTargetLimit(target).getMaxConcurrency() : 0;
        boolean useTarget = tarMaxCon > 0;
        if(useTarget){
            if(statisticsStrategy.getTargetConcurrency(target) >= tarMaxCon){
                return false;
            }
        }

        int callMaxCon = limitStrategy.getCallerLimit(caller) != null ? limitStrategy.getCallerLimit(caller).getMaxConcurrency() : 0;
        boolean useCaller = callMaxCon > 0;
        if(useCaller) {
            if (statisticsStrategy.getCallerConcurrency(caller) >= callMaxCon) {
                return false;
            }
        }

        int tarCallMaxCon = limitStrategy.getTargetCallerLimit(target, caller) != null ? limitStrategy.getTargetCallerLimit(target, caller).getMaxConcurrency() : 0;
        boolean useTargetCaller = tarCallMaxCon > 0;
        if(useTargetCaller) {
            if (statisticsStrategy.getTargetCallerConcurrency(target, caller) >= tarCallMaxCon) {
                return false;
            }
        }

        AtomicExecutor atomicInvoker = new AtomicExecutor(4);
        if(useGlobal){
            atomicInvoker.add(new AtomicRunnerAdapter() {
                @Override
                public boolean run() {
                    int newCon = statisticsStrategy.incGlobalConcurrency();
                    return newCon <= gloMaxCon;
                }

                @Override
                public boolean rollback() {
                    statisticsStrategy.decGlobalConcurrency();
                    return true;
                }
            });
        }
        if(useTarget){
            atomicInvoker.add(new AtomicRunnerAdapter() {
                @Override
                public boolean run() {
                    int newCon = statisticsStrategy.incTargetConcurrency(target);
                    return newCon <= tarMaxCon;
                }

                @Override
                public boolean rollback() {
                    statisticsStrategy.decTargetConcurrency(target);
                    return true;
                }
            });
        }
        if(useCaller){
            atomicInvoker.add(new AtomicRunnerAdapter() {
                @Override
                public boolean run() {
                    int newCon = statisticsStrategy.incCallerConcurrency(caller);
                    return newCon <= callMaxCon;
                }

                @Override
                public boolean rollback() {
                    statisticsStrategy.decCallerConcurrency(caller);
                    return true;
                }
            });
        }
        if(useTargetCaller){
            atomicInvoker.add(new AtomicRunnerAdapter() {
                @Override
                public boolean run() {
                    int newCon = statisticsStrategy.incTargetCallerConcurrency(target, caller);
                    return newCon <= tarCallMaxCon;
                }

                @Override
                public boolean rollback() {
                    statisticsStrategy.decTargetCallerConcurrency(target, caller);
                    return true;
                }
            });
        }
        if(!atomicInvoker.execute()){
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
            statisticsStrategy.decTargetCallerConcurrency(target, caller);
        }
        if(acs.callerMax > 0){
            statisticsStrategy.decCallerConcurrency(caller);
        }
        if(acs.targetMax > 0){
            statisticsStrategy.decTargetConcurrency(target);
        }
        if(acs.globalMax > 0){
            statisticsStrategy.decGlobalConcurrency();
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
