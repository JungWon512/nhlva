package com.ishift.auction.service.admin.task;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.ssl.SSLContexts;
import org.checkerframework.checker.units.qual.s;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.util.ObjectUtils;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.SDKGlobalConfiguration;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.http.AmazonHttpClient;
import com.amazonaws.http.apache.client.impl.SdkHttpClient;
import com.amazonaws.http.conn.ssl.SdkTLSSocketFactory;
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
import com.ishift.auction.service.auction.AuctionService;
import com.ishift.auction.service.common.CommonDAO;
import com.ishift.auction.service.common.CommonService;
import com.ishift.auction.util.SessionUtill;
import com.ishift.auction.web.RestApiJsonController;

/**
 * 홈페이지 관리자 경매업무 서비스
 * @author iShift
 */
@Service
@Transactional
@SuppressWarnings({"unused", "unchecked"})
public class AdminTaskServiceImpl implements AdminTaskService {

	private static Logger log = LoggerFactory.getLogger(AdminTaskServiceImpl.class);
	
	@Autowired
	private AdminTaskDAO adminTaskDAO;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private SessionUtill sessionUtil;
	
	@Autowired
	private CommonDAO commonDAO;
	
	@Autowired
	private AuctionService auctionService;
	
	@Value("${ncloud.storage.end-point}") private String endPoint;
	@Value("${ncloud.storage.region-name}") private String regionName;
	@Value("${ncloud.storage.access-key}") private String accessKey;
	@Value("${ncloud.storage.secret-key}") private String secretKey;
	@Value("${ncloud.storage.bucket-name}") private String bucketName;

