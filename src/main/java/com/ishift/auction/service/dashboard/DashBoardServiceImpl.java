package com.ishift.auction.service.dashboard;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.Base64;
import com.amazonaws.util.IOUtils;

@Service("DashBoardService")
public class DashBoardServiceImpl implements DashBoardService {

	private static long loadTime; 
	private final static long duration = 3600 * 1000L;		//1시간 (3600초)
	public static final Map<String, Object> COW_PRICE_LIST = new HashMap<String, Object>();						//findCowPriceList
	public static final Map<String, Object> AVG_PLACE_BID_AM_LIST = new HashMap<String, Object>();		//findAvgPlaceBidAmList
	public static final Map<String, Object> RECENT_DATE_TOP_LIST = new HashMap<String, Object>();			//findRecentDateTopList
	public static final Map<String, Object> JOHAP_LOGO_LIST = new HashMap<String, Object>();			//findJohapLogoList
	
	@Autowired
	DashBoardDAO dashBoardDAO;
	
	@Value("${ncloud.storage.end-point}") private String endPoint;
	@Value("${ncloud.storage.region-name}") private String regionName;
	@Value("${ncloud.storage.access-key}") private String accessKey;
	@Value("${ncloud.storage.secret-key}") private String secretKey;
	@Value("${ncloud.storage.bucket-name}") private String bucketName;
	
	//데이터 캐시 clear 하기
	public void invalidateCacheMap(String cacheName) {
		switch(cacheName) {
			case "COW_PRICE_LIST" : COW_PRICE_LIST.clear(); break;
			case "AVG_PLACE_BID_AM_LIST" : AVG_PLACE_BID_AM_LIST.clear(); break;
			case "RECENT_DATE_TOP_LIST" : RECENT_DATE_TOP_LIST.clear(); break;
			case "JOHAP_LOGO_LIST" : JOHAP_LOGO_LIST.clear(); break;
		}
	}
	
