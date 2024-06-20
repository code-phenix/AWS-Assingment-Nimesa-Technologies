package com.Nimesa.AWS.Assingment.repository;

import com.Nimesa.AWS.Assingment.model.S3Bucket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface S3BucketRepository extends JpaRepository<S3Bucket, Long> {
    List<S3Bucket> findByJobId(Long jobId);

    List<S3Bucket> findAll();
}