	/**
	 * 개체 정보를 조회한다.(한우종합)
	 * @param params
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> searchIndvAmnno(final Map<String, Object> params) throws SQLException, Exception {
		final Map<String, Object> result = new HashMap<String, Object>();
		
		// 1. 같은 경매일에 등록된 개체가 있는지 체크
		final List<Map<String, Object>> duplList = adminTaskDAO.selectDuplList(params);
		if (duplList != null && duplList.size() > 0) {
			result.put("success", false);
			result.put("type", "A");
			result.put("message", "출장우로 등록된 개체입니다.");
			return result;
		}
		
		// 2. 한우 종합에 등록된 개체 상세내역 조회 후 저장
		final Map<String, Object> indvMap = commonService.searchIndvDetails(params);
		
		// 3. 개체 정보 조회 후 개체 정보가 있으면 success를 true로 리턴
		final Map<String, Object> indvInfo	= this.selectIndvInfo(params);
		if (indvInfo == null || indvInfo.isEmpty()) {
			result.put("success", false);
			result.put("type", "C");
			result.put("message", "개체 정보가 없습니다.<br/>계속 하시겠습니까?");
			return result;
		}
		
		result.put("success", true);
		return result;
	}
	
	/**
	 * 등록된 개체 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> selectIndvInfo(final Map<String, Object> params) throws SQLException {
		params.put("na_bzplc", params.getOrDefault("naBzplc", sessionUtil.getNaBzplc()));
		params.put("sra_indv_amnno", params.getOrDefault("sra_indv_amnno", params.get("sraIndvAmnno")));
		return adminTaskDAO.selectIndvInfo(params);
	}

	/**
	 * 경매일자 조회(진행 또는 진행예정)
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> selectAucDtList(Map<String, Object> params) throws SQLException {
		return adminTaskDAO.selectAucDtList(params);
	}

	/**
	 * 경매일자, 경매대상 체크
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> checkAucDt(Map<String, Object> params) throws SQLException {
		final Map<String, Object> result = new HashMap<String, Object>();
		
		final List<Map<String, Object>> aucDtList = this.selectQcnInfo(params);
		if (aucDtList != null && aucDtList.size() > 0) {
			result.put("success", true);
		}
		else {
			result.put("success", false);
			result.put("message", "등록된 차수가 없습니다.<br/>차수를 등록해주세요.");
		}
		return result;
	}

	/**
	 * QCN 정보
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> selectQcnInfo(Map<String, Object> params) throws SQLException {
		return adminTaskDAO.selectQcnInfo(params);
	}

	/**
	 * 개체 + 출장우 정보 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> selectSogCowInfo(Map<String, Object> params) throws SQLException {
		params.put("na_bzplc", params.getOrDefault("naBzplc", sessionUtil.getNaBzplc()));
		params.put("sra_indv_amnno", params.getOrDefault("sra_indv_amnno", params.get("sraIndvAmnno")));
		params.put("auc_dt", params.getOrDefault("auc_dt", params.get("aucDt")));
		params.put("auc_obj_dsc", params.getOrDefault("auc_obj_dsc", params.get("aucObjDsc")));
		return adminTaskDAO.selectSogCowInfo(params);
	}
	
	/**
	 * 개체 + 출장우 정보 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> selectSogCowLogList(Map<String, Object> params) throws SQLException {
		params.put("na_bzplc", params.getOrDefault("naBzplc", sessionUtil.getNaBzplc()));
		params.put("sra_indv_amnno", params.getOrDefault("sra_indv_amnno", params.get("sraIndvAmnno")));
		params.put("auc_dt", params.getOrDefault("auc_dt", params.get("aucDt")));
		params.put("auc_obj_dsc", params.getOrDefault("auc_obj_dsc", params.get("aucObjDsc")));
		return adminTaskDAO.selectSogCowLogList(params);
	}

	/**
	 * 출장우 정보 저장
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int insertSogCow(Map<String, Object> params) throws SQLException {
		return adminTaskDAO.insertSogCow(params);
	}
	
	/**
	 * 출장우 정보 수정
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int updateSogCow(Map<String, Object> params) throws SQLException {
		return adminTaskDAO.updateSogCow(params);
	}

	/**
	 * 출장우 정보 저장
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> registSogCow(Map<String, Object> params) throws SQLException, Exception {
		final Map<String, Object> result = new HashMap<String, Object>();
		int cnt = 0;
		
		// 경매번호 중복 체크
		int regCnt = adminTaskDAO.checkAucPrgSq(params);
		if (regCnt > 0) {
			result.put("success", false);
			result.put("message", "동일한 경매번호가 있습니다.");
			return result;
		}
		
		// 등록모드가 regist인 경우 신규 저장
		if ("regist".equals(params.get("regMode"))) {
			cnt += this.insertSogCow(params);
		}
		// 등록모드가 modify인 경우 업데이트
		else {
			cnt += this.updateSogCow(params);
		}
		
		if (cnt == 0) {
			result.put("success", false);
			result.put("message", "저장에 실패했습니다.");
			return result;
		}
		
		// 출장우 로그 저장
		adminTaskDAO.insertSogCowLog(params);
		
		// 개체정보 저장
		commonService.updateIndvInfo(params);
		
		result.put("success", true);
		result.put("message", "저장되었습니다.");
		return result;
	}

	/**
	 * 출장우 정보, 출장우 로그 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> searchSogCow(Map<String, Object> params) throws SQLException {
		final Map<String, Object> result = new HashMap<String, Object>();
	
		result.put("qcnInfo", this.selectQcnInfo(params));
		result.put("sogCowInfo", this.selectSogCowInfo(params));
		result.put("sogCowLogList", this.selectSogCowLogList(params));
		result.put("success", true);
		
		return result;
	}

	/**
	 * 변경한 경매내역 저장
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> updateResult(Map<String, Object> params) throws SQLException {
		Map<String, Object> result = new HashMap<String, Object>();
		
		// 낙찰받은 사람의 참가번호가 없는 경우
		// 낙찰자 이름, 생년월일을 입력받아 참가번호 생성 후 내역 변경
		if ("22".equals(params.get("selStsDsc"))) {
			if ("1".equals(params.get("entryYn"))) {
				// 1. 낙찰자 이름, 생년월일로 등록된 중도매인 정보가 있는지 확인
				final Map<String, Object> mwmnInfo = adminTaskDAO.selectMwmnInfo(params);
				
				// 2. 중도매인 정보가 없는 경우 false return
				if (mwmnInfo == null || mwmnInfo.isEmpty()) {
					result.put("success", false);
					result.put("message", "등록된 중도매인 정보가 없습니다.");
					return result;
				}
				
				params.put("trmnAmnno", mwmnInfo.get("TRMN_AMNNO"));
				// 3. 중도매인 정보가 있는 경우 참가번호 부여
				
				if (mwmnInfo.get("LVST_AUC_PTC_MN_NO") == null || "".equals(mwmnInfo.getOrDefault("LVST_AUC_PTC_MN_NO", ""))) {
					adminTaskDAO.insertAucEntr(params);
				}
			}
			// 등록된 참가번호인지 조회
			else {
				final Map<String, Object> aucEntrInfo = adminTaskDAO.selectAucEntrInfo(params);
				
				// 2. 참가 정보가 없는 경우 false return
				if (aucEntrInfo == null || aucEntrInfo.isEmpty()) {
					result.put("success", false);
					result.put("message", "등록된 참가 정보가 없습니다.");
					return result;
				}
				
				params.put("trmnAmnno", aucEntrInfo.get("TRMN_AMNNO"));
			}
		}

		// 4. 경매내역 저장
		result = auctionService.updateAuctionResultMap(params);

		return result;
	}
	
	/**
	 * 출장우 이미지 업로드
	 * @param params
	 * @return
	 * @throws SQLException 
	 * @throws KeyStoreException 
	 */
	@Override
	public Map<String, Object> uploadImageProc(Map<String, Object> params) throws SQLException, AmazonS3Exception, SdkClientException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
		final Map<String, Object> result = new HashMap<String, Object>();

