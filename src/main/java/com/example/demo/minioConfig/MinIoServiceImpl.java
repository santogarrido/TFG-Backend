package com.example.demo.minioConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.MinIoService;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;

@Service("minIoService")
public class MinIoServiceImpl implements MinIoService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String facilityBucket;

    @Value("${minio.url}")
    private String minioUrl;

    public MinIoServiceImpl(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public String uploadImage(MultipartFile file) {

        try {

            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            if (!file.getContentType().startsWith("image/")) {
                throw new RuntimeException("Only image files are allowed");
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(facilityBucket)
                            .build()
            );

            if (!exists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(facilityBucket)
                                .build()
                );
            }

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(facilityBucket)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            return minioUrl + "/" + facilityBucket + "/" + fileName;

        } catch (Exception e) {
            throw new RuntimeException("Error uploading image: " + e.getMessage());
        }
    }
}