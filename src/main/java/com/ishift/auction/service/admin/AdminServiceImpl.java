package com.ishift.auction.service.admin;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service("adminService")
public class AdminServiceImpl implements AdminService {
    @Resource(name = "adminDAO")
    AdminDAO adminDAO;

    @Override
    public Map<String,Object> selectOneJohap(Map<String, Object> map) throws SQLException{
    	return adminDAO.selectJohapOne(map);
    }
    
    @Override
    public Map<String,Object> selectOneNotice(Map<String, Object> map) throws SQLException{
    	return adminDAO.selectOneNotice(map);
    }
    
    @Override
    public int insertNotice(Map<String, Object> map) throws SQLException {
        return adminDAO.insertNotice(map);
    }

    @Override
    public List<Map<String,Object>> selectListNotice(Map<String, Object> vo) throws SQLException {
        return adminDAO.selectListNotice(vo);    	
    }

    @Override
    public int countNotice(Map<String, Object> vo) throws SQLException  {
        return adminDAO.countNotice(vo);    	
    }
    
    @Override
    public int updateNotice(Map<String, Object> vo) throws SQLException{
    	return adminDAO.updateNotice(vo);
    }
    
    @Override
    public int deleteNotice(Map<String, Object> map) throws SQLException{
    	return adminDAO.deleteNotice(map);
    }
    
    @Override
    public boolean existNotice(Map<String,Object> vo) throws SQLException {
        return adminDAO.countNotice(vo) > 0 ? true : false;
    }

    @Override
    public Map<String, Object> selectOneMaxVO(Map<String,Object> map) throws SQLException {
        return adminDAO.selectOneMaxVO( map);
    }
    
    @Override
    public List<Map<String,Object>> selectListVisit(Map<String, Object> map) throws SQLException{
    	return adminDAO.selectListVisit( map);
    }
    
    @Override
    public int selectVisitTotalCnt(Map<String, Object> map) throws SQLException{
    	return adminDAO.selectVisitTotalCnt(map);
    }
}