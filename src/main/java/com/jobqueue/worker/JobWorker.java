package com.jobqueue.worker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobqueue.model.Job;
import com.jobqueue.model.JobStatus;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobWorker {
    private final JedisPool jedisPool;
    private final ObjectMapper mapper;
    private final Map<String, JobHandler> jobHandlers;
    private volatile boolean running = true;

    public JobWorker(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
        this.mapper = new ObjectMapper();
        this.jobHandlers = new HashMap<>();
    }

    public void registerHandler(String jobType, JobHandler handler) {
        jobHandlers.put(jobType, handler);
    }

    public void start(){
        System.out.println("[WORKER] Starting job worker...");

        while (running) {
            try (Jedis jedis = jedisPool.getResource()) {
                List<String> jobData = jedis.blpop(5, "queue:pending");
                if (jobData == null || jobData.isEmpty()) {
                    continue; // No job available, continue to next iteration
                }

                String jobId = jobData.get(1);
                String jobJson = jedis.get("job:" + jobId);
                if (jobJson == null) {
                    System.err.println("[WORKER] Job data not found for job ID: " + jobId);
                    continue;
                }

                Job job = mapper.readValue(jobJson, Job.class);
                JobHandler handler = jobHandlers.get(job.getType());
                if (handler == null) {
                    System.err.println("[WORKER] No handler registered for job type: " + job.getType());
                    continue;
                }

                job.setStatus(JobStatus.PROCESSING);
                jedis.set("job:" + job.getId(), mapper.writeValueAsString(job));

                boolean success = handler.handle(job);
                if (success) {
                    jedis.del("job:" + jobId);
                    System.out.println("[WORKER] Completed and deleted job: " + jobId);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("[WORKER] Error processing job: " + e.getMessage());
            }

            }
        }
        public void stop() {
            running = false;
            System.out.println("[WORKER] Stopping job worker...");
    }


}
