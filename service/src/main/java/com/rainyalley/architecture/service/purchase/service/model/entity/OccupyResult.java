package com.rainyalley.architecture.service.purchase.service.model.entity;

/**
 * occupy结果
 */
public class OccupyResult {

    /**
     * 失败原因
     */
	public enum FailureCause{

    	/**
    	 * 并发
    	 */
    	CONCURRENT,

    	/**
    	 * 库存不足
    	 */
        UNDER_STOCK,

        /**
         * 未知错误
         */
        UNKNOWN_ERROR
	}


    /**
     * 商品ID
     */
    private String entityId;
    /**
     * 抢占数量
     */
    private long occupyNum;
    /**
     * 剩余数量
     */
    private long restNum;
    /**
     * 失败原因
     */
    private FailureCause failureCause;


	/**
	 * 抢占结果
	 * @param entityId 商品Id
     * @param num 抢占数量
	 */
	public OccupyResult(final String entityId, final long num, final long restNum) {
        this.entityId = entityId;
        this.occupyNum = num;
        this.restNum = restNum;
        this.failureCause = null;
	}

	/**
	 * 抢占结果
     * @param entityId 商品Id
	 * @param failureCause 失败原因 {@link FailureCause}
	 */
	public OccupyResult(final String entityId, final long num, final long restNum, final FailureCause failureCause) {
        this.entityId = entityId;
        this.occupyNum = num;
        this.restNum = restNum;
		this.failureCause = failureCause;
	}

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public long getOccupyNum() {
        return occupyNum;
    }

    public void setOccupyNum(long occupyNum) {
        this.occupyNum = occupyNum;
    }

    public long getRestNum() {
        return restNum;
    }

    public void setRestNum(long restNum) {
        this.restNum = restNum;
    }

    public FailureCause getFailureCause() {
        return failureCause;
    }

    public void setFailureCause(FailureCause failureCause) {
        this.failureCause = failureCause;
    }
}