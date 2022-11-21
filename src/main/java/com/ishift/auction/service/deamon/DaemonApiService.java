package com.ishift.auction.service.deamon;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DaemonApiService {

	/**
	 * 개체 리스트
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	List<Map<String, Object>> selectIndvs(Map<String, Object> params) throws SQLException;

}
