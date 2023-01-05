<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>
<script src="https://webrtc.github.io/adapter/adapter-latest.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@remotemonster/sdk/remon.min.js"></script>
<script src="/static/js/socket.io/socket.io.js"></script>
<style>
	video[poster]{ 
		/* 포스터 이미지의 크기를 비디오 영상에 꽉차도록 */
    	height:100%;
    	width:100%;
    }
/* 	@media only all and (max-width: 1024px) { */
/* 		.draggable {min-height:243px !important; } */
/* 	} */
	
/* 	@media only all and (max-width: 768px) { */
/* 		.draggable {min-height:243px !important; } */
/* 	} */
</style>
<div class="auction_list">
	<input type="hidden" id="token" value="${watchToken }"/>
	<input type="hidden" id="naBzPlc" value="${johapData.NA_BZPLC}" />
	<input type="hidden" id="aucDsc" value="${johapData.AUC_DSC}" />
	<input type="hidden" id="kkoSvcId" value="${johapData.KKO_SVC_ID}" />
	<input type="hidden" id="kkoSvcKey" value="${johapData.KKO_SVC_KEY}" />
	<input type="hidden" id="kkoSvcCnt" value="${johapData.KKO_SVC_CNT}" />
	<input type="hidden" id="webPort" value="${johapData.WEB_PORT}" />
	
	<input type="hidden" id="aucDate" value="${dateVo.AUC_DT}" />

