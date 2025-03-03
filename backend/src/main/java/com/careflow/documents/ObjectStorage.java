package com.careflow.documents;
import java.io.InputStream;
public interface ObjectStorage {
 void put(String key,InputStream content,long size,String contentType);
 byte[] get(String key);
 void delete(String key);
}
