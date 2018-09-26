package com.rainyalley.architecture.util.jedis;

/**
 * @author bin.zhang
 */
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

    /**
     * 预期job执行时间
     */
    private long ttr;

    public Job(String topic, long create, long delay, long ttr, String body) {
        this.topic = topic;
        this.create = create;
        this.delay = delay;
        this.ttr = ttr;
        this.body = body;
    }

    private String body;

    public Job() {
    }

    public long getTtr() {
        return ttr;
    }

    public void setTtr(long ttr) {
        this.ttr = ttr;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
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
