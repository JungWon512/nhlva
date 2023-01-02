package com.ishift.auction.service.admin.task;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface AdminTaskService {
	
	/**
	 * 출장우 정보 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> selectSogCowInfo(Map<String, Object> params) throws SQLException;

	/**
	 * 출장우 이미지 업로드
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	Map<String, Object> uploadImageProc(Map<String, Object> params) throws SQLException;

	/**
	 * 출장우 이미지 리스트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectCowImg(Map<String, Object> params) throws SQLException;


}
