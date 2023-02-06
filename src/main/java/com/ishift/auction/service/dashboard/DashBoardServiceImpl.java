package com.ishift.auction.service.dashboard;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("DashBoardService")
public class DashBoardServiceImpl implements DashBoardService {

	private static Logger log = LoggerFactory.getLogger(DashBoardServiceImpl.class);
	private final static long duration = 3600 * 1000L;		//1시간 (3600초)
	public static final Map<String, Object> COW_PRICE_LIST = new HashMap<String, Object>();						//findCowPriceList
	public static final Map<String, Object> AVG_PLACE_BID_AM_LIST = new HashMap<String, Object>();		//findAvgPlaceBidAmList
	public static final Map<String, Object> RECENT_DATE_TOP_LIST = new HashMap<String, Object>();			//findRecentDateTopList
	
	@Autowired
	DashBoardDAO dashBoardDAO;
	
	//데이터 캐시 clear 하기
	public void invalidateCacheMap(String cacheName) {
		switch(cacheName) {
			case "COW_PRICE_LIST" : COW_PRICE_LIST.clear(); break;
			case "AVG_PLACE_BID_AM_LIST" : AVG_PLACE_BID_AM_LIST.clear(); break;
			case "RECENT_DATE_TOP_LIST" : RECENT_DATE_TOP_LIST.clear(); break;
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
			long loadTime = !COW_PRICE_LIST.isEmpty() ? (long) COW_PRICE_LIST.get("loadTime") : 0L;
			
			try {
				if(COW_PRICE_LIST.isEmpty() || now - loadTime > duration) {
					synchronized (COW_PRICE_LIST) {
						if(COW_PRICE_LIST.isEmpty() || now - loadTime > duration) {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("list", dashBoardDAO.findCowPriceList(reqMap));
							
							COW_PRICE_LIST.clear();
							COW_PRICE_LIST.putAll(map);
							COW_PRICE_LIST.put("loadTime", now);
						}
					}
				}
				
				resultList = (List<Map<String, Object>>) COW_PRICE_LIST.get("list");
			}
			catch(RuntimeException | SQLException se) {
				log.error("DashboardServiceImpl.findCowPriceList : {} ", se);
			}
			catch (Exception e) {
				log.error("DashboardServiceImpl.findCowPriceList : {} ", e);
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
			long loadTime = !AVG_PLACE_BID_AM_LIST.isEmpty() ? (long) AVG_PLACE_BID_AM_LIST.get("loadTime") : 0L;
			
			try {
				if(AVG_PLACE_BID_AM_LIST.isEmpty() || now - loadTime > duration) {
					synchronized (AVG_PLACE_BID_AM_LIST) {
						if(AVG_PLACE_BID_AM_LIST.isEmpty() || now - loadTime > duration) {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("list", dashBoardDAO.findAvgPlaceBidAmList(reqMap));
							
							AVG_PLACE_BID_AM_LIST.clear();
							AVG_PLACE_BID_AM_LIST.putAll(map);
							AVG_PLACE_BID_AM_LIST.put("loadTime", now);
						}
					}
				}
				
				resultList = (List<Map<String, Object>>) AVG_PLACE_BID_AM_LIST.get("list");
			} 
			catch(RuntimeException | SQLException se) {
				log.error("DashboardServiceImpl.findAvgPlaceBidAmList : {} ", se);
			}
			catch (Exception e) {
				log.error("DashboardServiceImpl.findAvgPlaceBidAmList : {} ", e);
			}
		}else {
			resultList = dashBoardDAO.findAvgPlaceBidAmList(reqMap);
		}
		
		return resultList;
	}

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
			long loadTime = !RECENT_DATE_TOP_LIST.isEmpty() ? (long) RECENT_DATE_TOP_LIST.get("loadTime") : 0L;
			
			try {
				if(RECENT_DATE_TOP_LIST.isEmpty() || now - loadTime > duration) {
					synchronized (RECENT_DATE_TOP_LIST) {
						if(RECENT_DATE_TOP_LIST.isEmpty() || now - loadTime > duration) {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("list", dashBoardDAO.findRecentDateTopList(reqMap));
							
							RECENT_DATE_TOP_LIST.clear();
							RECENT_DATE_TOP_LIST.putAll(map);
							RECENT_DATE_TOP_LIST.put("loadTime", now);
						}
					}
				}
				
				resultList = (List<Map<String, Object>>) RECENT_DATE_TOP_LIST.get("list");
			}
			catch(RuntimeException | SQLException se) {
				log.error("DashboardServiceImpl.findRecentDateTopList : {} ", se);
			}
			catch (Exception e) {
				log.error("DashboardServiceImpl.findRecentDateTopList : {} ", e);
			}
		}else {
			resultList = dashBoardDAO.findRecentDateTopList(reqMap);
		}
		
		return resultList;
	}

	@Override
	public List<Map<String, Object>> findFilterRegionList(Map<String, Object> params) throws SQLException {
		return dashBoardDAO.findFilterRegionList(params);
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
