package com.ishift.auction.service.deamon;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ishift.auction.base.dao.MainDao;

@Repository("daemonApiDAO")
public class DaemonApiDAO {
	
	@Autowired
	private MainDao mainDao;

	public List<Map<String, Object>> selectIndvs(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("DaemonApiMapper.selectIndvs", params);
	}

}
