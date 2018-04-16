package com.rainyalley.architecture.boot.filter;

public interface StatisticsStrategy {


    long getCallTimes(String target);

    long getCallTimes(String target, String callerId);

    /**
     * 增加调用次数
     * @param target 目标
     * @param callerId 调用者
     * @return 次数
     */
    long increaseCallTimes(String target, String callerId);

    /**
     * 增加无效调用的次数
     * @return
     */
    long increaseInvaidCallTimes(String target, String callerId);
}
