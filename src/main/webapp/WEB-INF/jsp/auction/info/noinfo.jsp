<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>
<style>
div.title {
  width: 100%;
  height: 120px;
  margin: 0 0 20px;
  padding: 20px 0px 0px 15px; 
/*   padding: 50px 125px 50px 50px; */
  border-radius: 20px;
  background-color: #007eff;
  box-shadow : 0 3px 10px 0 rgb(0 0 0 / 16%);
}
@media only all and (max-width: 1024px)
div.title {
    height: 80px;
    border-radius: 10px;
}
div.title .txt {
  font-family: "Noto Sans KR", "Roboto", "AppleGothic", "sans-serif";
  font-size: 25px;
  font-weight: 700;
  font-stretch: normal;
  font-style: normal;
  line-height: 1.39;
  letter-spacing: -1.4px;
  text-align: left;
  color: #fff;
}

div.title .txt .text-style {
  font-weight: bold;
  color: #ffaf00;
}

div.btn_app {
  width: 75%;
  height: 60px;
  margin: 40px 35px 0;
  padding: 14px 0px 0px 16px; 
  border-radius: 10px;
  box-shadow: 0 3px 10px 0 rgba(0, 0, 0, 0.16);
  border: solid 2px #dbdbdb;
  background-color: #fff;
}

div.btn_app .txt {
  width: 305px;
  height: 52px;
  margin: 0 24px 0 0;
  font-family: "Noto Sans KR", "Roboto", "AppleGothic", "sans-serif";
  font-size: 20px;
  font-weight: bold;
  font-stretch: normal;
  font-style: normal;
  line-height: 1.11;
  letter-spacing: -0.9px;
  text-align: left;
  color: #1a1a1a;
}
div.btn_app a {
	width: 14px;
	height: 20px;
	display: block;
	background-repeat: no-repeat !important;
	background-size: 14px 20px !important;
	text-indent: -9999px;
	background-image: url(/static/images/guide/btn_next.svg);
}
</style>
<div class="auction_choice">
	<h3>경매안내</h3>	
	<dl>
		<dt><span>${fn:substring(johapData.CLNTNM,0,6)}</span></dt>
		<dd class="link"><a href="javascript:pageMove('/home');"><span class="ico_arrowblue">지역선택</span></a></dd>
	</dl>
	
	<div class="title">
		<span class="txt">스마트 가축시장<br/> <span class="text-style">서비스 준비중</span>입니다.</span>
	</div>
	
<!-- 	<div class="btn_app"> -->
<!-- 		<div style="float:left;"><span class="txt">가축시장 앱으로 보기</span></div> -->
<!-- 		<div style="float:right;margin-right:11px;"><a href="javascript:;">다음</a></div> -->
<!-- 	</div> -->

</div>
<!-- //auction_info e -->

