package com.github.sergeisolodkov.voipadmin.integration;

import org.springframework.core.io.Resource;

/**
 * File storage adapter.
 */
public interface FileStorage {

    /**
     * Upload file to storage.
     * @param bucket bucket path.
     * @param path path to file within bucket.
     * @param data file.
     * @return path to file in bucket.
     */
    String upload(String bucket, String path, Resource data);

    /**
     * Download file from storage.
     * @param bucket bucket name.
     * @param path path to file within bucket.
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
