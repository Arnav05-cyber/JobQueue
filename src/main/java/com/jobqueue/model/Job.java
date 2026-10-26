package com.jobqueue.model;

import java.util.UUID;

public class Job {
    private String id;
    private String type;
    private String payload;
    private JobStatus status;
    private int priority;
    private int maxRetries;
    private int attempts;
    private long delay;
    private long createdAt;
    private long updatedAt;

    public Job(String type, String payload, int priority, int maxRetries, long delay) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.payload = payload;
        this.status = JobStatus.PENDING;
        this.priority = priority;
        this.maxRetries = maxRetries;
        this.attempts = 0;
        this.delay = delay;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public Job(){

    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getPayload() {
        return payload;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

}
