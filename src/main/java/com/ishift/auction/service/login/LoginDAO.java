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
	 * 인증번호 조회 (중도매인)
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> selectAuthNumberInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("LoginMapper.selectAuthNumberInfo", params);
	}

	/**
	 * 새로 발급한 인증번호 저장 (중도매인)
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
	 * 로그인 최종접속일시, 휴면예정일자 업데이트
	 * 최종접속일시 SYSDATE, 휴면예정일자 TO_CHAR(SYSDATE + 365, 'YYYYMMDD')
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int updateMbintgConDormInfo(Map<String, Object> params) throws SQLException{
		return mainDao.update("LoginMapper.updateMbintgConDormInfo", params);
	}

	/**
	 * 회원통합번호를 중도매인 테이블 컬럼에 업데이트 
	 * @param params
	 * @return int
	 * @throws SQLException
	 */
	public int updateMmMwmnMbintgNo(Map<String, Object> params) throws SQLException{
		return mainDao.update("LoginMapper.updateMmMwmnMbintgNo", params);
	}

	/**
	 * 회원통합번호를 출하주(농가) 테이블 컬럼에 업데이트
	 * @param params
	 * @return int
	 * @throws SQLException
	 */
	public int updateMmFhsMbintgNo(Map<String, Object> params) throws SQLException{
		return mainDao.update("LoginMapper.updateMmFhsMbintgNo", params);
	}

	/**
	 * 로그인 이력 저장하기
	 * @param params
	 * @throws SQLException
	 */
	public void insertMmConnHistory(Map<String, Object> params) throws SQLException{
		mainDao.insert("LoginMapper.insertMmConnHistory", params);
	}

	/**
	 * 인증번호 조회 (출하주)
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> selectAuthFhsNumberInfo(Map<String, Object> params) throws SQLException{
		return mainDao.selectOne("LoginMapper.selectAuthFhsNumberInfo", params);
	}

	/**
	 * 새로 발급한 인증번호 저장 (출하주)
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int updateAuthFhsNumber(Map<String, Object> params) throws SQLException{
		return mainDao.update("LoginMapper.updateAuthFhsNumber", params);
	}

}