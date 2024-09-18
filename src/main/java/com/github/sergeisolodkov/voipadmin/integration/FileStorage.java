package com.github.sergeisolodkov.voipadmin.integration;

import org.springframework.core.io.Resource;

import java.io.InputStream;

/**
 * File storage adapter.
 */
public interface FileStorage {

    /**
     * Upload file to storage.
     * @param bucket bucket name.
     * @param data file.
     * @return path to file in bucket.
     */
    String upload(String bucket, InputStream data);

    /**
     * Download file from storage.
     * @param bucket bucket name.
     * @param path path to file in bucket.
     * @return {@link Resource}
     */
    Resource download(String bucket, String path);

    /**
     * Delete file.
     * @param bucket bucket name.
     * @param path path to file in bucket.
     */
    void delete(String bucket, String path);

    /**
     * Create bucket if not exists.
     * @param name bucket name.
     */
    void makeBucket(String name);
}
