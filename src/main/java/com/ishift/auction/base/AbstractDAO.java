package com.ishift.auction.base;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Map;

/**
 * @Generated by http://generator.tirablue.com
 * @User: photoprogrammer@gmail.com
 * @Date: 2021-02-02 11:24:55.308
 * @Description:
 **/
@Deprecated
public abstract class AbstractDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDAO.class);

    @Autowired
    @Qualifier("sqlSessionTemplate")
    private SqlSessionTemplate sqlSession;

    public int insert(String queryID, Map<String, Object> map) throws Exception {
        LOGGER.info("##### queryID : " + queryID);
        return sqlSession.insert(queryID, map);
    }

    public int insertVO(String queryID, Object object) throws Exception {
        LOGGER.info("##### queryID : " + queryID);
        return sqlSession.insert(queryID, object);
    }

    public int insertBatch(String queryID, List<Map<String, Object>> list) throws Exception {
        LOGGER.info("##### queryID : " + queryID);
        return sqlSession.insert(queryID, list);
    }
    
    public int insertBatchVO(String queryID, List<?> list) throws Exception {
        LOGGER.info("##### queryID : " + queryID);
        return sqlSession.insert(queryID, list);
    }

    public List<Map<String,Object>> selectList(String queryID, Map<String, Object> map) throws Exception {
        LOGGER.info("##### queryID : " + queryID);
        return sqlSession.selectList(queryID, map);
    }
    
    public List<?> selectListVO(String queryID, Object object) throws Exception {
        LOGGER.info("##### queryID : " + queryID);
        return sqlSession.selectList(queryID, object);
    }

    public Map<String, Object> selectOne(String queryID, Map<String, Object> map) throws Exception {
        LOGGER.info("##### queryID : " + queryID);
        return sqlSession.selectOne(queryID, map);
    }
    
    public Object selectOneVO(String queryID, Object object) throws Exception {
        LOGGER.info("##### queryID : " + queryID);
        return sqlSession.selectOne(queryID, object);
    }
    

    public int update(String queryID, Map<String, Object> map) throws Exception {
        LOGGER.info("##### queryID : " + queryID);
        return sqlSession.update(queryID, map);
    }
    
    public int updateVO(String queryID, Object object) throws Exception {
        LOGGER.info("##### queryID : " + queryID);
        return sqlSession.update(queryID, object);
    }
    
    public int updateBatch(String queryID, List<Map<String, Object>> list) throws Exception {
        LOGGER.info("##### queryID : " + queryID);
        return sqlSession.update(queryID, list);
    }

    public int updateBatchVO(String queryID, List<?> list) throws Exception {
        LOGGER.info("##### queryID : " + queryID);
        return sqlSession.update(queryID, list);
    }
    
    public int delete(String queryID, Map<String, Object> map) throws Exception {
        LOGGER.info("##### queryID : " + queryID);
        return sqlSession.delete(queryID, map);
    }
    
    public int deleteBatch(String queryID, List<?> list) throws Exception {
        LOGGER.info("##### queryID : " + queryID);
        return sqlSession.delete(queryID, list);
    }
    
    public int deleteVO(String queryID, Object object) throws Exception {
        LOGGER.info("##### queryID : " + queryID);
        return sqlSession.delete(queryID, object);
    }
    
    public int deleteBatchVO(String queryID, List<?> list) throws Exception {
        LOGGER.info("##### queryID : " + queryID);
        return sqlSession.delete(queryID, list);
    }
    
    public int count(String queryID, Map<String, Object> map) throws Exception {
        LOGGER.info("##### queryID : " + queryID);
        return sqlSession.selectOne(queryID, map);
    }
    
    public int countVO(String queryID, Object object) throws Exception {
        LOGGER.info("##### queryID : " + queryID);
        return sqlSession.selectOne(queryID, object);
    }
}