<%@ page language='java' contentType='text/html; charset=UTF-8' pageEncoding='UTF-8'%>
<%@ include file='/WEB-INF/__system/taglibs.jsp'%>

<c:set var="URL" value="${pageContext.request.requestURL}" />
<div>
	<ul>
		<li>
			<a href="#" onclick="window.open('/agreement', '이용약관', 'width=800, height=700, toolbar=no, menubar=no, scrollbars=no, resizable=yes' );return false;">이용약관</a>
		</li>
		<li>
			<a href="#" onclick="window.open('/privacy', '개인정보 처리 방침', 'width=800, height=700, toolbar=no, menubar=no, scrollbars=no, resizable=yes' );return false;">개인정보 처리 방침</a>
		</li>
		<li>
			<a href=''>ⓒ가축시장.kr</a>
		</li>
	</ul>
</div>