package com.jobqueue.worker;

import com.jobqueue.model.Job;

public interface JobHandler {
    boolean handle(Job job) throws Exception;
}