		final String[] protocols = {"TLSv1.2", "TLSv1.1", "TLSv1", "SSLv3", "SSLv2Hello"};
		final String[] chipers = {"TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384", "TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256", "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384", "TLS_RSA_WITH_AES_256_GCM_SHA384", "TLS_ECDH_ECDSA_WITH_AES_256_GCM_SHA384", "TLS_ECDH_RSA_WITH_AES_256_GCM_SHA384", "TLS_DHE_RSA_WITH_AES_256_GCM_SHA384", "TLS_DHE_DSS_WITH_AES_256_GCM_SHA384", "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256", "TLS_RSA_WITH_AES_128_GCM_SHA256", "TLS_ECDH_ECDSA_WITH_AES_128_GCM_SHA256", "TLS_ECDH_RSA_WITH_AES_128_GCM_SHA256", "TLS_DHE_RSA_WITH_AES_128_GCM_SHA256", "TLS_DHE_DSS_WITH_AES_128_GCM_SHA256", "TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384", "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384", "TLS_RSA_WITH_AES_256_CBC_SHA256", "TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA384", "TLS_ECDH_RSA_WITH_AES_256_CBC_SHA384", "TLS_DHE_RSA_WITH_AES_256_CBC_SHA256", "TLS_DHE_DSS_WITH_AES_256_CBC_SHA256", "TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA", "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA", "TLS_RSA_WITH_AES_256_CBC_SHA", "TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA", "TLS_ECDH_RSA_WITH_AES_256_CBC_SHA", "TLS_DHE_RSA_WITH_AES_256_CBC_SHA", "TLS_DHE_DSS_WITH_AES_256_CBC_SHA", "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256", "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256", "TLS_RSA_WITH_AES_128_CBC_SHA256", "TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA256", "TLS_ECDH_RSA_WITH_AES_128_CBC_SHA256", "TLS_DHE_RSA_WITH_AES_128_CBC_SHA256", "TLS_DHE_DSS_WITH_AES_128_CBC_SHA256", "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA", "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA", "TLS_RSA_WITH_AES_128_CBC_SHA", "TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA", "TLS_ECDH_RSA_WITH_AES_128_CBC_SHA", "TLS_DHE_RSA_WITH_AES_128_CBC_SHA", "TLS_DHE_DSS_WITH_AES_128_CBC_SHA", "TLS_EMPTY_RENEGOTIATION_INFO_SCSV"};
		final TrustManager[] trustAllCerts = new TrustManager[] {
			new X509TrustManager() {
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				@Override
				public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {return;}
				@Override
				public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {return;}
			}
		};
		HostnameVerifier hv = new HostnameVerifier() {
			@Override
			public boolean verify(String arg0, SSLSession arg1) {
				return true;
			}
		};

		final SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new SecureRandom());
		
		ClientConfiguration cc = new ClientConfiguration();
		SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(sc, protocols, chipers, hv);
		cc.getApacheHttpClientConfig().setSslSocketFactory(ssf);
		cc.setSignerOverride("AWSS3V4SignerType");
		cc.setMaxErrorRetry(0);
		cc.setConnectionTimeout(5000);
		cc.withProtocol(Protocol.HTTP);
		
		// S3 client
		final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
												 .withClientConfiguration(cc)
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

		final String naBzplc = params.getOrDefault("naBzplc", sessionUtil.getNaBzplc()).toString();
		final String aucDt = params.get("aucDt").toString();
		final String sraIndvAmnno = params.get("sraIndvAmnno").toString();
		final String filePath = naBzplc + "/" + sraIndvAmnno + "/";
		final String fileExtNm = ".png";
		
		// 해당 path에 있는 이미지 삭제 : 이미지 저장에 실패했을때 transaction은 어떤 방식으로 처리할 지....
