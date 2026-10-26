package com.jobqueue;

import com.jobqueue.model.Job;
import com.jobqueue.producer.JobProducer;
import com.jobqueue.worker.EmailHandler;
import com.jobqueue.worker.FailingJobHandler;
import com.jobqueue.worker.JobWorker;
import redis.clients.jedis.JedisPool;

public class Main {
    public static void main(String[] args) {
        // 1. Initialize Redis Connection Pool
        try (JedisPool jedisPool = new JedisPool("localhost", 6379)) {

            // 2. Initialize Producer and Worker Engine
            JobProducer producer = new JobProducer(jedisPool);
            JobWorker worker = new JobWorker(jedisPool);

            // 3. Register Task Handlers
            worker.registerHandler("SEND_EMAIL", new EmailHandler());
            worker.registerHandler("FAIL_TASK", new FailingJobHandler());

            // 4. Enqueue a Successful Email Job (1 retry allowed)
            Job emailJob = new Job("SEND_EMAIL", "{\"to\": \"user@example.com\"}", 1, 1, 0);
            producer.enqueueJob(emailJob);

            // 5. Enqueue a Failing Job (3 retries allowed before DLQ)
            Job failingJob = new Job("FAIL_TASK", "{\"data\": \"test_failure\"}", 1, 3, 0);
            producer.enqueueJob(failingJob);

            // 6. Start Worker Execution Loop
            worker.start();
        }
    }
}