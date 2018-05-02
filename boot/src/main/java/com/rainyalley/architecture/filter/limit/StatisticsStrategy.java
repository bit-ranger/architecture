package com.rainyalley.architecture.filter.limit;

public interface StatisticsStrategy {

    /**
     * 获取目标调用次数
     * @param target
     * @return
     */
    long getTimes(String target);

    /**
     * 获取指定用户调用指定目标的次数
     * @param target
     * @param caller
     * @return
     */
    long getTimes(String target, String caller);

    /**
     * 获取无效调用的总次数
     * @return
     */
    long getInvalidTimes();

    /**
     * 获取指定用户的无效调用次数
     * @param callerId
     * @return
     */
    long getInvalidTimes(String callerId);

    /**
     * 增加调用次数
     * @param target 目标
     * @param caller 调用者
     * @return 次数
     */
    long incTimes(String target, String caller);


    /**
     * 增加无效调用的次数
     * @param callerId 调用者
     * @return
     */
    long incInvalidTimes(String callerId);

    int getGlobalConcurrency();

    int incGlobalConcurrency();

    int decGlobalConcurrency();

    int getTargetConcurrency(String target);

    int incTargetConcurrency(String target);

    int decTargetConcurrency(String target);

    int getCallerConcurrency(String caller);

    int incCallerConcurrency(String caller);

    int decCallerConcurrency(String caller);

    int getTargetCallerConcurrency(String target, String caller);

    int incTargetCallerConcurrency(String target, String caller);

    int decTargetCallerConcurrency(String target, String caller);
}
