<%@ page language='java' contentType='text/html; charset=UTF-8' pageEncoding='UTF-8'%>
<%@ include file='/WEB-INF/__system/taglibs.jsp'%>

<c:set var="URL" value="${pageContext.request.requestURL}" />
<div  class="hideFoot">
	<ul>
		<li>
			<a   href="#" onclick="window.open('/agreement/new', '이용약관', 'width=800, height=700, toolbar=no, menubar=no, scrollbars=no, resizable=yes' );return false;">이용약관</a>
<!-- 			<a href="javascript:pageMove('/agreement/new')"  id="1" name="a" value="a1">이용약관</a> --> 
		</li>
		<li>
			<a  href="#" onclick="window.open('/privacy', '개인정보 처리 방침', 'width=800, height=700, toolbar=no, menubar=no, scrollbars=no, resizable=yes' );return false;">개인정보 처리 방침</a>
		</li>
		<li>
			<a  href=''>ⓒ가축시장.kr</a>
		</li>
	</ul>
	<div class="co-infp"><a role="button" href="javascript:;" onclick="modalBzInfo()">농협경제지주 주식회사(축산경제) 사업자 정보</a></div>
</div>