package com.sud.s3_operaions.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.s3.model.Bucket;
import com.sud.s3_operaions.model.BucketObjectRepresentaion;
import com.sud.s3_operaions.service.S3Services;

@RestController
@RequestMapping(value = "/buckets/")
public class S3OperationsController {

	@Autowired
	private S3Services s3Service;

	@PostMapping(value = "/{bucketName}")
	public void createBucket(@PathVariable String bucketName) {
		s3Service.createS3Bucket(bucketName);
	}

	@GetMapping
	public List<String> listBuckets() {
		List<Bucket> buckets = s3Service.listBuckets();
		List<String> names = buckets.stream().map(Bucket::getName).collect(Collectors.toList());
		return names;
	}

	@DeleteMapping(value = "/{bucketName}")
	public void deleteBucket(@PathVariable String bucketName) {
		s3Service.deleteBucket(bucketName);
	}

	@PostMapping(value = "/{bucketName}/objects")
	public void createObject(@PathVariable String bucketName, @RequestBody BucketObjectRepresentaion representaion)
			throws IOException {
		s3Service.putObject(bucketName, representaion);
	}

	@PatchMapping(value = "/{bucketName}/objects/{objectName}/{bucketSource}")
	public void moveObject(@PathVariable String bucketName, @PathVariable String objectName,
			@PathVariable String bucketSource) throws IOException {
		s3Service.moveObject(bucketName, objectName, bucketSource);
	}
}
