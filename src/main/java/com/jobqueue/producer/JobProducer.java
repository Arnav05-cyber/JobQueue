package com.jobqueue.producer;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobqueue.model.Job;
import redis.clients.jedis.JedisPool;

public class JobProducer {
    JedisPool jedisPool;
    public JobProducer(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    ObjectMapper mapper = new ObjectMapper();

    public void enqueueJob(Job job) {
        try (var jedis = jedisPool.getResource()) {
            String jobJson = mapper.writeValueAsString(job);
            jedis.set("job:" + job.getId(), jobJson);
            jedis.rpush("queue:pending", job.getId());
            System.out.println("[PRODUCER] Enqueued job " + job.getId() + " (" + job.getType() + ")");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
