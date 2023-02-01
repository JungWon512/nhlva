package com.ishift.auction.service.admin.task;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.AmazonS3Exception;

/**
 * 홈페이지 관리자 경매업무 서비스
 * @author iShift
 */
public interface AdminTaskService {

	/**
	 * 개체 정보를 조회한다.(한우종합)
	 * @param params
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	Map<String, Object> searchIndvAmnno(Map<String, Object> params) throws SQLException, Exception;

	/**
	 * 등록된 개체 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> selectIndvInfo(Map<String, Object> params) throws SQLException;

	/**
	 * 경매일자 조회(진행 또는 진행예정)
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectAucDtList(Map<String, Object> params) throws SQLException;

	/**
	 * 경매일자, 경매대상 체크
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> checkAucDt(Map<String, Object> params) throws SQLException;

	/**
	 * QCN 정보
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectQcnInfo(Map<String, Object> params) throws SQLException;

	/**
	 * 개체 + 출장우 정보 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> selectSogCowInfo(Map<String, Object> params) throws SQLException;

	/**
	 * 출장우 정보 저장
	 * @param params
	 * @return
	 * @throws SQLException
	 * @throws Exception 
	 */
	Map<String, Object> registSogCow(Map<String, Object> params) throws SQLException, Exception;

	/**
	 * 출장우 정보 저장
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	int insertSogCow(Map<String, Object> params) throws SQLException;

	/**
	 * 출장우 정보 수정
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	int updateSogCow(Map<String, Object> params) throws SQLException;

	/**
	 * 출장우 정보, 출장우 로그 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> searchSogCow(Map<String, Object> params) throws SQLException;

	/**
	 * 출장우 로그 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectSogCowLogList(Map<String, Object> params) throws SQLException;

	/**
	 * 변경한 경매내역 저장
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> updateResult(Map<String, Object> params) throws SQLException;

	/**
	 * 출장우 이미지 업로드
	 * @param params
	 * @return
	 * @throws SQLException 
	 * @throws KeyManagementException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyStoreException 
	 */
	Map<String, Object> uploadImageProc(Map<String, Object> params) throws SQLException, AmazonS3Exception, SdkClientException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException;

	/**
	 * 출장우 이미지 리스트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectCowImg(Map<String, Object> params) throws SQLException;

}
