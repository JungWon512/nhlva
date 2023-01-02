package com.ishift.auction.service.admin.task;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.BucketCrossOriginConfiguration;
import com.amazonaws.services.s3.model.CORSRule;
import com.amazonaws.services.s3.model.DeleteBucketRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.ishift.auction.util.SessionUtill;

import io.swagger.v3.oas.models.parameters.RequestBody;

@Service
@SuppressWarnings("unchecked")
public class AdminTaskServiceImpl implements AdminTaskService {
	
	@Autowired
	private SessionUtill sessionUtill;
	
	@Autowired
	private AdminTaskDAO adminTaskDAO;

	@Override
	public Map<String, Object> selectSogCowInfo(Map<String, Object> params) throws SQLException {
		return adminTaskDAO.selectSogCowInfo(params);
	}

	@Override
	public Map<String, Object> uploadImageProc(Map<String, Object> params) throws SQLException {
		final Map<String, Object> result = new HashMap<String, Object>();
		
		final String endPoint = "https://kr.object.ncloudstorage.com";
		final String regionName = "kr-standard";
		final String accessKey = "loqHvgq2A4WGx0D7feer";
		final String secretKey = "yrmIJmsF37g1BExQXk5CIhrMn1EG1h32qJyaTvzF";
		final String bucketName = "test-tt12";
		
		// S3 client
		final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
												 .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
												 .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
												 .build();
		
		AccessControlList accessControlList = new AccessControlList();
		accessControlList.grantPermission(GroupGrantee.AllUsers, Permission.Read);
		
		List<CORSRule.AllowedMethods> methodRule = new ArrayList<CORSRule.AllowedMethods>();
		methodRule.add(CORSRule.AllowedMethods.PUT);
		methodRule.add(CORSRule.AllowedMethods.GET);
		methodRule.add(CORSRule.AllowedMethods.POST);
		CORSRule rule = new CORSRule().withId("CORSRule")
										.withAllowedMethods(methodRule)
										.withAllowedHeaders(Arrays.asList(new String[] { "*" }))
										.withAllowedOrigins(Arrays.asList(new String[] { "*" }))
										.withMaxAgeSeconds(3000);

		List<CORSRule> rules = new ArrayList<CORSRule>();
		rules.add(rule);

		BucketCrossOriginConfiguration configuration = new BucketCrossOriginConfiguration();
		configuration.setRules(rules);

		s3.setBucketCrossOriginConfiguration(bucketName, configuration);

		final String naBzplc = params.getOrDefault("naBzplc", sessionUtill.getNaBzplc()).toString();
		final String aucDt = params.get("aucDt").toString();
		final String sraIndvAmnno = params.get("sraIndvAmnno").toString();
		final String filePath = naBzplc + "/" + aucDt + "/" + sraIndvAmnno + "/";
		final String fileExtNm = ".png";
		
		adminTaskDAO.deleteCowImg(params);
		
		for (S3ObjectSummary file : s3.listObjects(bucketName, filePath).getObjectSummaries()){
			s3.deleteObject(bucketName, file.getKey());
		}
		
		List<Map<String, Object>> files = (List<Map<String, Object>>)params.get("files");
		int successCnt = 0;
		int imgSqno = 1;
		if (!ObjectUtils.isEmpty(files)) {
			for (Map<String, Object> file : files) {

				boolean isSuccess = true;
				String fileNm = "";

				if ("image".equals(file.get("type"))) {
					fileNm = file.get("fileNm").toString();
					successCnt++;
				}
				else {
					// origin 파일이 없는 경우 or 값이 data:image로 시작하지 않는 경우 pass
					if (ObjectUtils.isEmpty(file.get("origin"))
					|| !file.getOrDefault("origin", ",").toString().startsWith("data:image")) continue;

					fileNm = UUID.randomUUID().toString();

					String origin = file.getOrDefault("origin", ",").toString();
					String[] oriBase64Arr = origin.split(",");
					byte[] oriImgByte = Base64.decodeBase64(oriBase64Arr[1]);
					InputStream oriBis = new ByteArrayInputStream(oriImgByte);

					ObjectMetadata oriObjectMetadata = new ObjectMetadata();
					oriObjectMetadata.setContentLength(oriImgByte.length);
					oriObjectMetadata.setContentType(MediaType.IMAGE_PNG_VALUE);
					PutObjectRequest oriPutObjectRequest = new PutObjectRequest(bucketName, filePath + fileNm + fileExtNm, oriBis, oriObjectMetadata);

					try {
						oriPutObjectRequest.setAccessControlList(accessControlList);
						s3.putObject(oriPutObjectRequest);
					}
					catch (AmazonS3Exception e) {
						e.printStackTrace();
						isSuccess = false;
					}
					catch(SdkClientException e) {
						e.printStackTrace();
						isSuccess = false;
					}
					
					if (!isSuccess) continue;
					successCnt++;

					// origin 파일이 없는 경우 or 값이 data:image로 시작하지 않는 경우 pass
					if (ObjectUtils.isEmpty(file.get("thumbnail"))
					|| !file.getOrDefault("thumbnail", ",").toString().startsWith("data:image")) continue;
					
					String thumbnail = file.getOrDefault("thumbnail", ",").toString();
					String[] thumbBase64Arr = thumbnail.split(",");
					byte[] thumbImgByte = Base64.decodeBase64(thumbBase64Arr[1]);
					InputStream thumbBis = new ByteArrayInputStream(thumbImgByte);
					
					ObjectMetadata thumbObjectMetadata = new ObjectMetadata();
					thumbObjectMetadata.setContentLength(thumbImgByte.length);
					thumbObjectMetadata.setContentType(MediaType.IMAGE_PNG_VALUE);
					PutObjectRequest thumbPutObjectRequest = new PutObjectRequest(bucketName, filePath + "thumb/" + fileNm + fileExtNm, thumbBis, thumbObjectMetadata);
					
					try {
						thumbPutObjectRequest.setAccessControlList(accessControlList);
						s3.putObject(thumbPutObjectRequest);
					}
					catch (AmazonS3Exception e) {
						e.printStackTrace();
					}
					catch(SdkClientException e) {
						e.printStackTrace();
					}
				}

				params.put("imgSqno", imgSqno);
				params.put("filePath", filePath);
				params.put("fileNm", fileNm);
				params.put("fileExtNm", fileExtNm);
				adminTaskDAO.insertCowImg(params);
				imgSqno++;
			}
		}
		
		result.put("success", (files.size() == successCnt));
		result.put("message", String.format("전체 : %d, 성공 : %d", files.size(), successCnt));
		return result;
	}

	@Override
	public List<Map<String, Object>> selectCowImg(Map<String, Object> params) throws SQLException {
		return adminTaskDAO.selectCowImg(params);
	}

}
