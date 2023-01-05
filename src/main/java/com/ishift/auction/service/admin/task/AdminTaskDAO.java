package com.ishift.auction.service.admin.task;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ishift.auction.base.dao.MainDao;

@Repository("adminTaskDAO")
public class AdminTaskDAO {

	@Autowired
	private MainDao mainDao;

	/**
	 * 경매일자에 등록된 개체조회
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	public List<Map<String, Object>> selectDuplList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("AdminTaskMapper.selectDuplList", params);
	}

	/**
	 * 조합에 등록된 개체 조회
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	public Map<String, Object> selectIndvInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("AdminTaskMapper.selectIndvInfo", params);
	}

	/**
	 * 경매일자 조회(진행 또는 진행예정)
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectAucDtList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("AdminTaskMapper.selectAucDtList", params);
	}

	/**
	 * 경매정보
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectQcnInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("AdminTaskMapper.selectQcnInfo", params);
	}

	/**
	 * 개체 + 출장우 정보 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> selectSogCowInfo(Map<String, Object> params) throws SQLException {
		return  mainDao.selectOne("AdminTaskMapper.selectSogCowInfo", params);
	}

	/**
	 * 출장우 정보 등록
	 * @param params
	 * @throws SQLException
	 */
	public int insertSogCow(Map<String, Object> params) throws SQLException {
		return mainDao.insert("AdminTaskMapper.insertSogCow", params);
	}
	
	/**
	 * 출장우 정보 수정
	 * @param params
	 * @throws SQLException
	 */
	public int updateSogCow(Map<String, Object> params) throws SQLException {
		return mainDao.update("AdminTaskMapper.updateSogCow", params);
	}
	
	/**
	 * 출장우 정보 로그 저장
	 * @param params
	 * @throws SQLException
	 */
	public void insertSogCowLog(Map<String, Object> params) throws SQLException {
		mainDao.insert("AdminTaskMapper.insertSogCowLog", params);
	}

	/**
	 * 출장우 정보 로그 조회
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	public List<Map<String, Object>> selectSogCowLogList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("AdminTaskMapper.selectSogCowLogList", params);
	}

	/**
	 * 낙찰자 이름, 생년월일로 등록된 중도매인 정보가 있는지 확인
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> selectMwmnInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("AdminTaskMapper.selectMwmnInfo", params);
	}

	/**
	 * 경매 참가 정보 등록
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int insertAucEntr(Map<String, Object> params) throws SQLException {
		return mainDao.insert("AdminTaskMapper.insertAucEntr", params);
	}

	/**
	 * 경매 참가 정보 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> selectAucEntrInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("AdminTaskMapper.selectAucEntrInfo", params);
	}

	/**
	 * 경매번호 중복 카운트
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	public int checkAucPrgSq(Map<String, Object> params) throws SQLException {
		return mainDao.count("AdminTaskMapper.checkAucPrgSq", params);
	}
	
	/**
	 * 출장우 이미지 저장
	 * @param params
	 * @throws SQLException
	 */
	public void insertCowImg(Map<String, Object> params) throws SQLException {
		mainDao.insert("AdminTaskMapper.insertCowImg", params);
	}

	/**
	 * 출장우 이미지 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectCowImg(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("AdminTaskMapper.selectCowImg", params);
	}

	/**
	 * 출장우 이미지 삭제
	 * @param params
	 * @throws SQLException
	 */
	public void deleteCowImg(Map<String, Object> params) throws SQLException {
		mainDao.delete("AdminTaskMapper.deleteCowImg", params);
	}

}
