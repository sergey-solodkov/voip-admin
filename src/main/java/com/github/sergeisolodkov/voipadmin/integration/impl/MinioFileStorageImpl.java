package com.github.sergeisolodkov.voipadmin.integration.impl;

import com.github.sergeisolodkov.voipadmin.integration.FileStorage;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.file.Paths;

@Component
public class MinioFileStorageImpl implements FileStorage {

    private static final Logger LOG = LoggerFactory.getLogger(MinioFileStorageImpl.class);

    private final MinioClient minioClient;

    public MinioFileStorageImpl(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public String upload(String bucket, InputStream data) {
        try {
            var args = PutObjectArgs
                .builder()
                .bucket(bucket)
                .stream(data, data.available(), 0L)
                .build();
            var response = minioClient.putObject(args);

            return Paths.get(response.object()).toString();
        } catch (Exception ex) {
            var message = String.format("Error while trying to upload file in bucket '%s'", bucket);
            LOG.error(message);
            throw new RuntimeException(message, ex);
        }
    }

    @Override
    public Resource download(String bucket, String path) {
        try (InputStream file = minioClient.getObject(
            GetObjectArgs.builder()
                .bucket(bucket)
                .object(path)
                .build()
        )) {
            return new ByteArrayResource(file.readAllBytes());
        } catch (Exception ex) {
            var message = String.format("Error while downloading file %s/%s", bucket, path);
            LOG.error(message);
            throw new RuntimeException(message, ex);
        }
    }

    @Override
    public void delete(String bucket, String path) {
        try {
            var args = RemoveObjectArgs
                .builder()
                .bucket(bucket)
                .object(path)
                .build();
            minioClient.removeObject(args);
        } catch (Exception ex) {
            var message = String.format("Error while removing file %s/%s", bucket, path);
            LOG.error(message);
            throw new RuntimeException(message, ex);
        }
    }

    @Override
    public void makeBucket(String name) {
        try {
            var bucketExistsArgs = BucketExistsArgs
                .builder()
                .bucket(name)
                .build();
            if (minioClient.bucketExists(bucketExistsArgs)) {
                return;
            }

            var makeBucketArgs = MakeBucketArgs.builder()
                .bucket(name)
                .build();
            minioClient.makeBucket(makeBucketArgs);
        } catch (Exception ex) {
            var message = String.format("Error while trying to make bucket with name %s", name);
            LOG.error(message);
            throw new RuntimeException(message, ex);
        }
    }
}
