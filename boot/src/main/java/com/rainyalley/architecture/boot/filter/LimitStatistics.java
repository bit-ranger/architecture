package com.rainyalley.architecture.boot.filter;

public interface LimitStatistics {


    long getCallTimes(String target);

    long getCallTimes(String target, String callerId);

    long increaseCallTimes(String target, String callerId);
}
