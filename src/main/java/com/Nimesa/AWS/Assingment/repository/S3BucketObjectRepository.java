package com.Nimesa.AWS.Assingment.repository;

import com.Nimesa.AWS.Assingment.model.S3BucketObject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface S3BucketObjectRepository extends JpaRepository<S3BucketObject, Long> {
    List<S3BucketObject> findByBucketName(String bucketName);
    List<S3BucketObject> findByBucketNameAndFileNameLike(String bucketName, String pattern);
}