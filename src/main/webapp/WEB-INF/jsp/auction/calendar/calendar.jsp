<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>
<!-- <div class="kt-container  kt-container--fluid  kt-grid__item kt-grid__item--fluid"> -->
<!--    <img src="/static/images/prepare.png" style="width:100vw;"></img> -->
<!-- </div> -->
<div class="auction_date">
	<h3>일정안내</h3>
	<div class="date_board">
		<input type="hidden" id="searchYm" name="searchYm" value="${paramVo.searchYm }"/>
		<dl class="date_top">
			<dt>${title }</dt>
			<dd class="btn_prev"><a href="javascript:;">이전</a></dd>
			<dd class="btn_next"><a href="javascript:;">다음</a></dd>
		</dl>
		<div class="date_bottom">
			<ul>
				<c:if test="${ resultList.size() <= 0 }">
					<li>
						<dl class="no_date">
							<h4>경매일정이 없습니다.</h4>
						</dl>
					</li>
				</c:if>
				<c:forEach items="${ resultList }" var="vo">
					<li>
						<dl class="${vo.AUC_DT < paramVo.today ?'dis_day':'' }">
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
<!-- 				<li> -->
<!-- 					<dl  class="dis_day"> -->
<!-- 						<dt> -->
<!-- 							<p>03</p> -->
<!-- 							<span>목요일</span> -->
<!-- 						</dt> -->
<!-- 						<dd> -->
<!-- 							<p class="date_tit">합천축협 큰소, 일반우 경매완료 합천축협 큰소, 일반우 경매완료 합천축협 큰소, 일반우 경매완료</p> -->
<!-- 							<p class="date_time">오전 10시 30분</p> -->
<!-- 						</dd> -->
<!-- 					</dl> -->
<!-- 				</li> -->
<!-- 				<li> -->
<!-- 					<dl> -->
<!-- 						<dt> -->
<!-- 							<p>03</p> -->
<!-- 							<span>목요일</span> -->
<!-- 						</dt> -->
<!-- 						<dd> -->
<!-- 							<p class="date_tit">합천축협 큰소, 일반우 경매완료</p> -->
<!-- 							<p class="date_time">오전 10시 30분</p> -->
<!-- 						</dd> -->
<!-- 					</dl> -->
<!-- 				</li> -->
			</ul>
		</div>
		<!-- //date_bottom e -->
	</div>
	<!-- //date_board e -->
</div>
<!-- //auction_date e -->