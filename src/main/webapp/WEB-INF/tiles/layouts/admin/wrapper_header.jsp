<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>
<link href="/static/assets/css/themes/layout/header/base/dark.css" rel="stylesheet" type="text/css" />
<link href="/static/assets/css/themes/layout/header/menu/dark.css" rel="stylesheet" type="text/css" />

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <a class="navbar-brand" href="/admin/main">NH 가축시장</a>
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarToggleExternalContent" aria-controls="navbarToggleExternalContent" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
  </button>

  <div class="collapse navbar-collapse" id="navbarToggleExternalContent">
	<c:set var="uri" value="${requestScope['javax.servlet.forward.servlet_path']}" />
    <ul class="navbar-nav mr-auto mt-2 mt-lg-0">
<!-- 		<li class="nav-item"> -->
<%-- 			<a class="nav-link ${fn:indexOf(uri, '/admin/task/') > -1 ? 'active' : ''}" href="/admin/task/main">경매업무</a>  --%>
<!-- 		</li> -->
		<li class="nav-item"> 
			<a class="nav-link ${uri eq '/admin/broad/cast' ? 'active' : ''}" href="/admin/broad/cast">영상송출</a> 
		</li>
		<li class="nav-item">
			<a class="nav-link ${uri eq '/admin/auction/monster' ? 'active' : ''}" href="/admin/auction/monster">모니터링</a> 
		</li>
		<li class="nav-item">
			<a class="nav-link ${uri eq '/admin/auction/board' ? 'active' : ''}" href="/admin/auction/board">멀티비젼</a> 
		</li>
		<li class="nav-item">
			<a class="nav-link ${uri eq '/admin/auction/aucNotice' ? 'active' : ''}" href="/admin/auction/aucNotice">공지사항</a> 
		</li>
		<sec:authorize access="!isAnonymous()"> 
			<li class="nav-item"> 
				<a class="nav-link" href="javascript:pageMove('/admin/user/login');">로그아웃</a> 
			</li>
		</sec:authorize>
	</ul>
  </div>
</nav>