<%-- 	<div class="auction_seeBox" style="${johapData.AUC_DSC eq '2'?'margin-bottom: 0px;':'' }"> --%>
	<div class="auction_seeBox">
		<div class="seeBox_top">
			<dl>
				<dt>${johapData.CLNTNM}</dt>
				<dd>
					<ul>
						<!-- <li>송아지 경매</li> -->
						<li>${johapData.AUC_DSC eq '1' ? '단일' : '일괄'} 경매</li>
						<li>출장우 : <span class='watchCowCnt'>${watchCount.CNT }</span> 두</li>
						<c:if test="${johapData.AUC_DSC eq '1' }"><li><a href="javscript:;" class="m_sound">소리</a></li></c:if>
					</ul>
				</dd>
			</dl>
		</div>
		<!-- //seeBox_top e -->
		
		<div class="seeBox_bottom vidioSlide">
			<div class="seeBox_slick">
				<ul class="slider">
						<li class="boarder">
							<div class="seeBox_slick_inner">
								<ul class="seeBox-cell">
									<li>
										<dl>
											<dt>번호</dt>
											<dd class="auctionNum">${watchList[0].AUC_PRG_SQ }</dd>
										</dl>
									</li>
									<li>
										<dl>
											<dt>출하주</dt>
											<dd class="ftsnm">${watchList[0].FTSNM }</dd>
										</dl>
									</li>
									<li>
										<dl>
											<dt>성별</dt>
											<dd class="sex">${watchList[0].INDV_SEX_C_NAME }</dd>
										</dl>
									</li>
									<li>
										<dl>
											<dt>중량(Kg)</dt>
											<dd class="cowSogWt">
												<fmt:formatNumber value="${empty watchList[0].COW_SOG_WT or watchList[0].COW_SOG_WT <= 0 ? '0' : fn:split(watchList[0].COW_SOG_WT,'.')[0]}" type="number" />
											</dd>
										</dl>
									</li>
								</ul>
								<ul class="seeBox-cell">
									<li>
										<dl>
											<dt>산차</dt>
											<dd class="matime">${watchList[0].MATIME }</dd>
										</dl>
									</li>
									<li>
										<dl>
											<dt>계대</dt>
											<dd class="sraIndvPasgQcn">${watchList[0].SRA_INDV_PASG_QCN }</dd>
										</dl>
									</li>
									<li>
										<dl>
											<dt>어미</dt>
											<dd class="mcowDsc">${watchList[0].MCOW_DSC_NAME }</dd>
										</dl>
									</li>
									<li>
										<dl>
											<dt>예정가</dt>
											<dd class="lowsSbidLmtAm">
												<fmt:formatNumber value="${watchList[0].LOWS_SBID_LMT_AM <= 0 ? '0' : fn:split(watchList[0].LOWS_SBID_LMT_UPR,'.')[0]}" type="number" />
											</dd>
										</dl>
									</li>
								</ul>
								<ul class="seeBox-cell">
									<li>
										<dl>
											<dt>KPN</dt>
											<dd class="kpnNo">${watchList[0].KPN_NO_STR }</dd>
										</dl>
									</li>
									<li class="cell_3">
										<dl>
											<dt>비고</dt>
											<dd class="rmkCntn"><p class="move-txt">${watchList[0].RMK_CNTN }</p></dd>
										</dl>
									</li>
								</ul>
							</div>
							<div class="mo_seeBox">
								<ul>
									<li>
										<dl>
											<dt><span style="color:#1a1a1a;">경</span>번호</dt>
											<dd class="auctionNum">${watchList[0].AUC_PRG_SQ }</dd>
										</dl>
									</li>
									<li>
										<dl>
											<dt>성별</dt>
											<dd class="sex">${watchList[0].INDV_SEX_C_NAME }</dd>
										</dl>
									</li>
									<li>
										<dl>
											<dt>출하주</dt>
											<dd class="ftsnm">${watchList[0].FTSNM }</dd>
										</dl>
									</li>
									<li>
										<dl>
											<dt>중량</dt>
											<dd class="cowSogWt"><fmt:formatNumber value="${empty watchList[0].COW_SOG_WT or watchList[0].COW_SOG_WT <= 0 ? '0' : fn:split(watchList[0].COW_SOG_WT,'.')[0]}" type="number" /></dd>
										</dl>
									</li>
									<li>
										<dl>
											<dt><span style="color:#1a1a1a;">경</span>산차</dt>
											<dd class="matime">${watchList[0].MATIME }</dd>
										</dl>
									</li>
									<li>
										<dl>
											<dt>어미</dt>
											<dd class="mcowDsc">${watchList[0].MCOW_DSC_NAME }</dd>
										</dl>
									</li>
									<li>
										<dl>
											<dt><span style="color:#1a1a1a;">경</span>계대</dt>
											<dd class="sraIndvPasgQcn">${watchList[0].SRA_INDV_PASG_QCN }</dd>
										</dl>
									</li>
									<li>
										<dl>
											<dt>KPN</dt>
											<dd class="kpnNo">${watchList[0].KPN_NO_STR }</dd>
										</dl>
									</li>
									<li class="harf-list">
										<dl>
											<dt>예정가</dt>
											<dd class="lowsSbidLmtAm">
												<fmt:formatNumber value="${watchList[0].LOWS_SBID_LMT_AM <= 0 ? '0' : fn:split(watchList[0].LOWS_SBID_LMT_UPR,'.')[0]}" type="number" />
											</dd>
										</dl>
									</li>
									<li class="harf-list">
										<dl>
											<dt>비고</dt>
											<dd class="rmkCntn"><p class="move-txt" style="width: 150px;">${watchList[0].RMK_CNTN }</p></dd>
										</dl>
									</li>
								</ul>
							</div>	
						</li>
						<c:forEach begin="1" end="${(johapData.KKO_SVC_CNT eq '' or johapData.KKO_SVC_CNT == null) ? '0' : johapData.KKO_SVC_CNT}" varStatus="st">
