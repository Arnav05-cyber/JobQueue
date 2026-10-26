package com.jobqueue.worker;

import com.jobqueue.model.Job;

public class EmailHandler implements JobHandler {
    @Override
    public boolean handle(Job job) throws Exception {
        System.out.println("[WORKER] Processing job " + job.getId() + " (" + job.getType() + ")");
        System.out.println("[WORKER] Sending email with payload: " + job.getPayload());
        Thread.sleep(5000);
        System.out.println("[WORKER] Email sent successfully for job " + job.getId());

        return true;
    }
}
