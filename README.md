# AWS-Assingment-Nimesa-Technologies

**Introduction:**

This document outlines the Spring Boot project that exposes REST APIs to manage AWS EC2 instances and S3 buckets asynchronously. The APIs allow discovering services, retrieving job status, fetching discovery results, and extracting file information from S3 buckets.



**Test Endpoints:**
Used Postman to send requests to your application's REST endpoints.

Tested each endpoint to ensure they behave as expected

**1)Discover Services**

**Description:** Initiates asynchronous discovery of EC2 instances and S3 buckets in the Mumbai region.

**Endpoint:** POST http://localhost:8080/api/discoverServices
**Body:** JSON array of services (["EC2", "S3"])
**Response:** Job ID
Get Job Result

HTTP Status Codes:
200 OK: Successfully initiated discovery.
400 Bad Request: Invalid request payload.
500 Internal Server Error: Server error occurred.

**2. GetJobResult**
**Description**: Retrieves the status of a job identified by jobId.

**Endpoint**: GET http://localhost:8080/api/getJobResult/{jobId}
Replace {jobId} with a valid Job ID.
**Response**: Job status (e.g., Success, In Progress, Failed)
Get Discovery Result

**HTTP Status Codes:**
200 OK: Successful retrieval.
404 Not Found: Job ID not found.
500 Internal Server Error: Server error occurred.

**3. GetDiscoveryResult**
**Description**: Retrieves discovery results based on the service name (S3 or EC2)

**Endpoint**: GET http://localhost:8080/api/getDiscoveryResult/{service}
Replace {service} with EC2 or S3.
**Response**: List of EC2 instance IDs or S3 bucket names.
Get S3 Bucket Objects

**HTTP Status Codes:**
200 OK: Successful retrieval.
400 Bad Request: Invalid service name.
404 Not Found: No results found for the service.
500 Internal Server Error: Server error occurred.


**4. GetS3BucketObjects**

**Description**: Initiates discovery of file names in a specified S3 bucket.

**Endpoint**: POST http://localhost:8080/api/getS3BucketObjects
**Body**: Form data or query parameter (bucketName)
**Response**: Job ID
Get S3 Bucket Object Count

**HTTP Status Codes:**
200 OK: Successfully initiated discovery.
400 Bad Request: Invalid request payload.
500 Internal Server Error: Server error occurred.

**5. GetS3BucketObjectCount**

**Description**: Retrieves the count of files in a specified S3 bucket.

**Endpoint**: GET http://localhost:8080/api/getS3BucketObjectCount/{bucketName}
Replace {bucketName} with a valid S3 bucket name.
**Response**: Count of objects in the S3 bucket.
Get S3 Bucket Object Like

HTTP Status Codes:
200 OK: Successful retrieval.
404 Not Found: Bucket not found.
500 Internal Server Error: Server error occurred.

6. GetS3BucketObjectLike

**Description**: Retrieves file names in a specified S3 bucket matching a pattern

**Endpoint**: GET http://localhost:8080/api/getS3BucketObjectlike
Query Parameters: bucketName and pattern
**Response**: List of file names matching the pattern in the specified S3 bucket.

**HTTP Status Codes:**
200 OK: Successful retrieval.
404 Not Found: Bucket not found or no files matching the pattern.
500 Internal Server Error: Server error occurred.
