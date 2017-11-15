package com.rainyalley.architecture.common.purchase.service;

public interface Lock {
    boolean hasLock(String entityId, String owner);

    boolean tryLock(String entityId, String owner);

    boolean lock(String entityId, String owner);

    boolean unLock(String entityId, String owner);
}
