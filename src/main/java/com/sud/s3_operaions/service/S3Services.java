package com.sud.s3_operaions.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sud.s3_operaions.model.BucketObjectRepresentaion;

@Service
public class S3Services {

	@Autowired
	private AmazonS3 amazonS3Client;
	
	private static final Logger log = LoggerFactory.getLogger(S3Services.class);
	
	@SuppressWarnings("deprecation")
	public void createS3Bucket(String bucketName) {
	    if(amazonS3Client.doesBucketExist(bucketName)) {
	        log.info("Bucket name already in use. Try another name.");
	        return;
	    }
	    amazonS3Client.createBucket(bucketName);
	}
	
	public List<Bucket> listBuckets(){
	    return amazonS3Client.listBuckets();
	}
	
	public void deleteBucket(String bucketName){
	    try {
	        amazonS3Client.deleteBucket(bucketName);
	    } catch (AmazonServiceException e) {
	        log.error(e.getErrorMessage());
	        return;
	    }
	}
	
	public void putObject(String bucketName, BucketObjectRepresentaion representation) throws IOException {

	    String objectName = representation.getObjectName();
	    String objectValue = representation.getText();

	    File file = new File("." + File.separator + objectName);
	    FileWriter fileWriter = new FileWriter(file, false);
	    PrintWriter printWriter = new PrintWriter(fileWriter);
	    printWriter.println(objectValue);
	    printWriter.flush();
	    printWriter.close();

	    try {
	    	PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, file);
	        amazonS3Client.putObject(putObjectRequest);
	    } catch (Exception e){
	        log.error("Some error has ocurred.");
	    }

	}
	
	public void deleteObject(String bucketName, String objectName){
	    amazonS3Client.deleteObject(bucketName, objectName);
	}
	
	public void deleteMultipleObjects(String bucketName, List<String> objects){
	    DeleteObjectsRequest delObjectsRequests = new DeleteObjectsRequest(bucketName)
	            .withKeys(objects.toArray(new String[0]));
	    amazonS3Client.deleteObjects(delObjectsRequests);
	}
	
	public void moveObject(String bucketSourceName, String objectName, String bucketTargetName){
	    amazonS3Client.copyObject(
	            bucketSourceName,
	            objectName,
	            bucketTargetName,
	            objectName
	    );
	}
}
