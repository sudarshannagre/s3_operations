package com.sud.s3_operaions.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AwsS3Config {

	BasicAWSCredentials basicAwsCredentials = new BasicAWSCredentials("AccessKey",
			"SecretAccessKey");
	
	@Bean
	public AmazonS3 createS3Client() {
		return AmazonS3ClientBuilder.standard().withRegion("region")
				.withCredentials(new AWSStaticCredentialsProvider(basicAwsCredentials)).build();
	}
}
