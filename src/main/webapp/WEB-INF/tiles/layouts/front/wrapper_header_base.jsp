<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>
<!--begin::Header Menu-->
<c:set var="requestPath" value="${requestScope['javax.servlet.forward.request_uri']}"/> 
<div class="w_header_inner">
	<div class="w_header ${requestPath eq '/home' or requestPath eq '/district' or requestPath eq '/schedule' or fn:contains( requestPath, 'dashboard') ? 'has-main-tab' :''}">
		<a href="javascript:pageMove('/home');" class="logo">
			<h1>
				<span>농협경제지주</span>
				가축시장.kr
			</h1>
		</a>
		<c:choose>
			<c:when test="${requestPath eq '/home' or requestPath eq '/district' or requestPath eq '/schedule' or fn:contains( requestPath, 'dashboard')}">
				<div class="side_menu">
					<ul>
						<li class="home"><a href="javascript:pageMove('/home');">지역선택</a></li>
						<li class="guide"><a href="javascript:pageMove('/guide');">이용안내</a></li>
					</ul>
				</div>
				<c:if test ="${requestPath eq '/home' or requestPath eq '/district' or requestPath eq '/schedule' or  fn:contains( requestPath, 'dashboard')}">
					<div class="main-tab-pc">
						<ul>
							<li class="${(requestPath eq '/home' or requestPath eq '/district' or requestPath eq '/schedule') ? 'on' :''}">
								<a href="javascript:pageMove('/home');">경매<span class="sub-txt">인터넷 <br>스마트 경매</span></a>
							</li>
							<li class="${fn:contains( requestPath, 'dashboard') ? 'on' :''}">
								<a href="javascript:pageMove('/dashboard/main');">현황<span class="sub-txt">전국 <br>가축시장현황</span></a>
							</li>
						</ul>
					</div>
				</c:if>
			</c:when>
			<c:when test="${requestPath eq '/schedule' or  fn:contains( requestPath, 'agreement') }">
			</c:when>
			<c:when test="${param.aucYn eq 'N'}">
				<div class="side_menu">
					<ul>
						<li class="home"><a href="javascript:pageMove('/home');">지역선택</a></li>
						<c:if test="${requestPath ne '/user/login'}">
							<sec:authorize access="isAnonymous()">
								<li class="login"><a href="javascript:pageMove('/user/login');">로그인</a></li>
							</sec:authorize>
							<sec:authorize access="!isAnonymous()">
								<li class="login"><a href="javascript:pageMove('/my/info');">나의정보</a></li>
								<li class="login"><a href="javascript:logoutProc();">로그아웃</a></li>
							</sec:authorize>
						</c:if>
						<li class="notice"><a href="javascript:pageMove('/main');">공지사항</a></li>
						<li class="guide"><a href="javascript:pageMove('/main');">이용안내</a></li>
					</ul>
				</div>
				<!-- //side_menu e -->
				<nav id="nav" style="">
					<ul>
						<li class="watch">
							<a href="javascript:pageMove('/main');">경매관전</a>
						</li>
						<li class="auction">
							<a href="javascript:pageMove('/main');">경매응찰</a>
						</li>
						<li class="sales">
							<a href="javascript:pageMove('/main');">출장우 조회</a>
						</li>
						<li class="results">
							<a href="javascript:pageMove('/main');">경매결과 조회</a>
						</li>
						<li class="myBuy">
							<a href="javascript:pageMove('/main');">나의 경매내역</a>
						</li>
						<li class="myEntry">
							<a href="javascript:pageMove('/main');">나의 출장우</a>
						</li>
						<li class="calendar">
							<a href="javascript:pageMove('/calendar');">경매일정</a>
						</li>
					</ul>
				</nav>
			</c:when>
			<c:otherwise>
				<div class="side_menu">
					<ul>
						<li class="home"><a href="javascript:pageMove('/home');">지역선택</a></li>
						<c:if test="${requestPath ne '/user/login'}">
							<sec:authorize access="isAnonymous()">
								<li class="login"><a href="javascript:pageMove('/user/login');">로그인</a></li>
							</sec:authorize>
							<sec:authorize access="!isAnonymous()">
								<li class="login"><a href="javascript:pageMove('/my/info');">나의정보</a></li>
								<li class="login"><a href="javascript:logoutProc();">로그아웃</a></li>
							</sec:authorize>
						</c:if>
						<li class="notice"><a href="javascript:pageMove('/notice');">공지사항</a></li>
						<li class="guide"><a href="javascript:pageMove('/guide');">이용안내</a></li>
					</ul>
				</div>
				<!-- //side_menu e -->
				<nav id="nav">
					<ul>
						<li class="watch">
							<a href="javascript:goWatchApp();">경매관전</a>
						</li>
						<li class="auction">
							<a href="javascript:pageMove('/main');">경매응찰</a>
						</li>
						<li class="sales">
							<a href="javascript:pageMove('/sales');">출장우 조회</a>
						</li>
						<li class="results">
							<a href="javascript:pageMove('/results');">경매결과 조회</a>
						</li>
						<li class="myBuy">
							<a href="javascript:pageMove('/my/buy');">나의 경매내역</a>
						</li>
						<li class="myEntry">
							<a href="javascript:pageMove('/my/entry');">나의 출장우</a>
						</li>
						<li class="calendar">
							<a href="javascript:pageMove('/calendar');">경매일정</a>
						</li>
					</ul>
				</nav>
			</c:otherwise>
		</c:choose>		
		<!-- //nav e -->
	</div>
	<!-- //w_header e -->
