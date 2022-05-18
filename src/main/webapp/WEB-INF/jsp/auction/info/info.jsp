<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>
<div class="auction_info">
	<h3>경매안내</h3>
	<dl class="board_top">
		<dt>
			<h4><span>${fn:substring(johapData.CLNTNM,0,8) }</span></h4>
			
			<c:choose>
				<c:when test="${aucDate.size() > 0  }">
					<p>${aucDate[0].AUC_MONTH }월 ${aucDate[0].AUC_DAY }일(${aucDate[0].AUC_WEEK_DAY }) ${aucDate[0].AUC_OBJ_DSC_NAME } 경매입니다.</p>			
				</c:when>
				<c:otherwise>
					<p>예정된 경매가 없습니다.</p>
				</c:otherwise>
			</c:choose>
			
		</dt>
		<dd>
			<ul>
				<li><a href="javascript:pageMove('/results');"><span class="ico_arrowblue">경매결과 보기</span></a></li>
				<li><a href="javascript:pageMove('/sales');"><span class="ico_arrowblue">출장우 보기</span></a></li>
			</ul>
		</dd>
	</dl>
	<div class="board_bottom">
		<dl>
<!-- 			<dt style="text-align: center;">일정 안내</dt> -->
<!-- 			<dd class="time"> -->
<!-- 				큰소는 매월 1, 3째주 <span>목요일 오전 10시 30분</span> /<br>  -->
<!-- 				송아지는 2, 4째주 <span>목요일 오전 09시</span>에 시작합니다. -->
<!-- 			</dd> -->
<!-- 			<dd>경매에 참가하시기전에 인터넷 경매에 대한 내용과 이용방법을 숙지하신 후 경매진행을 하시길 바랍니다.</dd> -->
		</dl>
	</div>
	
	<div class="auction_date">
		<h3>일정안내</h3>
		<div class="date_board">
			<div class="date_bottom">
				<ul>
					<c:if test="${ resultList.size() <= 0 }">
						<li>
							<dl class="no_date">
								<h4>경매일정이 없습니다.</h4>
							</dl>
						</li>
					</c:if>
					<c:forEach items="${ dateList }" var="vo">
						<li>
							<dl class="${vo.AUC_DT < today ?'dis_day':'' }">
								<dt>
									<p>${vo.AUC_DAY }</p>
									<span>${vo.AUC_WEEK_DAY }요일</span>
								</dt>
								<dd>
									<p class="date_tit">${vo.CLNTNM } ${vo.AUC_OBJ_DSC_NAME } 경매</p>	
									<p class="date_time">${vo.AUC_YEAR }년 ${vo.AUC_MONTH }월 ${vo.AUC_DAY}일(${vo.AUC_WEEK_DAY })</p>
								</dd>
							</dl>
						</li>
					</c:forEach>
				</ul>
			</div>
			<!-- //date_bottom e -->
		</div>
	</div>
	<!-- //auction_info_bottom e -->
</div>
<!-- //auction_info e -->