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
	 * 키오스크 인증번호 확인
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> selectAuthNoInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("KioskApiMapper.selectAuthNoInfo", params);
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


}
