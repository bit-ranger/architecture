package com.rainyalley.architecture.util.jedis;

public class Job {

    private String topic;

    private String jobId;

    /**
     * 创建时间
     */
    private long create;

    /**
     * 延迟时间
     */
    private long delay;

    private String body;

    public Job() {
    }

    public Job(String topic, long create, long delay, String body) {
        this.topic = topic;
        this.create = create;
        this.delay = delay;
        this.body = body;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public long getCreate() {
        return create;
    }

    public void setCreate(long create) {
        this.create = create;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }
}
