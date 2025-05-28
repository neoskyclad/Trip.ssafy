package com.ssafy.tripssafy.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class S3Service {

	private final S3Client s3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;

	public String uploadFile(MultipartFile file) throws IOException {
		String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

		PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketName).key(fileName)
				.contentType(file.getContentType()).build();

		s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

		return "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
	}

	public void deleteFile(String fileUrl) {
	    // URL에서 파일 이름 추출
	    String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);

	    s3Client.deleteObject(builder -> 
	        builder.bucket(bucketName).key(fileName).build()
	    );
	}
}
