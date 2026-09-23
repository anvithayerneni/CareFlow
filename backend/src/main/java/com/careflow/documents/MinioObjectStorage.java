package com.careflow.documents;
import io.minio.*;
import io.minio.errors.ErrorResponseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.InputStream;
@Service
public class MinioObjectStorage implements ObjectStorage {
 private final MinioClient client; private final String bucket;
 public MinioObjectStorage(@Value("${careflow.storage.endpoint}") String endpoint,@Value("${careflow.storage.access-key}") String access,@Value("${careflow.storage.secret-key}") String secret,@Value("${careflow.storage.region}") String region,@Value("${careflow.storage.bucket}") String bucket){this.client=MinioClient.builder().endpoint(endpoint).credentials(access,secret).region(region).build();this.bucket=bucket;}
 private void ensureBucket()throws Exception{if(!client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build()))client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());}
 @Override public void put(String key,InputStream content,long size,String contentType){try{ensureBucket();client.putObject(PutObjectArgs.builder().bucket(bucket).object(key).stream(content,size,-1).contentType(contentType).build());}catch(Exception e){throw new IllegalStateException("Object storage upload failed",e);}}
 @Override public byte[] get(String key){try(InputStream stream=client.getObject(GetObjectArgs.builder().bucket(bucket).object(key).build())){return stream.readAllBytes();}catch(Exception e){throw new IllegalStateException("Object storage read failed",e);}}
 @Override public void delete(String key){try{client.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(key).build());}catch(Exception e){throw new IllegalStateException("Object storage delete failed",e);}}
}
