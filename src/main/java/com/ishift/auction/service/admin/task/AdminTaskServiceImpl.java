package com.ishift.auction.service.admin.task;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.ishift.auction.util.SessionUtill;

@Service
@SuppressWarnings("unchecked")
public class AdminTaskServiceImpl implements AdminTaskService {
	
	@Autowired
	private SessionUtill sessionUtill;
	
	@Autowired
	private AdminTaskDAO adminTaskDAO;
	
	@Value("${ncloud.storage.end-point}") private String endPoint;
	@Value("${ncloud.storage.region-name}") private String regionName;
	@Value("${ncloud.storage.access-key}") private String accessKey;
	@Value("${ncloud.storage.secret-key}") private String secretKey;
	@Value("${ncloud.storage.bucket-name}") private String bucketName;

	@Override
	public Map<String, Object> selectSogCowInfo(Map<String, Object> params) throws SQLException {
		return adminTaskDAO.selectSogCowInfo(params);
	}

	@Override
	public Map<String, Object> uploadImageProc(Map<String, Object> params) throws SQLException {
		final Map<String, Object> result = new HashMap<String, Object>();
		
		// S3 client
		final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
												 .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
												 .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
												 .build();
		
		// ACL 설정 : 파일마다 읽기 권한을 설정
		final AccessControlList accessControlList = new AccessControlList();
		accessControlList.grantPermission(GroupGrantee.AllUsers, Permission.Read);
		
		// CORS 설정 : 이미지 업로드 페이지에서 이미지 url로 fetch 후 canvas 형태로 append 하는 형식이기 때문에 CORS 세팅이 필요
		final List<CORSRule.AllowedMethods> methodRule = Arrays.asList(CORSRule.AllowedMethods.PUT, CORSRule.AllowedMethods.GET, CORSRule.AllowedMethods.POST);
		final CORSRule rule = new CORSRule().withId("CORSRule")
											.withAllowedMethods(methodRule)
											.withAllowedHeaders(Arrays.asList(new String[] { "*" }))
											.withAllowedOrigins(Arrays.asList(new String[] { "*" }))
											.withMaxAgeSeconds(3000);

		final List<CORSRule> rules = Arrays.asList(rule);

		s3.setBucketCrossOriginConfiguration(bucketName, new BucketCrossOriginConfiguration().withRules(rules));

		final String naBzplc = params.getOrDefault("naBzplc", sessionUtill.getNaBzplc()).toString();
		final String aucDt = params.get("aucDt").toString();
		final String sraIndvAmnno = params.get("sraIndvAmnno").toString();
		final String filePath = naBzplc + "/" + aucDt + "/" + sraIndvAmnno + "/";
		final String fileExtNm = ".png";
		
		// 해당 path에 있는 이미지 삭제 : 이미지 저장에 실패했을때 transaction은 어떤 방식으로 처리할 지....
		for (S3ObjectSummary file : s3.listObjects(bucketName, filePath).getObjectSummaries()){
			s3.deleteObject(bucketName, file.getKey());
		}
		
		// 이미지 정보 삭제
		adminTaskDAO.deleteCowImg(params);
		
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

					// 썸네일 파일이 없는 경우 or 값이 data:image로 시작하지 않는 경우 pass
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
		result.put("message", "이미지 저장에 성공했습니다.");
//		result.put("message", String.format("전체 : %d, 성공 : %d", files.size(), successCnt));
		return result;
	}

	@Override
	public List<Map<String, Object>> selectCowImg(Map<String, Object> params) throws SQLException {
		return adminTaskDAO.selectCowImg(params);
	}

}
