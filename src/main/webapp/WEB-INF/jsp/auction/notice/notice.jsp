<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>
<!--begin::Container-->
<div class="notice_area">
	<input type="hidden" id="johpCdVal" value="${johapData.NA_BZPLC}" />
	<h3>공지사항</h3>
	<p class="notice_title">[${johapData.CLNTNM}]에서 알려드립니다.</p>
	<div class="notice_list">
		<ul>		
			<c:forEach items="${ noticeList }" var="vo">
				<li>
					<div class="notice_btn">
						<input type="hidden" class="noticeSeqNo" value="${vo.BBRD_SQNO}" />
						<input type="hidden" class="noticeDsc" value="${vo.BLBD_DSC}" />
						<a href="javascript:;">
							<dl>
								<dt><c:out value="${vo.BBRD_TINM }" escapeXml="false" /></dt>
								<dd>${vo.LSCHG_DTM }</dd>
							</dl>
						</a>
					</div>
					<!-- //notice_btn e -->
					<div class="notice_view readCnt sqNo_${vo.BLBD_DSC}_${vo.BBRD_SQNO}""></div>
					<!-- //notice_view e -->
				</li>
			</c:forEach>
		</ul>
	</div>
	<!-- //notice_list e -->
</div>
<!-- //notice_area e -->
	
	