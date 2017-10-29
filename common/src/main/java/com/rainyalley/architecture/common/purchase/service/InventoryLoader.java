package com.rainyalley.architecture.common.purchase.service;

public interface InventoryLoader {

    long load(String entityId);

    boolean isFinalStatus(String entity);

}
