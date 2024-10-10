package com.github.sergeisolodkov.voipadmin.integration.impl;

import com.github.sergeisolodkov.voipadmin.config.properties.MinioFileStorageProperties;
import com.github.sergeisolodkov.voipadmin.integration.FileProvider;
import com.github.sergeisolodkov.voipadmin.integration.domain.StorageCatalog;
import com.github.sergeisolodkov.voipadmin.integration.domain.StorageType;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Component
@RequiredArgsConstructor
public class MinioFileProviderImpl implements FileProvider {

    private final MinioFileStorageProperties minioFileStorageProperties;
    private final MinioClient minioClient;

    @PostConstruct
    void init() {
        minioFileStorageProperties.getBuckets().forEach((key, value) -> {
            try {
                makeBucket(value);
            } catch (IOException e) {
                log.error("Error while trying to make bucket {}", value);
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public StorageType getType() {
        return StorageType.S3;
    }

    @Override
    public String put(StorageCatalog catalog, Path path, Resource data) throws IOException {
        var bucket = getBucket(catalog);
        try {
            var args = PutObjectArgs
                .builder()
                .bucket(bucket)
                .stream(data.getInputStream(), data.contentLength(), 0L)
                .object(path.toString())
                .build();
            var response = minioClient.putObject(args);

            return Paths.get(response.object()).toString();
        } catch (IOException ex) {
            log.error("Error while trying to put file in bucket {}", bucket);
            throw ex;
        } catch (Exception ex) {
            var message = String.format("Error while trying to upload file in bucket '%s'", bucket);
            log.error(message);
            throw new RuntimeException(message, ex);
        }
    }

    @Override
    public Resource get(StorageCatalog catalog, Path path) throws IOException {
        var bucket = getBucket(catalog);
        try (InputStream file = minioClient.getObject(
            GetObjectArgs.builder()
                .bucket(bucket)
                .object(path.toString())
                .build()
        )) {
            return new ByteArrayResource(file.readAllBytes()) {
                @Override
                public String getFilename() {
                    return path.getFileName().toString();
                }
            };
        } catch (IOException ex) {
            log.error("Error while trying to get file from bucket {}", bucket);
            throw ex;
        } catch (Exception ex) {
            var message = String.format("Error while downloading file %s", path);
            log.error(message);
            throw new RuntimeException(message, ex);
        }
    }

    @Override
    public void delete(StorageCatalog catalog, Path path) throws IOException {
        var bucket = getBucket(catalog);
        try {
            var args = RemoveObjectArgs
                .builder()
                .bucket(bucket)
                .object(path.toString())
                .build();
            minioClient.removeObject(args);
        } catch (IOException ex) {
            log.error("Error while trying to delete file from bucket {}", bucket);
            throw ex;
        } catch (Exception ex) {
            var message = String.format("Error while removing file %s", path);
            log.error(message);
            throw new RuntimeException(message, ex);
        }
    }


    public void makeBucket(String name) throws IOException {
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
        } catch (IOException ex) {
            log.error("Error while trying to make bucket with name {}", name);
            throw ex;
        }  catch (Exception ex) {
            var message = String.format("Error while trying to make bucket with name %s", name);
            log.error(message);
            throw new RuntimeException(message, ex);
        }
    }

    private String getBucket(StorageCatalog catalog) {
        return minioFileStorageProperties.getBuckets().get(catalog);
    }
}
