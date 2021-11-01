package com.ishift.auction.configuration.listener;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.collections.map.HashedMap;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;


import lombok.extern.java.Log;

@Log
@WebListener
public class VisitSessionListenner implements HttpSessionListener{	
	@Override
	public void sessionCreated(HttpSessionEvent http) {
		
        HttpSession session = http.getSession();
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(session.getServletContext());
        
        //등록되어있는 빈을 사용할수 있도록 설정해준다
        HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        
        try {
        	String ip = req.getHeader("X-Forwarded-For");
        	
        	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        		ip = req.getHeader("Proxy-Client-IP");
        	}        	 
        	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        	    ip = req.getHeader("WL-Proxy-Client-IP");
        	}        	 
        	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        	    ip = req.getHeader("HTTP_CLIENT_IP");
        	}        	 
        	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        		ip = req.getHeader("HTTP_X_FORWARDED_FOR");
        	}        	 
        	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        	    ip = req.getRemoteAddr();
        	}
        	//log.info(" CLIENT IP : "+ip);
	        Map<String, Object> vo = new HashedMap();
	        vo.put("visit_ip", ip);
	        vo.put("visit_refer", req.getHeader("referer"));
	        vo.put("visit_agent", req.getHeader("User-Agent"));
	        //System.out.println(vo);
	        SqlSessionTemplate sqlSession = getSessionService(http);
	        sqlSession.insert("LoginMapper.insertVisitor",vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	@Override
	public void sessionDestroyed(HttpSessionEvent arg0){
	}
	private SqlSessionTemplate getSessionService(HttpSessionEvent se) {
		WebApplicationContext context = WebApplicationContextUtils
				.getWebApplicationContext(se.getSession().getServletContext());
		return (SqlSessionTemplate) context.getBean("sqlSessionTemplate");
	}
}
