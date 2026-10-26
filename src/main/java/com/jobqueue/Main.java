package com.jobqueue;

import com.jobqueue.model.Job;
import com.jobqueue.producer.JobProducer;
import com.jobqueue.worker.EmailHandler;
import com.jobqueue.worker.JobWorker;
import redis.clients.jedis.JedisPool;

public class Main {
    public static void main(String[] args) {
        try (JedisPool jedisPool = new JedisPool("localhost", 6379)) {

            // 1. Setup Producer
            JobProducer producer = new JobProducer(jedisPool);

            // 2. Setup Worker and register handlers
            JobWorker worker = new JobWorker(jedisPool);
            worker.registerHandler("SEND_EMAIL", new EmailHandler());

            // 3. Enqueue a job
            Job sampleJob = new Job("SEND_EMAIL", "{\"to\": \"user@example.com\"}", 1, 3, 0);
            producer.enqueueJob(sampleJob);

            // 4. Start the worker (this will block and continuously process jobs)
            worker.start();
        }
    }
}