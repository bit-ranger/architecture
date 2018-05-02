package com.rainyalley.architecture.filter.limit;

import com.google.common.util.concurrent.RateLimiter;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 默认的限制策略
 */
public class DefaultLimitStrategy implements LimitStrategy {

    private LimitInfoStorage limitInfoStorage;

    private Limit globalLimit;

    private Map<String, Limit> targetLimit;

    private Map<String, Limit> callerLimit;

    private Map<String, Limit> targetCallerLimit;

    private Set<String> targetCallerAuth;

    private Set<String> validTargetSet;

    @Override
    public Limit getGlobalLimit() {
        return globalLimit;
    }

    @Override
    public Limit getCallerLimit(String caller) {
        return callerLimit.get(caller);
    }

    @Override
    public Limit getTargetLimit(String target) {
        return targetLimit.get(target);
    }

    @Override
    public Limit getTargetCallerLimit(String target, String caller) {
        return targetCallerLimit.get(toKey(target, caller));
    }

    @Override
    public boolean isValidCall(HttpServletRequest request) {
        String target = determineTarget(request);
        return validTargetSet.contains(target);
    }

    @Override
    public boolean hasAuth(String target, String caller) {
        String key = toKey(target, caller);
        return targetCallerAuth.contains(key);
    }

    public void setLimitInfoStorage(LimitInfoStorage limitInfoStorage) {
        this.limitInfoStorage = limitInfoStorage;
        init();
    }

    private void init(){
        GlobalLimitInfo globalLimitInfo = limitInfoStorage.getGlobalLimitInfo();
        globalLimit = new Limit();
        globalLimit.setMaxConcurrency(globalLimitInfo.getMaxConcurrency());
        if(globalLimitInfo.getPermitsPerSecond() > 0 && globalLimitInfo.getWarmupPeriod() > 0){
            globalLimit.setRateLimiter(RateLimiter.create(Double.valueOf(String.valueOf(globalLimitInfo.getPermitsPerSecond())), globalLimitInfo.getWarmupPeriod(), TimeUnit.SECONDS));
        }
        validTargetSet = globalLimitInfo.getValidTargetSet();

        List<TargetLimitInfo> targetLimitInfo = limitInfoStorage.getTargetLimitInfo();
        targetLimit = new HashMap<>(targetLimitInfo.size());
        for (TargetLimitInfo tli : targetLimitInfo) {
            Limit limit = new Limit();
            limit.setMaxConcurrency(tli.getMaxConcurrency());
            if(tli.getPermitsPerSecond() > 0 && tli.getWarmupPeriod() > 0){
                limit.setRateLimiter(RateLimiter.create(Double.valueOf(String.valueOf(tli.getPermitsPerSecond())), tli.getWarmupPeriod(), TimeUnit.SECONDS));
            }
            targetLimit.put(tli.getTarget(), limit);

        }

        List<CallerLimitInfo> callerLimitInfo = limitInfoStorage.getCallerLimitInfo();
        callerLimit = new HashMap<>(callerLimitInfo.size());
        for (CallerLimitInfo cli : callerLimitInfo) {
            Limit limit = new Limit();
            limit.setMaxConcurrency(cli.getMaxConcurrency());
            if(cli.getPermitsPerSecond() > 0 && cli.getWarmupPeriod() > 0){
                limit.setRateLimiter(RateLimiter.create(Double.valueOf(String.valueOf(cli.getPermitsPerSecond())), cli.getWarmupPeriod(), TimeUnit.SECONDS));
            }
            targetLimit.put(cli.getCaller(), limit);
        }

        List<TargetCallerLimitInfo> targetCallerLimitInfo = limitInfoStorage.getTargetCallerLimitInfo();
        targetCallerLimit = new HashMap<>(targetCallerLimitInfo.size());
        for (TargetCallerLimitInfo tcli : targetCallerLimitInfo) {
            Limit limit = new Limit();
            limit.setMaxConcurrency(tcli.getMaxConcurrency());
            if(tcli.getPermitsPerSecond() > 0 && tcli.getWarmupPeriod() > 0){
                limit.setRateLimiter(RateLimiter.create(Double.valueOf(String.valueOf(tcli.getPermitsPerSecond())), tcli.getWarmupPeriod(), TimeUnit.SECONDS));
            }
            targetCallerLimit.put(toKey(tcli.getTarget(), tcli.getCaller()), limit);
        }

        targetCallerAuth = targetCallerLimit.keySet();
    }

    private String toKey(String target, String caller){
        return target + ":" + caller;
    }

    private String determineTarget(HttpServletRequest request){
        return "";
    }
}