<%-- 							<c:forEach begin="1" end="4" varStatus="st"> --%>
							<li class="video_item" style="width: 100%;height: auto;">
								<video id="remoteVideo${st.index }" style="width: 100%;background: black;min-height:230px;" poster="/static/images/assets/no_video_18980.png" muted="muted" autoplay playsinline webkit-playsinline controls>
									Your browser does not support HTML5 video.
								</video>
							</li>
						</c:forEach>
					</ul>
				</div> 
				<div class="seeBox_gp">
					<!-- 페이지 하단에 js 있음 -->
					<div class="chart" data-percent="${watchCount.CNT == 0 ? '0' : watchCount.CNT_STAND_ELSE/ watchCount.CNT * 100 }">
						<input type='hidden' name='entryTotalCnt' value='${watchCount.CNT}' />
						<input type='hidden' name='entryStandCnt' value='${watchCount.CNT_STAND }' />
						<input type='hidden' name='entryStandElseCnt' value='${watchCount.CNT_STAND_ELSE }' />
						<dl>
							<dt>${watchCount.CNT == 0 ? '0' : fn:split(watchCount.CNT_STAND_ELSE/ watchCount.CNT * 100,'.')[0] }%</dt>
							<dd class="gp_tit1 stand">대기 <span>${watchCount.CNT_STAND }</span></dd>
							<dd class="gp_tit2 bid">완료 <span>${watchCount.CNT_BID }</span> </dd>
						</dl>
					</div>
				</div>
			</div>		
			<div class="stats-box">
				<div class="num stand"><i class="off"></i>대기 <span>${watchCount.CNT_STAND }</span></div>
				<div class="num bid"><i class="on"></i>완료 <span>${watchCount.CNT_BID }</span></div>
				<div><button type="button" class="btn_reload btn-f5">새로고침</button></div>
			</div>
	</div>
	<!-- //auction_seeBox e -->
	<div class="list_table auction_see tblAuction">
		<div class="list_head" style="${johapData.AUC_DSC eq '2'?'border-radius: 0px;':'' }">
			<dl>
				<dd class="date">경매일자</dd>
				<dd class="num"><span class="w_view_in">경매</span>번호</dd>
				<dd class="name">출하주</dd>
				<dd class="pd_ea">개체</dd>
				<dd class="pd_sex">성별</dd>
				<dd class="pd_kg">중량<span class="w_view_in">(kg)</span></dd>
				<dd class="pd_kpn">KPN</dd>
				<dd class="pd_num1">계대</dd>
				<dd class="pd_pay1">예정가</dd>
				<dd class="pd_pay2">낙찰가</dd>
				<dd class="pd_state">경매결과</dd>
				<dd class="pd_etc">비고</dd>
			</dl>
		</div>
		<div class="list_body">
			<ul class="mCustomScrollBox">
               	<c:if test="${ watchList.size() <= 0 }">
					<li class="noInfo">
						<dl>
							<dd>경매관전 자료가 없습니다.</dd>
						</dl>
					</li>
               	</c:if>
               	<c:forEach items="${ watchList }" var="vo" varStatus="st">
					<li class="${st.index == 0?'act':'' }">
						<input type='hidden' name="mcowDsc" class="mcowDsc" value="${ vo.MCOW_DSC_NAME }"/>
						<input type='hidden' name="matime" class="matime" value="${ vo.MATIME }"/>
						<dl>
							<dd class="date aucDt">${ vo.AUC_DT_STR }</dd>
							<dd class="num aucPrgSq">${ vo.AUC_PRG_SQ }</dd>
							<dd class="name ftsnm">${ vo.FTSNM }</dd>
							<dd class="pd_ea sraIndvAmnno">${ vo.SRA_INDV_AMNNO_FORMAT }</dd>
							<dd class="pd_sex indvSexC">${ vo.INDV_SEX_C_NAME }</dd>
							<dd class="pd_kg cowSogWt textNumber">
								<fmt:formatNumber value="${(empty vo.COW_SOG_WT || vo.COW_SOG_WT <= 0 ) ? '0' : fn:split(vo.COW_SOG_WT,'.')[0]}" type="number" />
							</dd>
							<dd class="pd_kpn kpnNo">${ vo.KPN_NO_STR }</dd>
							<dd class="pd_num1 sraIndvPasgQcn">${ vo.SRA_INDV_PASG_QCN }</dd>
							<dd class="pd_pay1 lowsSbidLmtAm textNumber">
								<c:choose>
									<c:when test="${vo.LOWS_SBID_LMT_AM eq '' || vo.LOWS_SBID_LMT_AM == null || vo.LOWS_SBID_LMT_AM <= 0}">-</c:when>
									<c:otherwise>
										<fmt:formatNumber value="${fn:split(vo.LOWS_SBID_LMT_UPR,'.')[0]}" type="number" />
									</c:otherwise>
								</c:choose>
							</dd>
							<dd class="pd_pay2 sraSbidAm textNumber">
								<c:choose>
									<c:when test="${vo.SRA_SBID_UPR eq '' || vo.SRA_SBID_UPR == null || vo.SRA_SBID_UPR <= 0}">-</c:when>
									<c:otherwise>
										<fmt:formatNumber value="${fn:split(vo.SRA_SBID_UPR,'.')[0]}" type="number" />
									</c:otherwise>
								</c:choose>
							</dd>
							<dd class="pd_state selSts">${vo.SEL_STS_DSC_NAME }</dd>
							<dd class="pd_etc rmkCntn">${ fn:replace(vo.RMK_CNTN,'|',',') }</dd>
						</dl>
					</li>
				</c:forEach>				
			</ul>
		</div>
	</div>
	<!-- //auction_see e -->
</div>
<!-- //auction_list e -->