//		for (S3ObjectSummary file : s3.listObjects(bucketName, filePath).getObjectSummaries()){
//			s3.deleteObject(bucketName, file.getKey());
//		}
		
		// 이미지 정보 삭제
		adminTaskDAO.deleteCowImg(params);
		
		List<Map<String, Object>> files = (List<Map<String, Object>>)params.get("files");
		int successCnt = 0;
		int imgSqno = 1;
		if (!ObjectUtils.isEmpty(files)) {
			for (Map<String, Object> file : files) {

				boolean isSuccess = true;
				String fileNm = "";
				InputStream oriBis = null;

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
					oriBis = new ByteArrayInputStream(oriImgByte);

					ObjectMetadata oriObjectMetadata = new ObjectMetadata();
					oriObjectMetadata.setContentLength(oriImgByte.length);
					oriObjectMetadata.setContentType(MediaType.IMAGE_PNG_VALUE);
					PutObjectRequest oriPutObjectRequest = new PutObjectRequest(bucketName, filePath + fileNm + fileExtNm, oriBis, oriObjectMetadata);

//					try {
					oriPutObjectRequest.setAccessControlList(accessControlList);
					s3.putObject(oriPutObjectRequest);
//					}
//					catch (AmazonS3Exception e) {
//						log.error("AdminTaskServiceImpl.uploadImageProc AmazonS3Exception : {} ", e);
//						isSuccess = false;
//					}
//					catch(SdkClientException e) {
//						log.error("AdminTaskServiceImpl.uploadImageProc SdkClientException : {} ", e);
//						isSuccess = false;
//					}
//					finally {
					if(oriBis != null) try {oriBis.close();}catch(IOException ie) {log.error("AdminTaskServiceImpl.uploadImageProc IOException : {} ", ie);}
//					}
					
					if (!isSuccess) continue;
					successCnt++;

					params.put("imgSqno", imgSqno);
					params.put("filePath", filePath);
					params.put("fileNm", fileNm);
					params.put("fileExtNm", fileExtNm);
					adminTaskDAO.insertCowImg(params);
					imgSqno++;

					// 썸네일 파일이 없는 경우 or 값이 data:image로 시작하지 않는 경우 pass
					if (ObjectUtils.isEmpty(file.get("thumbnail"))
					|| !file.getOrDefault("thumbnail", ",").toString().startsWith("data:image")) continue;
					
					InputStream thumbBis = null;
					String thumbnail = file.getOrDefault("thumbnail", ",").toString();
					String[] thumbBase64Arr = thumbnail.split(",");
					byte[] thumbImgByte = Base64.decodeBase64(thumbBase64Arr[1]);
					thumbBis = new ByteArrayInputStream(thumbImgByte);
					
					ObjectMetadata thumbObjectMetadata = new ObjectMetadata();
					thumbObjectMetadata.setContentLength(thumbImgByte.length);
					thumbObjectMetadata.setContentType(MediaType.IMAGE_PNG_VALUE);
					PutObjectRequest thumbPutObjectRequest = new PutObjectRequest(bucketName, filePath + "thumb/" + fileNm + fileExtNm, thumbBis, thumbObjectMetadata);
					
//					try {
					thumbPutObjectRequest.setAccessControlList(accessControlList);
					s3.putObject(thumbPutObjectRequest);
//					}
//					catch (AmazonS3Exception e) {
//						log.error("AdminTaskServiceImpl.uploadImageProc AmazonS3Exception : {} ", e);
//					}
//					catch(SdkClientException e) {
//						log.error("AdminTaskServiceImpl.uploadImageProc SdkClientException : {} ", e);
//					}
//					finally {
					if (thumbBis != null) try {thumbBis.close();}catch(IOException ie) {log.error("AdminTaskServiceImpl.uploadImageProc IOException : {} ", ie);}
//					}
				}
			}
		}
		
		result.put("success", (files.size() == successCnt));
		result.put("message", (files.size() == successCnt) ? "이미지 저장에 성공했습니다." : "이미지 저장에 실패했습니다.");
//		result.put("message", String.format("전체 : %d, 성공 : %d", files.size(), successCnt));
		return result;
	}
	
	/**
	 * 출장우 이미지 업로드
	 * @param params
	 * @return
	 * @throws SQLException 
	 * @throws RuntimeException 
	 */
	@Override
	public Map<String, Object> uploadImageInfoProc(Map<String, Object> params) throws SQLException, RuntimeException {
		final Map<String, Object> result = new HashMap<String, Object>();
		
		// 이미지 정보 삭제
		adminTaskDAO.deleteCowImg(params);
		
		final String naBzplc = params.getOrDefault("naBzplc", sessionUtil.getNaBzplc()).toString();
		final String aucDt = params.get("aucDt").toString();
		final String sraIndvAmnno = params.get("sraIndvAmnno").toString();
		
		List<Map<String, Object>> uploadList = (List<Map<String, Object>>)params.get("uploadList");
		int successCnt = 0;
		int imgSqno = 1;
		if (!ObjectUtils.isEmpty(uploadList)) {
			for (Map<String, Object> info : uploadList) {
				params.put("imgSqno", imgSqno);
				params.put("filePath", info.get("filePath"));
				params.put("fileNm", info.get("fileNm"));
				params.put("fileExtNm", info.get("fileExtNm"));
				adminTaskDAO.insertCowImg(params);
				imgSqno++;
			}
		}
		
		result.put("success", true);
		result.put("message", "이미지 저장에 성공했습니다.");
		return result;
	}

	@Override
	public List<Map<String, Object>> selectCowImg(Map<String, Object> params) throws SQLException {
		params.put("imgDomain", endPoint + "/" + bucketName + "/");
		return adminTaskDAO.selectCowImg(params);
	}

}
