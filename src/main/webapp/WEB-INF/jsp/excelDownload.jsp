<%@ page contentType="application/vnd.ms-excel; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.net.URLDecoder"%>

<%
	//파일명에 다운로드 날짜 붙여주기 위해 작성
	final Date date = new Date();
	final SimpleDateFormat dayformat = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
	final SimpleDateFormat hourformat = new SimpleDateFormat("hhmmss", Locale.KOREA);
	final String day = dayformat.format(date);
	final String hour = hourformat.format(date);
	final String title = request.getParameter("title");
	final String fileName = title + "_" + day + "_" + hour;
	final String html = request.getParameter("tableHtml");
	final String dHtml = java.net.URLDecoder.decode(html, "UTF-8");
	
	//필수 선언 부분
	response.setHeader("Content-Disposition", "attachment; filename="+new String((fileName).getBytes("KSC5601"),"8859_1")+".xls");
	response.setHeader("Content-Description", "JSP Generated Date");

%>
<!document html>
<html lang="ko">
	<body>
<%= dHtml %>
	</body>
</html>
 