</div>
<!-- //w_header_inner e -->

<c:choose>
	<c:when test="${dashheaderTitle ne '' && dashheaderTitle ne null }"> 
	<div class="m_header ver-board">
		<div class="m_backTit">
			<a href="javascript:;" class="m_back"><h2 class="m_tit">${dashheaderTitle}</h2></a>				
		</div>
	</div>
	</c:when>
	<c:otherwise>
	<div class="m_header" style="<c:if test="${subheaderTitle eq '' || subheaderTitle eq null }"> display: none;</c:if>">
		<c:choose>
			<c:when test="${subheaderTitle eq '' || subheaderTitle eq null }">
				<h1 class="m_logo">
					<span>온라인 스마트 경매</span>
					가축시장.kr
				</h1>
			</c:when>
			<c:otherwise>
				<div class="m_backTit">
					<a href="javascript:;" class="m_back"><h2 class="m_tit">${subheaderTitle}</h2></a>				
				</div>
				<c:if test="${requestPath eq '/bid'}">
					<p class="join-box">참가번호:<b class="join-num"><span></span>번</b></p>
				</c:if>
				<c:if test="${(requestPath eq '/watch' or requestPath eq '/bid')}">
					<a href="javascript:;" class="m_sound fix_right off">소리</a>
				</c:if>
			</c:otherwise>
		</c:choose>
		<c:if test="${requestPath ne '/bid' and requestPath ne '/home' and requestPath ne '/district' and requestPath ne '/schedule' and requestPath ne '/my/info' 
														and !fn:contains( requestPath, 'agreement') }">
			<a href="#menu-lnb" class="m_menu">메뉴</a>
		</c:if>
	</div>
	</c:otherwise>
