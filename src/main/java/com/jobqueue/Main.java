package com.jobqueue;

import com.jobqueue.model.Job;
import com.jobqueue.producer.JobProducer;
import redis.clients.jedis.JedisPool;

public class Main {
    public static void main(String[] args) {

        try (JedisPool jedisPool = new JedisPool("localhost", 6379)) {
            JobProducer producer = new JobProducer(jedisPool);
            Job sampleJob = new Job("SEND_EMAIL", "{\"email\":\"user@example.com\"}", 1, 3, 0);
            producer.enqueueJob(sampleJob);
        }
    }
}