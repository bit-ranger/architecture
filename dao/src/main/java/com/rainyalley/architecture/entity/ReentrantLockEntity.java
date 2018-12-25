package com.rainyalley.architecture.entity;

import lombok.ToString;

import java.util.Date;

@ToString
public class ReentrantLockEntity {
    private String target;

    private Date acquireTime;

    private Date expireTime;

    private Integer entrantCount;

    private String lockKeeper;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Date getAcquireTime() {
        return acquireTime;
    }

    public void setAcquireTime(Date acquireTime) {
        this.acquireTime = acquireTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Integer getEntrantCount() {
        return entrantCount;
    }

    public void setEntrantCount(Integer entrantCount) {
        this.entrantCount = entrantCount;
    }

    public String getLockKeeper() {
        return lockKeeper;
    }

    public void setLockKeeper(String lockKeeper) {
        this.lockKeeper = lockKeeper;
    }
}