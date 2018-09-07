package com.rainyalley.architecture.filter.limit;

public enum  RejectReason {

    /**
     * 无权访问
     */
    NO_AUTH,

    /**
     * 无并发量
     */
    NO_CONCURRENCY,

    /**
     * 太频繁
     */
    TOO_FREQUENCY
}
