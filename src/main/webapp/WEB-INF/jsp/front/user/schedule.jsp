<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>

<style type="text/css">
	.list_table .list_body ul {
		min-height: 42px !important;
	}
	.list_table .list_body li dd.empty {
		padding: 10px 0 !important;
	}
</style>
<div class="chk_step1">
	<div class="main-tab">
		<ul>
			<li class="on">
				<a href="javascript:pageMove('/home');">경매<span class="sub-txt">인터넷 <br>스마트 경매</span></a>
			</li>
			<li>
				<a href="javascript:pageMove('/dashboard');">현황<span class="sub-txt">전국 <br>가축시장현황</span></a>
			</li>
		</ul>
	</div>
</div>
<div class="schedule_v2">
	<div class="back_area">
		<a href="javascript:pageMove('/home')"><img src="/static/images/guide/ico_back.svg">메인 화면으로 가기</a>
	</div>
	<div class="tab_list item-4">
		<ul>
			<li><a href="javascript:;" class="${params.type eq 'all' ? 'act' : ''} btn_tab" data-type="all">전체</a></li>
			<li><a href="javascript:;" class="${(params.type eq 'today' or empty params.type) ? 'act' : ''} btn_tab" data-type="today">오늘경매</a></li>
			<li><a href="javascript:;" class="${params.type eq 'tomorrow' ? 'act' : ''} btn_tab" data-type="tomorrow">내일경매</a></li>
			<li><a href="javascript:;" class="${params.type eq 'week' ? 'act' : ''} btn_tab" data-type="week">이번주</a></li>
		</ul>
	</div>
	
	<div class="list_table">
		<div class="list_head">
			<dl>
				<dd class="s_place">지역</dd>
				<dd class="s_market">가축시장</dd>
				<dd class="s_date">거래일</dd>
				<dd class="s_map"><img class="ico-map" src="./static/images/guide/v2/ico-place-white.svg"></dd>
			</dl>
		</div>
		<div class="list_body">
			<ul>
			<c:choose>
				<c:when test="${!empty scheduleList}">
					<c:forEach items="${scheduleList}" var="svo">
						<li>
							<dl>
								<dd class="s_place">${svo.LOCNM}</dd>
								<dd class="s_market">${fn:replace(svo.CLNTNM, '축협', '')}</dd>
								<dd class="s_date">${svo.AUCDT_DET}</dd>
								<dd class="s_telno" style="display:none">${svo.TELNO}</dd>
								<dd class="s_addr" style="display:none">${svo.ADDR1}&nbsp;${svo.ADDR2}</dd>
								<dd class="s_map"><button type="button" class="ico-map btn_detail_pop" title="연락처 소재지 팝업 열기" onclick="modalPopup('.pop_schedule_map')"></button></dd>
							</dl>
						</li>
					</c:forEach>
				</c:when>
				<c:otherwise>
						<li>
							<dl>
								<dd class="empty">경매 예정인 가축시장이 없습니다.</dd>
							</dl>
						</li>
				</c:otherwise>
			</c:choose>
			</ul>
		</div>
	</div>
</div>

<div id="" class="modal-wrap pop_schedule_map">
	<div class="modal-content">
		<p class="name"></p>
		<dl>
			<dt>연락처</dt>
			<dd class="tel"></dd>
		</dl>
		<dl>
			<dt>소재지</dt>
			<dd class="addr"></dd>
		</dl>
		<div class="btn_area">
			<button class="btn_ok" onclick="modalPopupClose('.pop_schedule_map');return false;">확인</button>
		</div>
	</div>
	<!-- //modal-content e -->
</div>