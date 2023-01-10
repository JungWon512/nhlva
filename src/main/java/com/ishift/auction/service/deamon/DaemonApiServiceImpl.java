package com.ishift.auction.service.deamon;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DaemonApiServiceImpl implements DaemonApiService {

	@Resource(name = "daemonApiDAO")
	private DaemonApiDAO daemonApiDAO;

	/**
	 * TB_LA_IS_MH_AUC_QCN : 경매 차수 정보
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> selectAucQcnList(Map<String, Object> params) throws SQLException {
		return daemonApiDAO.selectAucQcnList(params);
	}

	/**
	 * TB_LA_IS_MH_SOG_COW : 출장우 정보 리스트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> selectSogCowList(Map<String, Object> params) throws SQLException {
		return daemonApiDAO.selectSogCowList(params);
	}

	/**
	 * TB_LA_IS_MH_CALF : 송아지 정보 리스트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> selectCalfList(Map<String, Object> params) throws SQLException {
		return daemonApiDAO.selectCalfList(params);
	}

	/**
	 * TB_LA_IS_MM_INDV : 개체 정보 리스트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> selectIndvList(Map<String, Object> params) throws SQLException {
		return daemonApiDAO.selectIndvList(params);
	}

	/**
	 * TB_LA_IS_MH_AUC_ENTR : 경매 참가자 정보 리스트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> selectAucEntrList(Map<String, Object> params) throws SQLException {
		return daemonApiDAO.selectAucEntrList(params);
	}

	/**
	 * TB_LA_IS_MH_FEE : 경매 수수료 정보 리스트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> selectFeeList(Map<String, Object> params) throws SQLException {
		return daemonApiDAO.selectFeeList(params);
	}

	/**
	 * TB_LA_IS_MM_FHS : 출하주 정보 리스트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> selectFhsList(Map<String, Object> params) throws SQLException {
		return daemonApiDAO.selectFhsList(params);
	}

	/**
	 * TB_LA_IS_MM_MWMN : 중도매인 정보 리스트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> selectMwmnList(Map<String, Object> params) throws SQLException {
		return daemonApiDAO.selectMwmnList(params);
	}

	/**
	 * TB_LA_IS_MH_SOG_COW : 출장우 낙/유찰 정보 저장
	 * @param params
	 * @return
	 */
	@Override
	public Map<String, Object> updSogcow(Map<String, Object> params) throws SQLException {
		final Map<String, Object> rtnMap = new HashMap<String, Object>();
		final List<Map<String, Object>> list = this.createListRequestParam(params);
		if (list == null || list.size() == 0) return null;
		
		int updateNum = 0;
		for (Map<String, Object> info : list) {
			updateNum += daemonApiDAO.updSogcow(info);
		}
		
		rtnMap.put("updateNum", updateNum);
		return rtnMap;
	}

	/**
	 * TB_LA_IS_MH_FEE_IMPS : 낙/유찰 수수료 정보 저장
	 * @param params
	 * @return
	 */
	@Override
	public Map<String, Object> updFeeImps(Map<String, Object> params) throws SQLException {
		final Map<String, Object> rtnMap = new HashMap<String, Object>();
		final List<Map<String, Object>> list = this.createListRequestParam(params);
		if (list == null || list.size() == 0) return null;
		
		int updateNum = 0;
		for (Map<String, Object> info : list) {
			updateNum += daemonApiDAO.updFeeImps(info);
		}
		
		rtnMap.put("updateNum", updateNum);
		return rtnMap;
	}

	/**
	 * TB_LA_IS_MH_ATDR_LOG : 응찰 로그 저장
	 * @param params
	 * @return
	 */
	@Override
	public Map<String, Object> insAtdrLog(Map<String, Object> params) throws SQLException {
		final Map<String, Object> rtnMap = new HashMap<String, Object>();
		final List<Map<String, Object>> list = this.createListRequestParam(params);
		if (list == null || list.size() == 0) return null;
		
		int insertNum = 0;
		for (Map<String, Object> info : list) {
			insertNum += daemonApiDAO.insAtdrLog(info);
		}
		
		rtnMap.put("updateNum", insertNum);
		return rtnMap;
	}

	/**
	 * 요청 파라이터 list로 변환
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> createListRequestParam(Map<String, Object> params) {
		if (params.get("data") instanceof List) {
			return (List<Map<String, Object>>)params.get("data");
		}
		
		if (params.get("data") instanceof Map) {
			List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();
			rtnList.add((Map<String, Object>)params.get("data"));
			return rtnList;
		}
		return null;
	}

	/**
	 * TB_LA_IS_MH_FEE_IMPS : 낙/유찰 수수료 정보 리스트(테스트용)
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	@Override
	public List<Map<String, Object>> selectFeeImpsList(Map<String, Object> params) throws SQLException {
		return daemonApiDAO.selectFeeImpsList(params);
	}

	/**
	 * TB_LA_IS_MM_ATDR_LOG : 응찰 정보 리스트(테스트용)
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	@Override
	public List<Map<String, Object>> selectAtdrLogList(Map<String, Object> params) throws SQLException {
		return daemonApiDAO.selectAtdrLogList(params);
	}

}
