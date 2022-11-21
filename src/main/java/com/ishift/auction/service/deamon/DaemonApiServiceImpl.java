package com.ishift.auction.service.deamon;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

@Service
public class DaemonApiServiceImpl implements DaemonApiService {

	@Resource(name = "daemonApiDAO")
	DaemonApiDAO daemonApiDAO;

	@Override
	public List<Map<String, Object>> selectIndvs(Map<String, Object> params) throws SQLException {
		return daemonApiDAO.selectIndvs(params);
	}
}
