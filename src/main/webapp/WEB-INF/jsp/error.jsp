<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="en">
<!--begin::Head-->
<head>
	<meta charset="utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>가축시장</title>
	<link rel="SHORTCUT ICON" href="/static/favicon.ico" />
	<meta name="description" content="Updates and statistics" />
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<meta name="HandheldFriendly" content="true" />
	<meta name="apple-mobile-web-app-capable" content="yes">
	<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700" />
	<link rel="stylesheet" type="text/css" href="/static/css/guide/common.css">
	<link rel="stylesheet" type="text/css" href="/static/css/guide/jquery-ui.css">
	<link rel="stylesheet" type="text/css" href="/static/css/guide/popup.css">
	<link rel="stylesheet" type="text/css" href="/static/css/guide/slick.css">
	<link rel="stylesheet" type="text/css" href="/static/css/guide/jquery.mmenu.all.css">
	<link rel="stylesheet" type="text/css" href="/static/css/guide/jquery.mCustomScrollbar.css">
	<link rel="stylesheet" type="text/css" href="/static/css/guide/selectric.css">
	<script type="text/javascript" src="/static/js/guide/jquery-1.12.4.js" style="user-select: auto;"></script>
	<script src="/static/js/common/utils.js"></script>
	<style type="text/css">
		.page-wrap {
		    min-height: 100vh;
		}
	</style>
</head>
<!--end::Head-->
<!--begin::Body-->
<body>
	<div id="wrap">
		<section class="header">
			<div class="w_header_inner">
				<div class="w_header">
					<a href="javascript:pageMove('/home');" class="logo">
						<h1>
							<span>온라인 스마트 경매</span>
							가축시장.kr
						</h1>
					</a>
					<div class="side_menu">
						<ul>
							<li class="home"><a href="javascript:pageMove('/home');">지역선택</a></li>
							<li class="notice"><a href="javascript:pageMove('/notice');">공지사항</a></li>
							<li class="guide"><a href="javascript:pageMove('/guide');">이용안내</a></li>
						</ul>
					</div>
					<!-- //side_menu e -->
					<nav id="nav">
						<ul>
							<li class="watch">
								<a href="javascript:pageMove('/watch');">경매관전</a>
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
								<a href="javascript:pageMove('/my/buy');">나의 구매내역</a>
							</li>
							<li class="myEntry">
								<a href="javascript:pageMove('/my/entry');">나의 출장우</a>
							</li>
							<li class="calendar">
								<a href="javascript:pageMove('/calendar');">일정안내</a>
							</li>
						</ul>
					</nav>
					<!-- //nav e -->
				</div>
				<!-- //w_header e -->
			</div>
			<!-- //w_header_inner e -->
			<div class="m_header">
				<h1 class="m_logo">
					<span>온라인 스마트 경매</span>
					가축시장.kr
				</h1>
				<a href="#menu-lnb" class="m_menu">메뉴</a>
			</div>
			<!-- //m_header e -->
			<nav id="menu-lnb">
				<ul>
					<li class="lnb_box">
						<div class="lnb_base">
							<a href="javascript:;" class="nav_close">close</a>
						</div>
						<h3>서비스 이용을 위해<br>로그인 해주세요.	</h3>
						<dl class="lnb_info">
							<dd class="home"><a href="javascript:pageMove('/home');" class="ico_home">지역선택</a></dd>
							<dd class="login"><a href="javascript:pageMove('/user/login');" class="ico_login">로그인</a></dd>
							<dd class="notice"><a href="javascript:pageMove('/notice');" class="ico_noti">공지사항</a></dd>
						</dl>
						<dl class="lnb_menu">
							<dd><a href="javascript:pageMove('/watch');">경매관전</a></dd>
							<dd><a href="javascript:pageMove('/main');">경매응찰</a></dd>
							<dd><a href="javascript:pageMove('/sales');">출장우 조회</a></dd>
							<dd><a href="javascript:pageMove('/results');">경매결과 조회</a></dd>
							<dd><a href="javascript:pageMove('/my/buy');">나의 구매내역</a></dd>
							<dd><a href="javascript:pageMove('/my/entry');">나의 출장우</a></dd>
							<dd><a href="javascript:pageMove('/calendar');">일정안내</a></dd>
							<dd><a href="javascript:pageMove('/guide');">이용안내</a></dd>
						</dl>
					</li>
				</ul>
			</nav>
			<!-- //menu-lnb e -->
			<!--end::Header Menu-->
		</section>
		<!-- //header e -->
		<section class="contents">
