package com.ishift.auction.service.common;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ishift.auction.base.dao.MainDao;

@Repository("commonDAO")
public class CommonDAO {
	
	@Autowired
	private MainDao mainDao;

	/**
	 * 공통 코드 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getCommonCode(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("commonMapper.getCommonCode", params);
	}

	/**
	 * 한우종합에 등록된 개체정보 저장
	 * @param dataMap
	 * @return
	 * @throws SQLException 
	 */
	public int updateIndvInfo(Map<String, Object> params) throws SQLException {
		return mainDao.update("commonMapper.updateIndvInfo", params);
	}
	
	/**
	 * 개체정보 이력 저장
	 * @param params
	 * @throws SQLException
	 */
	public void insertIndvLog(Map<String, Object> params) throws SQLException {
		mainDao.insert("commonMapper.insertIndvLog", params);
	}

	/**
	 * 경매장 정보 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> getBzplcInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("commonMapper.getBzplcInfo", params);
	}

	/**
	 * 개체정보에 포함된 출하주정보 저장
	 * @param dataMap
	 * @throws SQLException 
	 */
	public int updateFhsInfo(Map<String, Object> params) throws SQLException {
		return mainDao.update("commonMapper.updateFhsInfo", params);
	}

	/**
	 * 농가코드로 통합회원코드 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> getFhsInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("commonMapper.getFhsInfo", params);
	}
	
	/**
	 * 통합회원정보 조회
	 * @param dataMap
	 * @return
	 * @throws SQLException 
	 */
	public Map<String, Object> getIntgNoInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("commonMapper.getIntgNoInfo", params);
	}
	
	/**
	 * 통합회원정보 조회 : 회원통합번호로 조회
	 * @param dataMap
	 * @return
	 * @throws SQLException 
	 */
	public Map<String, Object> getIntgNoInfoForNum(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("commonMapper.getIntgNoInfoForNum", params);
	}

	/**
	 * 통합회원정보 저장
	 * @param dataMap
	 * @throws SQLException 
	 */
	public int insertIntgInfo(Map<String, Object> params) throws SQLException {
		mainDao.insert("commonMapper.insertIntgInfo", params);
		return Integer.parseInt(params.get("MB_INTG_NO").toString());
	}

	/**
	 * 통합회원정보 수정
	 * @param dataMap
	 * @throws SQLException 
	 */
	public void updateIntgInfo(Map<String, Object> params) throws SQLException {
		mainDao.update("commonMapper.updateIntgInfo", params);
	}

	/**
	 * 등록된 형매정보 삭제
	 * @param params
	 * @throws SQLException 
	 */
	public void deleteIndvSibinf(Map<String, Object> params) throws SQLException {
		mainDao.delete("commonMapper.deleteIndvSibinf", params);
	}

	/**
	 * 형매정보 저장
	 * @param params
	 * @throws SQLException
	 */
	public void insertIndvSibinf(Map<String, Object> params) throws SQLException {
		mainDao.insert("commonMapper.insertIndvSibinf", params);
	}

	/**
	 * 등록된 후대정보 삭제
	 * @param params
	 * @throws SQLException 
	 */
	public void deleteIndvPostinf(Map<String, Object> params) throws SQLException {
		mainDao.delete("commonMapper.deleteIndvPostinf", params);
	}

	/**
	 * 후대정보 저장
	 * @param params
	 * @throws SQLException
	 */
	public void insertIndvPostinf(Map<String, Object> params) throws SQLException {
		mainDao.insert("commonMapper.insertIndvPostinf", params);
	}
	
	/**
	 * 회원통합정보 히스토리 저장
	 * @param params
	 * @throws SQLException
	 */
	public void insertMbintgHistoryData(Map<String, Object> params) throws SQLException{
		mainDao.insert("commonMapper.insertMbintgHistoryData", params);
	}

	/**
	 * 중도매인 이력 테이블에 저장
	 * @param params
	 * @throws SQLException
	 */
	public void insertMwmnHistoryData(Map<String, Object> params) throws SQLException{
		mainDao.insert("commonMapper.insertMwmnHistoryData", params);
	}
	
	/**
	 * 휴면백업 데이터 업데이트 (휴면백업 통합회원 데이터 -> 통합회원 원장 데이터로 update)
	 * @param params
	 * @throws SQLException
	 */
	public void updateDormMbintgBackupData(Map<String, Object> params) throws SQLException{
		mainDao.update("commonMapper.updateDormMbintgBackupData", params);
	}
	
	/**
	 * 휴면백업 데이터 업데이트 (휴면백업 중도매인 데이터 -> 중도매인 데이터로 update)
	 * @param params
	 * @throws SQLException
	 */
	public void updateDormMwmnBackupData(Map<String, Object> params) throws SQLException{
		mainDao.update("commonMapper.updateDormMwmnBackupData", params);
	}
	
	/**
	 * 휴면백업 데이터 업데이트 (휴면백업 출하주 데이터 -> 출하주 데이터로 update)
	 * @param params
	 * @throws SQLException
	 */
	public void updateDormFhsBackupData(Map<String, Object> params) throws SQLException{
		mainDao.update("commonMapper.updateDormFhsBackupData", params);
	}
	
	/**
	 * 회원통합 휴면 백업 데이터 삭제
	 * @param params
	 * @throws SQLException
	 */
	public void deleteDormMbintgBackupData(Map<String, Object> params) throws SQLException{
		mainDao.delete("commonMapper.deleteDormMbintgBackupData", params);
	}
	
	/**
	 * 중도매인 휴면 백업 데이터 삭제
	 * @param params
	 * @throws SQLException
	 */
	public void deleteDormMwmnBackupData(Map<String, Object> params) throws SQLException{
		mainDao.delete("commonMapper.deleteDormMwmnBackupData", params);
	}
	
	/**
	 * 출하주 휴면 백업 데이터 삭제
	 * @param params
	 * @throws SQLException
	 */
	public void deleteDormFhsBackupData(Map<String, Object> params) throws SQLException{
		mainDao.delete("commonMapper.deleteDormFhsBackupData", params);
	}

}
