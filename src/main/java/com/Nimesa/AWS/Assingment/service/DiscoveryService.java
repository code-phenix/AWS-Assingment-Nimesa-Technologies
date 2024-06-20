package com.Nimesa.AWS.Assingment.service;


import com.Nimesa.AWS.Assingment.model.EC2Instance;
import com.Nimesa.AWS.Assingment.model.Job;
import com.Nimesa.AWS.Assingment.model.S3Bucket;
import com.Nimesa.AWS.Assingment.model.S3BucketObject;
import com.Nimesa.AWS.Assingment.repository.EC2InstanceRepository;
import com.Nimesa.AWS.Assingment.repository.JobRepository;
import com.Nimesa.AWS.Assingment.repository.S3BucketObjectRepository;
import com.Nimesa.AWS.Assingment.repository.S3BucketRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesResponse;
import software.amazon.awssdk.services.ec2.model.Filter;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;


import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class DiscoveryService {
    @Value("${aws.accessKey}")
    private String awsAccessKey;

    @Value("${aws.secretKey}")
    private String awsSecretKey;

    @Value("${aws.region}")
    private String awsRegion;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private EC2InstanceRepository ec2InstanceRepository;

    @Autowired
    private S3BucketRepository s3BucketRepository;

    @Autowired
    private S3BucketObjectRepository s3BucketObjectRepository;

    private Ec2Client ec2Client;
    private S3Client s3Client;

    @PostConstruct
    private void init() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(awsAccessKey, awsSecretKey);
        ec2Client = Ec2Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .region(Region.of(awsRegion))
                .build();

        s3Client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .region(Region.of(awsRegion))
                .build();
    }

    @Async
    public CompletableFuture<Void> discoverEC2Instances(Long jobId) {
        DescribeInstancesRequest request = DescribeInstancesRequest.builder()
                .filters(Filter.builder()
                        .name("availability-zone")
                        .values("ap-south-1") // Mumbai region
                        .build())
                .build();

        DescribeInstancesResponse response = ec2Client.describeInstances(request);

        List<String> instanceIds = response.reservations().stream()
                .flatMap(r -> r.instances().stream())
                .map(i -> i.instanceId())
                .collect(Collectors.toList());

        for (String instanceId : instanceIds) {
            EC2Instance instance = new EC2Instance();
            instance.setInstanceId(instanceId);
            instance.setJobId(jobId);
            ec2InstanceRepository.save(instance);
        }

        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Void> discoverS3Buckets(Long jobId) {
        ListBucketsResponse response = s3Client.listBuckets();

        for (Bucket bucket : response.buckets()) {
            S3Bucket s3Bucket = new S3Bucket();
            s3Bucket.setBucketName(bucket.name());
            s3Bucket.setJobId(jobId);
            s3BucketRepository.save(s3Bucket);
        }

        return CompletableFuture.completedFuture(null);
    }

    public Job createJob(String service) {
        Job job = new Job();
        job.setService(service);
        job.setStatus("In Progress");
        job.setCreatedDate(new java.util.Date());
        return jobRepository.save(job);
    }

    public Optional<Job> getJob(Long jobId) {
        return jobRepository.findById(jobId);
    }

    public List<Job> getAllEC2Jobs() {
        return jobRepository.findAll();
    }

    public List<EC2Instance> getEC2InstancesByJobId(Long jobId) {
        return ec2InstanceRepository.findByJobId(jobId);
    }

    public List<EC2Instance> getAllEC2Instance(){
        return ec2InstanceRepository.findAll();
    }

    public List<S3BucketObject> getAllS3Bucket(){
        return s3BucketObjectRepository.findAll();
    }


    public List<S3Bucket> getS3BucketsByJobId(Long jobId) {
        return s3BucketRepository.findByJobId(jobId);
    }

    @Async
    public CompletableFuture<Void> discoverS3BucketObjects(String bucketName, Long jobId) {
        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();

        ListObjectsV2Response response = s3Client.listObjectsV2(request);

        for (S3Object s3Object : response.contents()) {
            S3BucketObject object = new S3BucketObject();
            object.setBucketName(bucketName);
            object.setFileName(s3Object.key());
            s3BucketObjectRepository.save(object);
        }

        return CompletableFuture.completedFuture(null);
    }

    public List<S3BucketObject> getS3BucketObjects(String bucketName) {
        return s3BucketObjectRepository.findByBucketName(bucketName);
    }

    public long getS3BucketObjectCount(String bucketName) {
        return s3BucketObjectRepository.findByBucketName(bucketName).size();
    }

    public List<S3BucketObject> getS3BucketObjectsLike(String bucketName, String pattern) {
        return s3BucketObjectRepository.findByBucketNameAndFileNameLike(bucketName, "%" + pattern + "%");
    }
}