package com.Nimesa.AWS.Assingment.controller;


import com.Nimesa.AWS.Assingment.model.Job;
import com.Nimesa.AWS.Assingment.model.S3BucketObject;
import com.Nimesa.AWS.Assingment.service.DiscoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
public class DiscoveryController {
    @Autowired
    private DiscoveryService discoveryService;

    @PostMapping("/discoverServices")
    public Long discoverServices(@RequestBody List<String> services) {
        Job job = discoveryService.createJob("EC2 and S3");
        Long jobId = job.getId();

        if (services.contains("EC2")) {
            CompletableFuture.runAsync(() -> discoveryService.discoverEC2Instances(jobId));
        }

        if (services.contains("S3")) {
            CompletableFuture.runAsync(() -> discoveryService.discoverS3Buckets(jobId));
        }

        return jobId;
    }

    @GetMapping("/getJobResult/{jobId}")
    public String getJobResult(@PathVariable Long jobId) {
        Optional<Job> jobOptional = discoveryService.getJob(jobId);
        if (jobOptional.isPresent()) {
            return jobOptional.get().getStatus();
        } else {
            return "Job not found";
        }
    }

    @GetMapping("/getDiscoveryResult/{service}")
    public List<?> getDiscoveryResult(@PathVariable String service) {
        if (service.equalsIgnoreCase("EC2")) {
            return discoveryService.getAllEC2Instance();
        } else if (service.equalsIgnoreCase("S3")) {
            return discoveryService.getAllS3Bucket();
        }
        return null;
    }

    @PostMapping("/getS3BucketObjects")
    public Long getS3BucketObjects(@RequestParam String bucketName) {
        Job job = discoveryService.createJob("S3 Bucket Objects Discovery");
        Long jobId = job.getId();

        CompletableFuture.runAsync(() -> discoveryService.discoverS3BucketObjects(bucketName, jobId));

        return jobId;
    }

    @GetMapping("/getS3BucketObjectCount/{bucketName}")
    public long getS3BucketObjectCount(@PathVariable String bucketName) {
        return discoveryService.getS3BucketObjectCount(bucketName);
    }

    @GetMapping("/getS3BucketObjectlike")
    public List<S3BucketObject> getS3BucketObjectlike(@RequestParam String bucketName, @RequestParam String pattern) {
        return discoveryService.getS3BucketObjectsLike(bucketName, pattern);
    }
}