<!-- 			<div class="page-wrap d-flex flex-row align-items-center"> -->
<!-- 			    <div class="container"> -->
<!-- 			        <div class="row justify-content-center"> -->
<!-- 			            <div class="col-md-12 text-center"> -->
<!-- 			                <span class="display-1 d-block"></span> -->
<%-- 			                <div class="mb-4 lead">요청하신 주소 또는 경로에 정상적으로 접근할 수 없습니다. [${requestScope['javax.servlet.error.status_code']}]</div> --%>
<!-- 			                <a href="javascript:pageMove('/home');" class="btn btn-link">Back to Home</a> -->
<!-- 			            </div> -->
<!-- 			        </div> -->
<!-- 			    </div> -->
<!-- 			</div> -->
			<style>
				div.error { text-align: center;}
				div.error img {
				  width: 136px;
				  height: 73px;
				  margin: 0 318px 38px;
				  object-fit: contain;
				}
				div.error p.errCode {
				  width: 273px;
				  height: 115px;
/* 				  margin: 61px 249px 31px 250px; */
				  margin: 61px auto 31px;
				  font-family: "Noto Sans KR", "Roboto", "AppleGothic", "sans-serif";
				  font-size: 159px;
				  font-weight: bold;
				  font-stretch: normal;
				  font-style: normal;
				  line-height: 0.24;
				  letter-spacing: -3.98px;
				  text-align: left;
				  color: #007eff;
				}
				
				div.error p.errCode span {
				  color: #76baff;
				}
				div.error .errorTxt1 {
				  width: 344px;
				  height: 46px;
/* 				  margin: 101px 214px 10px; */
				  margin: 101px auto 10px;
				  font-family: "Noto Sans KR", "Roboto", "AppleGothic", "sans-serif";
				  font-size: 32px;
				  font-weight: 500;
				  font-stretch: normal;
				  font-style: normal;
				  line-height: 1.19;
				  letter-spacing: -0.8px;
				  text-align: center;
				  color: #007eff;
				}
				div.error .errorTxt2 {
				  width: 772px;
				  height: 37px;
				  margin: 20px 0 40px;
				  font-family: "Noto Sans KR", "Roboto", "AppleGothic", "sans-serif";
				  font-size: 26px;
				  font-weight: 500;
				  font-stretch: normal;
				  font-style: normal;
				  line-height: 1.46;
				  letter-spacing: -0.65px;
				  text-align: center;
				  color: #999;
				}
/* 				div.error .btnDiv { */
/* 				  width: 273.2px; */
/* 				  height: 80.4px; */
/* 				  margin: 40px 249.4px 0; */
/* 				  padding: 21.6px 35.9px 10.5px 34.4px; */
/* 				  border-radius: 10px; */
/* 				  background-color: #007eff; */
/* 				} */
				div.error .btnDiv {
				  margin: 40px 346px 0;
				  padding: 21.6px 35.9px 10.5px 34.4px;
				  border-radius: 10px;
				  background-color: #007eff;
				}
				div.error div.btnDiv span{
				  width: 132.6px;
				  height: 40.2px;
				  font-family: "Noto Sans KR", "Roboto", "AppleGothic", "sans-serif";
				  font-size: 28px;
				  font-weight: 500;
				  font-stretch: normal;
				  font-style: normal;
				  line-height: 1.07;
				  letter-spacing: -0.7px;
				  text-align: left;
				  color: #fff;
				}
				@media only all and (max-width: 768px) {
					div.error img{ margin: 0 auto 38px; }
					div.error p.errCode {margin: 42px auto 31px;}
					div.error .errorTxt1 { margin: 101px auto 10px;font-size: 26px; }
					div.error .errorTxt2 { margin: 20px auto 40px;font-size: 18px;}
					div.error .btnDiv{margin: 30px 60px 0;padding: 21.6px 0 10.5px;}
			</style>
			<div class="error">
				<img src="/static/images/assets/errorIcon.png" srcset="/static/images/assets/errorIcon@2x.png 2x,/static/images/assets/errorIcon3x.png 3x" class="-\31 9139" />
				<div>
					<p class="errCode">4<span>0</span>4
					</p>
					<span class="errorTxt1">  페이지를 찾을 수 없습니다.</span><br/>
					<span class="errorTxt2">  입력하신 인터넷 주소(URL) 또는 경로를 다시 한 번 확인해주시기 바랍니다.</span>					
					
					<a href="javascript:pageMove('/home');">
						<div class="btnDiv"><span>가축시장 홈</span></div>
					</a>
					
				</div>
			</div>
		</section>
		<!-- //content e -->
		<section class="footer">
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
		</section>
		<!-- //footer e -->
	</div>	
	<!-- //wrap e -->
</body>
<!--end::Body-->
</html>