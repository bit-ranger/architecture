package com.rainyalley.architecture.service.purchase.service;

public interface Lock {
    boolean hasLock(String entityId, String owner);

    boolean tryLock(String entityId, String owner);

    boolean lock(String entityId, String owner);

    boolean unLock(String entityId, String owner);
}
