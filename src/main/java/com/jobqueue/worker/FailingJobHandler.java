package com.jobqueue.worker;

import com.jobqueue.model.Job;

public class FailingJobHandler implements JobHandler {
    @Override
    public boolean handle(Job job) throws Exception {
        System.out.println("[WORKER] Executing failing job: " + job.getId() + " (Attempt " + (job.getAttempts() + 1) + ")");
        return false; // Force failure
    }
}