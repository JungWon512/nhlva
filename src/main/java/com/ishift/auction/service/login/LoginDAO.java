package com.ishift.auction.service.login;

import com.ishift.auction.base.dao.MainDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository("loginDAO")
public class LoginDAO {
	
	@Autowired
	private MainDao mainDao;

	/**
	 * 중도매인 리스트 검색
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectWholesalerList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("LoginMapper.selectWholesalerList", params);
	}

	/**
	 * 중도매인 정보
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> selectWholesaler(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("LoginMapper.selectWholesaler", params);
	}

	/**
	 * 방문로그 저장
	 * @param vo
	 * @return
	 * @throws SQLException
	 */
	public int insertVisitor(Map<String, Object> vo) throws SQLException {
		return mainDao.insert("LoginMapper.insertVisitor", vo);
	}

	/**
	 * 로그인 출하주 검색
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectFarmUserList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("LoginMapper.selectFarmUserList", params);
	}

	/**
	 * 조합코드(NA_BZPLC)와 농가 식별번호(FHS_ID_NO)로 출하주 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> selectFarmUser(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("LoginMapper.selectFarmUser", params);
	}

	/**
	 * 인증번호 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> selectAuthNumberInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("LoginMapper.selectAuthNumberInfo", params);
	}

	/**
	 * 새로 발급한 인증번호 저장
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int updateAuthNumber(Map<String, Object> params) throws SQLException {
		return mainDao.update("LoginMapper.updateAuthNumber", params);
	}

	/**
	 * 로그인 인증번호 발송
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int sendSms(Map<String, Object> params) throws SQLException {
		return mainDao.update("LoginMapper.sendSms", params);
	}

	/**
	 * @methodName    : updateKkoRefreshToekn
	 * @author        : Jung JungWon
	 * @return 
	 * @date          : 2022.11.04
	 * @Comments      : 
	 */
	public int updateKkoRefreshToekn(Map<String, Object> params) throws SQLException {
		return mainDao.update("LoginMapper.updateKkoRefreshToekn", params);		
	}

}