package com.careflow.documents;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;
import java.net.URI;

@Service
public class MinioObjectStorage implements ObjectStorage {
    private final S3Client client;
    private final String bucket;

    public MinioObjectStorage(@Value("${careflow.storage.endpoint}") String endpoint,
                             @Value("${careflow.storage.access-key}") String access,
                             @Value("${careflow.storage.secret-key}") String secret,
                             @Value("${careflow.storage.region}") String region,
                             @Value("${careflow.storage.bucket}") String bucket) {
        this.client = S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(access, secret)))
                .region(Region.of(region))
                .forcePathStyle(true)
                .build();
        this.bucket = bucket;
    }

    @Override
    public void put(String key, InputStream content, long size, String contentType) {
        try {
            var request = PutObjectRequest.builder().bucket(bucket).key(key).contentType(contentType).build();
            client.putObject(request, RequestBody.fromInputStream(content, size));
        } catch (Exception e) {
            throw new IllegalStateException("Object storage upload failed", e);
        }
    }

    @Override
    public byte[] get(String key) {
        try (ResponseInputStream<GetObjectResponse> stream = client.getObject(GetObjectRequest.builder().bucket(bucket).key(key).build())) {
            return stream.readAllBytes();
        } catch (Exception e) {
            throw new IllegalStateException("Object storage read failed", e);
        }
    }

    @Override
    public void delete(String key) {
        try {
            client.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(key).build());
        } catch (Exception e) {
            throw new IllegalStateException("Object storage delete failed", e);
        }
    }
}
