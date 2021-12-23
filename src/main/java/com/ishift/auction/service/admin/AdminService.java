package com.ishift.auction.service.admin;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository("adminService")
public interface AdminService {

	Map<String,Object> selectOneJohap(Map<String, Object> map) throws SQLException;

	Map<String,Object> selectOneNotice(Map<String, Object> map) throws SQLException;

	int insertNotice(Map<String, Object> map) throws SQLException;

	List<Map<String,Object>> selectListNotice(Map<String, Object> vo) throws SQLException;

	int countNotice(Map<String, Object> vo) throws SQLException;

	int updateNotice(Map<String, Object> vo) throws SQLException;

	int deleteNotice(Map<String, Object> map) throws SQLException;

	boolean existNotice(Map<String, Object> vo) throws SQLException;

	Map<String, Object> selectOneMaxVO(Map<String,Object> map) throws SQLException;

	List<Map<String,Object>> selectListVisit(Map<String, Object> map) throws SQLException;

	int selectVisitTotalCnt(Map<String, Object> map) throws SQLException;

	int selectBidderCnt(Map<String, Object> param) throws SQLException;
}
