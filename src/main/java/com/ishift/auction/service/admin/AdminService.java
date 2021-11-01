package com.ishift.auction.service.admin;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository("adminService")
public interface AdminService {

	Map<String,Object> selectOneJohap(Map<String, Object> map) throws Exception;

	Map<String,Object> selectOneNotice(Map<String, Object> map) throws Exception;

	int insertNotice(Map<String, Object> map) throws Exception;

	List<Map<String,Object>> selectListNotice(Map<String, Object> vo) throws Exception;

	int countNotice(Map<String, Object> vo) throws Exception;

	int updateNotice(Map<String, Object> vo) throws Exception;

	int deleteNotice(Map<String, Object> map) throws Exception;

	boolean existNotice(Map<String, Object> vo) throws Exception;

	Map<String, Object> selectOneMaxVO(Map<String,Object> map) throws Exception;

	List<Map<String,Object>> selectListVisit(Map<String, Object> map) throws Exception;

	int selectVisitTotalCnt(Map<String, Object> map) throws Exception;
}
