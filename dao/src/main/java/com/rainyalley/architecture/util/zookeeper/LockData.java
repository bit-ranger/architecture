package com.rainyalley.architecture.util.zookeeper;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class LockData {



    private static final String uuid = UUID.randomUUID().toString();

    private String machineId = uuid;

    private Thread currentThread;

    private String lockedNodeName;

    public LockData(Thread currentThread, String lockedNodeName) {
        this.machineId = machineId;
        this.currentThread = currentThread;
        this.lockedNodeName = lockedNodeName;
    }

    private AtomicInteger lockCount = new AtomicInteger(1);

    public Thread getCurrentThread() {
        return currentThread;
    }


    public AtomicInteger getLockCount() {
        return lockCount;
    }

    public String getLockedNodeName() {
        return lockedNodeName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"@class\":\"com.rainyalley.architecture.util.zookeeper.LockData\"");
        sb.append("\"@super\":\"").append(super.toString()).append("\"");
        sb.append("\"machineId\":\"")
                .append(machineId).append('\"');
        sb.append(",\"currentThread\":")
                .append(currentThread);
        sb.append(",\"lockedNodeName\":\"")
                .append(lockedNodeName).append('\"');
        sb.append(",\"lockCount\":")
                .append(lockCount);
        sb.append('}');
        return sb.toString();
    }
}