	@Override
	public List<Map<String, Object>> findCowPriceList(Map<String, Object> reqMap) throws SQLException {
		
		List<Map<String, Object>> resultList = null;
		boolean isCache = false;
		if("1".equals(reqMap.get("searchAucObjDsc")) 
				&& ("range10".equals(reqMap.get("searchRaDate")) || reqMap.get("searchRaDate") == null) 
				&& ("".equals(reqMap.get("searchPlace")) || reqMap.get("searchPlace") == null)
			) {		//기본 파라미터일 때, 캐시되도록 하기
			isCache = true;
		}
		
		if(isCache) {
			long now = System.currentTimeMillis();
			try {
				if(COW_PRICE_LIST.isEmpty() || now - loadTime > duration) {
					synchronized (COW_PRICE_LIST) {
						if(COW_PRICE_LIST.isEmpty() || now - loadTime > duration) {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("list", dashBoardDAO.findCowPriceList(reqMap));
							
							COW_PRICE_LIST.clear();
							COW_PRICE_LIST.putAll(map);
							loadTime = now;
						}
					}
				}
				
				resultList = (List<Map<String, Object>>) COW_PRICE_LIST.get("list");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			resultList = dashBoardDAO.findCowPriceList(reqMap);
		}
		
		return resultList;
	}

	@Override
	public List<Map<String, Object>> findAvgPlaceBidAmList(Map<String, Object> reqMap) throws SQLException {
		
		List<Map<String, Object>> resultList = null;
		boolean isCache = false;
		if("1".equals(reqMap.get("searchAucObjDsc")) 
				&& ("range10".equals(reqMap.get("searchRaDate")) || reqMap.get("searchRaDate") == null) 
				&& ("".equals(reqMap.get("searchMonthOldC")) || reqMap.get("searchMonthOldC") == null)
			) {		//기본 파라미터일 때, 캐시되도록 하기
			isCache = true;
		}
		
		if(isCache) {
			long now = System.currentTimeMillis();
			try {
				if(AVG_PLACE_BID_AM_LIST.isEmpty() || now - loadTime > duration) {
					synchronized (AVG_PLACE_BID_AM_LIST) {
						if(AVG_PLACE_BID_AM_LIST.isEmpty() || now - loadTime > duration) {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("list", dashBoardDAO.findAvgPlaceBidAmList(reqMap));
							
							AVG_PLACE_BID_AM_LIST.clear();
							AVG_PLACE_BID_AM_LIST.putAll(map);
							loadTime = now;
						}
					}
				}
				
				resultList = (List<Map<String, Object>>) AVG_PLACE_BID_AM_LIST.get("list");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			resultList = dashBoardDAO.findAvgPlaceBidAmList(reqMap);
		}
		
		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> findRecentDateTopList(Map<String, Object> reqMap) throws SQLException {
		List<Map<String, Object>> resultList = null;
		boolean isCache = false;
		if("1".equals(reqMap.get("searchAucObjDsc")) 
				&& ("".equals(reqMap.get("searchMonthOldC")) || reqMap.get("searchMonthOldC") == null)
				&& ("".equals(reqMap.get("searchPlace")) || reqMap.get("searchPlace") == null)
			) {		//기본 파라미터일 때, 캐시되도록 하기
			isCache = true;
		}
		
		if(isCache) {
			long now = System.currentTimeMillis();
			try {
				if(RECENT_DATE_TOP_LIST.isEmpty() || now - loadTime > duration) {
					synchronized (RECENT_DATE_TOP_LIST) {
						if(RECENT_DATE_TOP_LIST.isEmpty() || now - loadTime > duration) {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("list", dashBoardDAO.findRecentDateTopList(reqMap));
							
							RECENT_DATE_TOP_LIST.clear();
							RECENT_DATE_TOP_LIST.putAll(map);
							loadTime = now;
						}
					}
				}
				
				resultList = (List<Map<String, Object>>) RECENT_DATE_TOP_LIST.get("list");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			resultList = dashBoardDAO.findRecentDateTopList(reqMap);
		}
		
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> findJohapLogoList(Map<String, Object> reqMap) throws SQLException {
		List<Map<String, Object>> resultList = null;
		boolean isCache = false;
		if("1".equals(reqMap.get("searchAucObjDsc")) 
				&& ("".equals(reqMap.get("searchMonthOldC")) || reqMap.get("searchMonthOldC") == null)
				&& ("".equals(reqMap.get("searchPlace")) || reqMap.get("searchPlace") == null)
				) {		//기본 파라미터일 때, 캐시되도록 하기
			isCache = true;
		}
		
		if(isCache) {
			long now = System.currentTimeMillis();
			try {
				if(JOHAP_LOGO_LIST.isEmpty() || now - loadTime > duration) {
					synchronized (JOHAP_LOGO_LIST) {
						if(JOHAP_LOGO_LIST.isEmpty() || now - loadTime > duration) {
							Map<String, Object> map = new HashMap<String, Object>();
							List<Map<String, Object>> fileList = this.selectLogoImgList();
							
							map.put("list", fileList);
							
							JOHAP_LOGO_LIST.clear();
							JOHAP_LOGO_LIST.putAll(map);
							loadTime = now;
						}
					}
				}
				
				resultList = (List<Map<String, Object>>) JOHAP_LOGO_LIST.get("list");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			try {
				resultList = this.selectLogoImgList();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return resultList;
	}

	public List<Map<String, Object>> selectLogoImgList() throws SQLException, IOException {
		List<Map<String, Object>> reList = new ArrayList<>();
		
		// S3 client
		final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
		    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
		    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
		    .build();
	
		// top level folders and files in the bucket
		try {
		    String filePath = "logo/";
		    
		    ObjectListing objectListing = s3.listObjects(bucketName, filePath);
	
		    for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
		    	Map<String, Object> reMap = new HashMap<>();
			    S3Object s3Object = s3.getObject(bucketName, objectSummary.getKey());
			    S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
			    byte[] sourceBytes = IOUtils.toByteArray(s3ObjectInputStream);
			    reMap.put("file_nm", objectSummary.getKey());
			    reMap.put("file_src","data:image/png;base64," + Base64.encodeAsString(sourceBytes)); 
			    
				s3ObjectInputStream.close();
			    reList.add(reMap);
		    }
		} catch (AmazonS3Exception e) {
		    e.printStackTrace();
		} catch(SdkClientException e) {
		    e.printStackTrace();
		}
		
		return reList;
	}
	
	@Override
	public Map<String, Object> findPartiBidderInfo(Map<String, Object> params) throws SQLException {
		return dashBoardDAO.findPartiBidderInfo(params);
	}

	@Override
	public List<Map<String, Object>> findPartiBidderPerList(Map<String, Object> params) throws SQLException {
		return dashBoardDAO.findPartiBidderPerList(params);
	}

	@Override
	public Map<String, Object> getBtcAucDateInfo(Map<String, Object> params) throws SQLException {
		return dashBoardDAO.getBtcAucDateInfo(params);
	}

	@Override
	public List<Map<String, Object>> getBtcAucGradeList(Map<String, Object> params) throws SQLException {
		return dashBoardDAO.getBtcAucGradeList(params);
	}

	@Override
	public Map<String, Object> getBtcAucPriceAvgInfo(Map<String, Object> params) throws SQLException {
		return dashBoardDAO.getBtcAucPriceAvgInfo(params);
	}

	@Override
	public List<Map<String, Object>> getBtcAucAreaAvgList(Map<String, Object> params) throws SQLException {
		return dashBoardDAO.getBtcAucAreaAvgList(params);
	}

	@Override
	public Map<String, Object> getBtcAucAreaAvgSum(Map<String, Object> params) throws SQLException {
		return dashBoardDAO.getBtcAucAreaAvgSum(params);
	}

	@Override
	public Map<String, Object> findSbidPriceInfo(Map<String, Object> params) throws SQLException {
		return dashBoardDAO.findSbidPriceInfo(params);
	}
	
	@Override
	public Map<String, Object> findSogCowInfo(Map<String, Object> params) throws SQLException {
		return dashBoardDAO.findSogCowInfo(params);
	}
	
	@Override
	public List<Map<String, Object>> findSogCowInfoList(Map<String, Object> params) throws SQLException {
		return dashBoardDAO.findSogCowInfoList(params);
	}

	@Override
	public List<Map<String, Object>> findAreaSbidList(Map<String, Object> params) throws SQLException {
		return dashBoardDAO.findAreaSbidList(params);
	}

	@Override
	public List<Map<String, Object>> findMonthlySbidPriceList(Map<String, Object> params) throws SQLException {
		return dashBoardDAO.findMonthlySbidPriceList(params);
	}
	
	@Override
	public List<Map<String, Object>> findMonthlySogCowList(Map<String, Object> params) throws SQLException {
		return dashBoardDAO.findMonthlySogCowList(params);
	}
	
	@Override
	public Map<String, Object> selStaticInfo(Map<String, Object> params) throws SQLException{
		return dashBoardDAO.selStaticInfo(params);
	}
	
	@Override
	public List<Map<String, Object>> selStaticList(Map<String, Object> params) throws SQLException{
		return dashBoardDAO.selStaticList(params);		
	}
	@Override
	public Map<String, Object> selAucStaticInfo(Map<String, Object> params) throws SQLException{
		return dashBoardDAO.selAucStaticInfo(params);				
	}
	@Override
	public List<Map<String, Object>> selMhSogCowRowDataList(Map<String, Object> params) throws SQLException{
		return dashBoardDAO.selMhSogCowRowDataList(params);
	}
}
