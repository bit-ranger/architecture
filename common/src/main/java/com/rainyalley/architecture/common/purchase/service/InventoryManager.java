package com.rainyalley.architecture.common.purchase.service;


import com.rainyalley.architecture.common.purchase.service.model.entity.OccupyResult;

public interface InventoryManager {

    /**
     * 加载库存
     * @param entityId 商品ID
     * @return
     */
    void store(final String entityId);

    /**
     * 获取库存值
     * @param entityId 商品ID
     * @return
     */
    long get(final String entityId);


    /**
     * 抢占
     * @param entityId 商品ID
     * @param occupyNum 抢占数量
     * @return
     */
    OccupyResult occupy(String entityId, long occupyNum);

    /**
     * 释放库存
     * @param entityId
     * @return
     */
    boolean release(String entityId, long releaseNum);

}
