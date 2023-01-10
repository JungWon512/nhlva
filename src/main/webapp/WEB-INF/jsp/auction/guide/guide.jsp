<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>
<style>
img.img_1,img.img_2,img.img_3 {
  width: 90%;
  height: 100%;
  margin: 10px 20px 10px;
  object-fit: contain;
}
img.img_2 {
  margin-bottom: 0px!important;
}
img.img_3 {
  margin-top: 0px!important;
}
div.txt{
  margin: 5px 20px 5px;
}
span.txt {
  font-family: "Noto Sans KR", "Roboto", "AppleGothic", "sans-serif";
  font-size: 16px;
  font-weight: 500;
  font-stretch: normal;
  font-style: normal;
  line-height: 1.45;
  letter-spacing: -0.7px;
  text-align: left;
  color: #1a1a1a;
}
</style>
<!-- url(/static/images/guide/btn_prev.svg) -->
<div class="guide">
	<img src="/static/images/assets/guid_18477.png" srcset="/static/images/assets/guid_18477@2x.png 2x, /static/images/assets/guid_18477@3x.png 3x" class="img_2">
   <!-- jpg -->
	<img src="/static/images/assets/11-1.경매응찰_테이블_1.png" srcset="/static/images/assets/11-1.경매응찰_테이블_1@2x.png 2x, /static/images/assets/11-1.경매응찰_테이블_1@3x.png 3x" class="img_1">
	<div class="txt">
		<span class="txt">① 경매에 참가한 중도매인의 참가번호<br/>② 출하우의 정보·영상</span>
	</div>
	<img src="/static/images/assets/11-1.경매응찰_테이블2.png" srcset="/static/images/assets/11-1.경매응찰_테이블2@2x.png 2x, /static/images/assets/11-1.경매응찰_테이블2@3x.png 3x" class="img_1">
	<div class="txt">
		<span class="txt">
			③ 경매 진행 상황 <br/>
			④ 응찰한 금액 표시. '찜' 기능 이용 시 파란색으로 '찜가격' 이 표시됩니다.<br/>
			⑤ 원하시는 금액 입력 후(만 원 단위) '응찰' 버튼 클릭합니다. 금액 수정 시 Backspace(화살표 버튼)로 지울 수 있습니다. 취소 클릭 시 해당 건 응찰이 취소되므로 주의하시길 바랍니다.<br/>
			⑥ 아이콘 클릭 시 예정조회·결과조회·응찰내역을 볼 수 있습니다. 예정조회에서 '찜가격'(응찰 예정가)을 미리 입력할 수 있습니다.
		</span>
	</div>
	<img src="/static/images/assets/11.경매응찰_레이어팝업창.png" srcset="/static/images/assets/11.경매응찰_레이어팝업창@2x.png 2x, /static/images/assets/11.경매응찰_레이어팝업창@3x.png 3x" class="img_1">
	<div class="txt">
		<span class="txt">
			⑦ 찜가격의 하트 아이콘 클릭 시  '찜가격'(응찰 예정가)을 미리 입력할 수 있는 팝업창이 켜집니다.
		</span>
	</div>
	<img src="/static/images/assets/11.png" srcset="/static/images/assets/11@2x.png 2x, /static/images/assets/11@3x.png 3x" class="img_2">
	<img src="/static/images/assets/11.경매응찰_레이어팝업창2.png" srcset="/static/images/assets/11.경매응찰_레이어팝업창2@2x.png 2x, /static/images/assets/11.경매응찰_레이어팝업창2@3x.png 3x" class="img_3">
	<div class="txt">
		<span class="\-">
			⑧ 금액 입력 후 확인 버튼 클릭 시 해당 출하우에 대한 '찜가격'이 설정됩니다.<br/>
			⑨ '찜가격' 설정 완료<br/><br/>
			* '찜가격' 설정된 하트 버튼(파란색 하트) 클릭 시 해당 출하우에 대한 기존의 '찜가격'이 초기화됩니다.
		</span>
	</div>
</div>
