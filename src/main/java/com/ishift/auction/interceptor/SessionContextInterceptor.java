package com.ishift.auction.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ishift.auction.service.admin.AdminService;
import com.ishift.auction.util.Constants;
import com.ishift.auction.util.CookieUtil;
import com.ishift.auction.util.JwtTokenUtil;
import com.ishift.auction.vo.JwtTokenVo;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
public class SessionContextInterceptor implements HandlerInterceptor {

	@Autowired
	private CookieUtil cookieUtil;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private AdminService adminService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    	String place = request.getParameter("place");
    	String watchToken = cookieUtil.getCookieValue(request, "watch_token");
    	
			
    	if(place != null && !"".equals(place)) {			
            Map<String,Object> map = new HashMap<>();
            map.put("naBzPlcNo", place);
            
        	final Map<String, Object> johapData = adminService.selectOneJohap(map);
			
        	String naBzplc = "";
			if(watchToken !=null && !"".equals(watchToken)){
				naBzplc = jwtTokenUtil.getValue(watchToken,"auctionHouseCode");				
			}
        	if(watchToken ==null || "".equals(watchToken) || !naBzplc.equals(johapData.get("NA_BZPLC"))){
				JwtTokenVo jwtTokenVo = JwtTokenVo.builder()
						.auctionHouseCode(johapData.get("NA_BZPLC").toString())
						.userMemNum("WATCHER")
						.userRole(Constants.UserRole.WATCHER)
						.build();
				String token = jwtTokenUtil.generateToken(jwtTokenVo, Constants.JwtConstants.ACCESS_TOKEN);
				Cookie cookie = cookieUtil.createCookie("watch_token", token);
				response.addCookie(cookie);
			}

    	}
    	
    	
        request.setAttribute("start",System.currentTimeMillis());
        if(request.getSession()!=null && request.getSession().getAttribute("country")!=null) {
            request.setAttribute("country", request.getSession().getAttribute("country"));
        }

        if(!"/user/login".equals(request.getRequestURI()) && !"/error".equals(request.getRequestURI())) {
            request.setAttribute("returnUrl", request.getRequestURI());
        }
        return true;


    }

}
