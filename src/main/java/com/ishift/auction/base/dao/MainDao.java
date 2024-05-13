package com.ishift.auction.base.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ishift.auction.util.SessionUtill;

@Repository
public class MainDao {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(MainDao.class);

    @Autowired
    @Qualifier("sqlSessionTemplate")
    private SqlSessionTemplate sqlSession;
    
    @Autowired
    private SessionUtill sessionUtill;

    public int insert(String queryID, Map<String, Object> map) throws SQLException {
        LOGGER.info("##### queryID : " + queryID);
        this.setDefaultValue(map);
        return sqlSession.insert(queryID, map);
    }

    public int insertVO(String queryID, Object object) throws SQLException {
        LOGGER.info("##### queryID : " + queryID);
        return sqlSession.insert(queryID, object);
    }

    public int insertBatch(String queryID, List<Map<String, Object>> list) throws SQLException {
        LOGGER.info("##### queryID : " + queryID);
        return sqlSession.insert(queryID, list);
    }
    
    public int insertBatchVO(String queryID, List<?> list) throws SQLException {
        LOGGER.info("##### queryID : " + queryID);
        return sqlSession.insert(queryID, list);
    }

    public List<Map<String,Object>> selectList(String queryID, Map<String, Object> map) throws SQLException {
        LOGGER.info("##### queryID : " + queryID);
        this.setDefaultValue(map);
        return sqlSession.selectList(queryID, map);
    }
    
    public List<?> selectListVO(String queryID, Object object) throws SQLException {
        LOGGER.info("##### queryID : " + queryID);
        return sqlSession.selectList(queryID, object);
    }

    public Map<String, Object> selectOne(String queryID, Map<String, Object> map) throws SQLException {
        LOGGER.info("##### queryID : " + queryID);
        this.setDefaultValue(map);
        return sqlSession.selectOne(queryID, map);
    }
    
    public Object selectOneVO(String queryID, Object object) throws SQLException {
        LOGGER.info("##### queryID : " + queryID);
        return sqlSession.selectOne(queryID, object);
    }
    

    public int update(String queryID, Map<String, Object> map) throws SQLException {
        LOGGER.info("##### queryID : " + queryID);
        this.setDefaultValue(map);
        return sqlSession.update(queryID, map);
    }
    
    public int updateVO(String queryID, Object object) throws SQLException {
        LOGGER.info("##### queryID : " + queryID);
        return sqlSession.update(queryID, object);
    }
    
    public int updateBatch(String queryID, List<Map<String, Object>> list) throws SQLException {
        LOGGER.info("##### queryID : " + queryID);
        return sqlSession.update(queryID, list);
    }

    public int updateBatchVO(String queryID, List<?> list) throws SQLException {
        LOGGER.info("##### queryID : " + queryID);
        return sqlSession.update(queryID, list);
    }
    
    public int delete(String queryID, Map<String, Object> map) throws SQLException {
        LOGGER.info("##### queryID : " + queryID);
        this.setDefaultValue(map);
        return sqlSession.delete(queryID, map);
    }
    
    public int deleteBatch(String queryID, List<?> list) throws SQLException {
        LOGGER.info("##### queryID : " + queryID);
        return sqlSession.delete(queryID, list);
    }
    
    public int deleteVO(String queryID, Object object) throws SQLException {
        LOGGER.info("##### queryID : " + queryID);
        return sqlSession.delete(queryID, object);
    }
    
    public int deleteBatchVO(String queryID, List<?> list) throws SQLException {
        LOGGER.info("##### queryID : " + queryID);
        return sqlSession.delete(queryID, list);
    }
    
    public int count(String queryID, Map<String, Object> map) throws SQLException {
        LOGGER.info("##### queryID : " + queryID);
        this.setDefaultValue(map);
        return sqlSession.selectOne(queryID, map);
    }
    
    public int countVO(String queryID, Object object) throws SQLException {
        LOGGER.info("##### queryID : " + queryID);
        return sqlSession.selectOne(queryID, object);
    }
    
    public void setDefaultValue(Map<String, Object> map) {
        map.put("regUsrid", map.getOrDefault("regUsrid", sessionUtill.getUserId()));
    	map.put("uptUsrid", map.getOrDefault("uptUsrid", sessionUtill.getUserId()));
    	map.put("lsCmeno", sessionUtill.getUserId());
    	map.put("authRole", map.getOrDefault("authRole", sessionUtill.getRoleConfirm()));
    	map.put("naBzplc", map.getOrDefault("naBzplc", sessionUtill.getNaBzplc()));
    }
}
