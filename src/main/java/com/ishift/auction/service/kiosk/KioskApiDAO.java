package com.ishift.auction.service.kiosk;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ishift.auction.base.dao.MainDao;

@Repository("kioskApiDAO")
public class KioskApiDAO {
	
	@Autowired
	private MainDao mainDao;

	/**
	 * 키오스크 중도매인 로그인을 위한 정보 조회
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	public Map<String, Object> selectMwmnInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("KioskApiMapper.selectMwmnInfo", params);
	}
	
	/**
	 * 출장우 정보 리스트 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectSogCowList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("KioskApiMapper.selectSogCowList", params);
	}

	/**
	 * 중도매인 키오스크 인증번호 정보
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> selectAuthNoInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("KioskApiMapper.selectAuthNoInfo", params);
	}

	/**
	 * 출하주 키오스크 인증번호 정보
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	public Map<String, Object> selectFhsAuthNoInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("KioskApiMapper.selectFhsAuthNoInfo", params);
	}

	/**
	 * 중도매인 참가번호 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectEntryList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("KioskApiMapper.selectEntryList", params);
	}

	/**
	 * 중도매인 참가번호 저장
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int insertEntryInfo(Map<String, Object> params) throws SQLException {
		return mainDao.insert("KioskApiMapper.insertEntryInfo", params);
	}

	/**
	 * 경매차수 정보
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> selectAucQcnInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("KioskApiMapper.selectAucQcnInfo", params);
	}
	
	/**
	 * 낙찰받은 출장우 리스트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectBuyList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("KioskApiMapper.selectBuyList", params);
	}
	
	/**
	 * 가축시장 정보
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	public Map<String, Object> selectWmcInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("KioskApiMapper.selectWmcInfo", params);
	}

	/**
	 * 참가정보
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	public Map<String, Object> selectEntrInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("KioskApiMapper.selectEntrInfo", params);
	}

	/**
	 * 낙찰 정산 상세 정보
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	public Map<String, Object> selectStatInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("KioskApiMapper.selectStatInfo", params);
	}

	/**
	 * 송아지, 비육우, 번식우 출하자 정보
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	public List<Map<String, Object>> selectAucObjDecList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("KioskApiMapper.selectAucObjDecList", params);
	}

	/**
	 * 송아지, 비육우, 번식우별 출하 상세 리스트
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	public List<Map<String, Object>> selectSelList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("KioskApiMapper.selectSelList", params);
	}
	
	/**
	 * 송아지, 비육우, 번식우별 출하자 통계
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	public Map<String, Object> selectSelStatInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("KioskApiMapper.selectSelStatInfo", params);
	}

}
