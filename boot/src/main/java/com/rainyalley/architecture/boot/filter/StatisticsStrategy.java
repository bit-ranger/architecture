package com.rainyalley.architecture.boot.filter;

public interface StatisticsStrategy {

    /**
     * 获取目标调用次数
     * @param target
     * @return
     */
    long getCallTimes(String target);

    /**
     * 获取指定用户调用指定目标的次数
     * @param target
     * @param callerId
     * @return
     */
    long getCallTimes(String target, String callerId);

    /**
     * 获取无效调用的总次数
     * @return
     */
    long getInvaidCallTimes();

    /**
     * 获取指定用户的无效调用次数
     * @param callerId
     * @return
     */
    long getInvaidCallTimes(String callerId);

    /**
     * 增加调用次数
     * @param target 目标
     * @param callerId 调用者
     * @return 次数
     */
    long increaseCallTimes(String target, String callerId);


    /**
     * 增加无效调用的次数
     * @param callerId 调用者
     * @return
     */
    long increaseInvaidCallTimes(String callerId);
}
