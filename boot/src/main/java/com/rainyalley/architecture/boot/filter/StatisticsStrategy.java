package com.rainyalley.architecture.boot.filter;

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
    long increaseTimes(String target, String caller);


    /**
     * 增加无效调用的次数
     * @param callerId 调用者
     * @return
     */
    long increaseInvalidTimes(String callerId);
}
