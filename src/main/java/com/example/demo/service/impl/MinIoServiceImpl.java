package com.example.demo.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.MinIoService;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;

@Service("minIoService")
public class MinIoServiceImpl implements MinIoService{

	private MinioClient minioClient;
	
	private final String facilityBucket = "facility";

	@Override
	public String uploadImage(MultipartFile file) {
		
		try {
			String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
			
			boolean exists = minioClient.bucketExists(
						BucketExistsArgs.builder().bucket(facilityBucket).build()
					);
			
			if(!exists) {
				minioClient.makeBucket(
					MakeBucketArgs.builder().bucket(facilityBucket).build()
				);
			}
			
			//Upload file
			minioClient.putObject(
				PutObjectArgs.builder()
					.bucket(facilityBucket)
					.object(fileName)
					.stream(file.getInputStream(), file.getSize(), -1)
					.contentType(file.getContentType())
					.build()
			);
			
			return "http://localhost:9000/" + facilityBucket + "/" + fileName;
			
		}catch (Exception e) {
			throw new RuntimeException("Error uploading image " + e);
		}
		
	}
	
	
	
}
