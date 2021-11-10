package com.ishift.auction.service.admin;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ishift.auction.base.dao.MainDao;

@Repository("adminDAO")
public class AdminDAO {
	
	@Autowired
	private MainDao mainDao;

	public Map<String, Object> selectJohapOne(Map<String, Object> map) throws SQLException {
		return mainDao.selectOne("admin.selectOneJohap", map);
	}

	public Map<String, Object> selectOneNotice(Map<String, Object> map) throws SQLException {
		return mainDao.selectOne("admin.selectOne", map);
	}

	public int insertNotice(Map<String, Object> map) throws SQLException {
		return mainDao.insert("admin.insertNotice", map);
	}

	public int countNotice(Map<String, Object> vo) throws SQLException  {
		return mainDao.count("admin.countNotice", vo);
	}

	public int updateNotice(Map<String, Object> vo) throws SQLException {
		return mainDao.update("admin.updateNotice", vo);
	}

	public int deleteNotice(Map<String, Object> map) throws SQLException {
        return mainDao.delete("admin.deleteNotice", map);
	}
	public Map<String, Object> selectOneMaxVO(Map<String,Object> map) throws SQLException {
		return mainDao.selectOne("admin.selectOneMaxNotice", map);
	}
	
	public List<Map<String, Object>> selectListNotice(Map<String, Object> vo) throws SQLException  {
		return mainDao.selectList("admin.selectListNotice", vo);
	}

	public List<Map<String, Object>> selectListVisit(Map<String, Object> map) throws SQLException {
		return mainDao.selectList("admin.selectListVisit", map);
	}

	public int selectVisitTotalCnt(Map<String, Object> map) throws SQLException {
		return mainDao.count("admin.selectVisitTotalCnt", map);
	}



}
