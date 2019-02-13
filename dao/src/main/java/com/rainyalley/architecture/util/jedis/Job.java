package com.rainyalley.architecture.util.jedis;

/**
 * @author bin.zhang
 */
public class Job {

    private String jobId;

    private String topic;


    /**
     * 创建时间
     */
    private long create = System.currentTimeMillis();

    /**
     * 延迟时间
     */
    private long delay;


    public Job(String topic, long delay, String body) {
        this.topic = topic;
        this.delay = delay;
        this.body = body;
    }

    private String body;

    public Job() {
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


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"@class\":\"com.rainyalley.architecture.util.jedis.Job\",");
        sb.append("\"@super\":\"").append(super.toString()).append("\",");
        sb.append("\"jobId\":\"")
                .append(jobId)
                .append("\"");
        sb.append(",\"topic\":\"")
                .append(topic)
                .append("\"");
        sb.append(",\"create\":\"")
                .append(create)
                .append("\"");
        sb.append(",\"delay\":\"")
                .append(delay)
                .append("\"");
        sb.append(",\"body\":\"")
                .append(body)
                .append("\"");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Job)) return false;

        Job job = (Job) o;

        return getJobId() != null ? getJobId().equals(job.getJobId()) : job.getJobId() == null;
    }

    @Override
    public int hashCode() {
        return getJobId() != null ? getJobId().hashCode() : 0;
    }
}
