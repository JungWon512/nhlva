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
	
	/**
	 * TB_LA_IS_MH_AUC_QCN : 경매 차수 정보
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectAucQcnList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("DaemonApiMapper.selectAucQcnList", params);
	}

	/**
	 * TB_LA_IS_MH_SOG_COW : 출장우 정보 리스트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectSogCowList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("DaemonApiMapper.selectSogCowList", params);
	}

	/**
	 * TB_LA_IS_MH_CALF : 송아지 정보 리스트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectCalfList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("DaemonApiMapper.selectCalfList", params);
	}

	/**
	 * TB_LA_IS_MM_INDV : 개체 정보 리스트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectIndvList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("DaemonApiMapper.selectIndvList", params);
	}

	/**
	 * TB_LA_IS_MM_INDV : 개별 개체 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> selectIndvInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("DaemonApiMapper.selectIndvInfo", params);
	}
	
	/**
	 * TB_LA_IS_MH_AUC_ENTR : 경매 참가자 정보 리스트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectAucEntrList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("DaemonApiMapper.selectAucEntrList", params);
	}

	/**
	 * TB_LA_IS_MH_FEE : 경매 수수료 정보 리스트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectFeeList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("DaemonApiMapper.selectFeeList", params);
	}

	/**
	 * TB_LA_IS_MM_FHS : 출하주 정보 리스트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectFhsList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("DaemonApiMapper.selectFhsList", params);
	}

	/**
	 * TB_LA_IS_MM_MWMN : 중도매인 정보 리스트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectMwmnList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("DaemonApiMapper.selectMwmnList", params);
	}

	/**
	 * TB_LA_IS_MH_SOG_COW : 출장우 낙/유찰 정보 저장
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	public int updSogcow(Map<String, Object> params) throws SQLException {
		return mainDao.update("DaemonApiMapper.updSogcow", params);
	}
	
	/**
	 * TB_LA_IS_MH_FEE_IMPS : 낙/유찰 수수료 정보 저장
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	public int updFeeImps(Map<String, Object> params) throws SQLException {
		return mainDao.update("DaemonApiMapper.updFeeImps", params);
	}

	/**
	 * TB_LA_IS_MH_ATDR_LOG : 응찰 로그 저장
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	public int insAtdrLog(Map<String, Object> params) throws SQLException {
		return mainDao.update("DaemonApiMapper.insAtdrLog", params);
	}

	/**
	 * TB_LA_IS_MH_FEE_IMPS : 낙/유찰 수수료 정보 리스트(테스트용)
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	public List<Map<String, Object>> selectFeeImpsList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("DaemonApiMapper.selectFeeImpsList", params);
	}

	/**
	 * TB_LA_IS_MM_ATDR_LOG : 응찰 정보 리스트(테스트용)
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	public List<Map<String, Object>> selectAtdrLogList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("DaemonApiMapper.selectAtdrLogList", params);
	}

}