</c:choose>
<!-- //m_header e -->
<c:if test="${requestPath ne '/home' and requestPath ne '/district' and requestPath ne '/schedule'}">
	<nav id="menu-lnb">
		<ul>
			<li class="lnb_box">
				<div class="lnb_base">
					<a href="javascript:;" class="nav_close">close</a>
				</div>
				<h3>
					<sec:authorize access="isAnonymous()">
						서비스 이용을 위해<br>로그인 해주세요.
					</sec:authorize>
					<sec:authorize access="!isAnonymous()">
						<sec:authorize access="hasRole('ROLE_BIDDER')">
							<sec:authentication property="principal.sraMwmnnm"/> 님,<br/>안녕하세요.
						</sec:authorize>
						<sec:authorize access="hasRole('ROLE_FARM')">
							<sec:authentication property="principal.ftsnm"/> 님,<br/>안녕하세요.
						</sec:authorize>
					</sec:authorize>
				</h3>
				<!-- <h3>홍길동 님,<br>안녕하세요.</h3> -->
				<c:choose>
					<c:when test="${param.aucYn eq 'N' }">
						<dl class="lnb_info">
							<dd class="home"><a href="javascript:pageMove('/home');" class="ico_home">지역선택</a></dd>
							<sec:authorize access="isAnonymous()">
								<dd class="login"><a href="javascript:pageMove('/user/login');" class="ico_login">로그인</a></dd>
							</sec:authorize>
							<sec:authorize access="!isAnonymous()">
								<dd class="login"><a href="javascript:pageMove('/my/info');" class="ico_logout">나의정보</a></dd>
							</sec:authorize>
							<dd class="notice"><a href="javascript:pageMove('/main');" class="ico_noti">공지사항</a></dd>
						</dl>
						<dl class="lnb_menu">
							<dd><a href="javascript:pageMove('/main');">경매관전</a></dd>
							<dd>
								<a href="javascript:pageMove('/main');">경매응찰</a>
							</dd>
							<dd><a href="javascript:pageMove('/main');">출장우 조회</a></dd>
							<dd><a href="javascript:pageMove('/main');">경매결과 조회</a></dd>
							<dd><a href="javascript:pageMove('/main');">나의 경매내역</a></dd>
							<dd><a href="javascript:pageMove('/main');">나의 출장우</a></dd>
							<dd><a href="javascript:pageMove('/calendar');">경매일정</a></dd>
							<dd><a href="javascript:pageMove('/guide');">이용안내</a></dd>
							<sec:authorize access="!isAnonymous()">
							<dd><a href="javascript:logoutProc();" class="btn-logout">로그아웃</a></dd>
							</sec:authorize>
						</dl>
					</c:when>
					<c:otherwise>
						<dl class="lnb_info">
							<dd class="home"><a href="javascript:pageMove('/home');" class="ico_home">지역선택</a></dd>
							<sec:authorize access="isAnonymous()">
								<dd class="login"><a href="javascript:pageMove('/user/login');" class="ico_login">로그인</a></dd>
							</sec:authorize>
							<sec:authorize access="!isAnonymous()">
								<dd class="login"><a href="javascript:pageMove('/my/info');" class="ico_logout">나의정보</a></dd>
							</sec:authorize>
							<dd class="notice"><a href="javascript:pageMove('/notice');" class="ico_noti">공지사항</a></dd>
						</dl>
						<dl class="lnb_menu">
							<dd><a href="javascript:goWatchApp();">경매관전</a></dd>
							<dd><a href="javascript:pageMove('/main');">경매응찰</a></dd>
							<dd><a href="javascript:pageMove('/sales');">출장우 조회</a></dd>
							<dd><a href="javascript:pageMove('/results');">경매결과 조회</a></dd>
							<dd><a href="javascript:pageMove('/my/buy');">나의 경매내역</a></dd>
							<dd><a href="javascript:pageMove('/my/entry');">나의 출장우</a></dd>
							<dd><a href="javascript:pageMove('/calendar');">경매일정</a></dd>
							<dd><a href="javascript:pageMove('/guide');">이용안내</a></dd>
							<sec:authorize access="!isAnonymous()">
							<dd><a href="javascript:logoutProc();" class="btn-logout">로그아웃</a></dd>
							</sec:authorize>
						</dl>
					</c:otherwise>
				</c:choose>
			</li>
		</ul>
	</nav>
</c:if>
<!-- //menu-lnb e -->
<!--end::Header Menu-->

