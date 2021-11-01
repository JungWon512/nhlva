package com.ishift.auction.service.login;

import com.ishift.auction.base.dao.MainDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("loginDAO")
public class LoginDAO {
	
	@Autowired
	private MainDao mainDao;

	/**
	 * 중도매인 리스트 검색
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectWholesalerList(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> returnList = null;
		
		return mainDao.selectList("LoginMapper.selectWholesalerList", params);
	}

	/**
	 * 중도매인 정보
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectWholesaler(Map<String, Object> params) throws Exception {
		Map<String, Object> returnMap = null;
				
		int place = Integer.parseInt(params.getOrDefault("naBzplcno", "0").toString());
		try {
			returnMap = mainDao.selectOne("LoginMapper.selectWholesaler", params);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return returnMap;
	}

	/**
	 * 방문로그 저장
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int insertVisitor(Map<String, Object> vo) throws Exception {
		return mainDao.insert("LoginMapper.insertVisitor", vo);
	}

	/**
	 * 로그인 출하주 검색
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectFarmUserList(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> returnList = null;
						
		int place = Integer.parseInt(params.getOrDefault("place", "0").toString());
		return mainDao.selectList("LoginMapper.selectFarmUserList", params);
	}

	/**
	 * 조합코드(NA_BZPLC)와 농가 식별번호(FHS_ID_NO)로 출하주 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectFarmUser(Map<String, Object> params) throws Exception {
		Map<String, Object> returnMap = null;
				
		int place = Integer.parseInt(params.getOrDefault("naBzplcno", "0").toString());
		return mainDao.selectOne("LoginMapper.selectFarmUser", params);
	}

}