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
</style>
<div class="auction_list">
	<input type="hidden" id="token" value="${watchToken }"/>
	<input type="hidden" id="naBzPlc" value="${johapData.NA_BZPLC}" />
	<input type="hidden" id="aucDsc" value="${johapData.AUC_DSC}" />
	<input type="hidden" id="kkoSvcId" value="${johapData.KKO_SVC_ID}" />
	<input type="hidden" id="kkoSvcKey" value="${johapData.KKO_SVC_KEY}" />
	<input type="hidden" id="kkoSvcCnt" value="${johapData.KKO_SVC_CNT}" />
	<input type="hidden" id="aucDate" value="${dateVo.AUC_DT}" />

	<div class="auction_seeBox" style="${johapData.AUC_DSC eq '2'?'margin-bottom: 0px;':'' }">
		<div class="seeBox_top">
			<dl>
				<dt>${johapData.CLNTNM}</dt>
				<dd>
					<ul>
						<!-- <li>송아지 경매</li> -->
						<li>${johapData.AUC_DSC eq '1' ? '단일' : '일괄'} 경매</li>
						<li>출장우 : ${watchCount.CNT } 두</li>
						<c:if test="${johapData.AUC_DSC eq '1' }"><li><a href="javscript:;" class="m_sound">소리</a></li></c:if>
					</ul>
				</dd>
			</dl>
		</div>
		<!-- //seeBox_top e -->
		
		<c:if test="${johapData.AUC_DSC eq '1' }">
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
												<dd class="ftsnm">${watchList[0].SRA_PDMNM }</dd>
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
												<dd class="cowSogWt">${fn:split(watchList[0].COW_SOG_WT,'.')[0] }</dd>
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
												<dt>최저가</dt>
		<%-- 										<dd class="lowsSbidLmtAm">${fn:split(watchList[0].LOWS_SBID_LMT_UPR ,'.')[0]}</dd> --%>
												<dd class="lowsSbidLmtAm">${watchList[0].LOWS_SBID_LMT_AM <= 0 ? '0' : fn:split(watchList[0].LOWS_SBID_LMT_UPR,'.')[0]}</dd>
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
												<dd class="ftsnm">${watchList[0].SRA_PDMNM }</dd>
											</dl>
										</li>
										<li>
											<dl>
												<dt>중량</dt>
												<dd class="cowSogWt">${fn:split(watchList[0].COW_SOG_WT,'.')[0] }</dd>
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
												<dt>최저가</dt>
		<%-- 										<dd class="lowsSbidLmtAm">${fn:split(watchList[0].LOWS_SBID_LMT_UPR ,'.')[0]}</dd> --%>
												<dd class="lowsSbidLmtAm">
<!-- 													<p class="move-txt" style="width: 150px;"> -->
														${watchList[0].LOWS_SBID_LMT_AM <= 0 ? '0' : fn:split(watchList[0].LOWS_SBID_LMT_UPR,'.')[0]}
<!-- 													</p>													 -->
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
							<c:forEach begin="1" end="1" varStatus="st">
								<li class="video_item" style="width: 100%;height: auto;">
									<video id="remoteVideo${st.index }" style="width: 100%;background: black;height: 100%;" poster="/static/images/assets/no_video_18980.png" muted="muted"  playsinline webkit-playsinline>
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
							<dd class="gp_tit1 bid">완료 <span>${watchCount.CNT_BID }</span> </dd>
							<dd class="gp_tit2 stand">대기 <span>${watchCount.CNT_STAND }</span></dd>
						</dl>
					</div>
				</div>
			</div>		
		</c:if>
	</div>
<!-- 	<video id="remoteVideo1" class="remote-video center w-300 h-300" poster="/static/images/assets/no_video_18980.png" playsinline style="z-index:1; width: 100%;"></video> -->
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
				<dd class="pd_pay1">최저가</dd>
				<dd class="pd_pay2">낙찰가</dd>
				<dd class="pd_state">경매상태</dd>
				<dd class="pd_etc">비고</dd>
			</dl>
		</div>
		<div class="list_body">
			<ul class="mCustomScrollBox">
               	<c:if test="${ watchList.size() <= 0 }">
					<li>
						<dl>
							<dd>경매관전 자료가 없습니다.</dd>
						</dl>
					</li>
               	</c:if>
               	<c:forEach items="${ watchList }" var="vo" varStatus="st">
					<li class="${st.index == 0?'act':'' }">
						<dl>
							<dd class="date aucDt">${ vo.AUC_DT_STR }</dd>
							<dd class="num aucPrgSq">${ vo.AUC_PRG_SQ }</dd>
							<dd class="name ftsnm">${ vo.SRA_PDMNM }</dd>
							<dd class="pd_ea sraIndvAmnno">${ vo.SRA_INDV_AMNNO_FORMAT }</dd>
							<dd class="pd_sex indvSexC">${ vo.INDV_SEX_C_NAME }</dd>
							<dd class="pd_kg cowSogWt textNumber">${(vo.COW_SOG_WT eq '' || vo.COW_SOG_WT == null || vo.COW_SOG_WT <= 0 ) ? '0' : fn:split(vo.COW_SOG_WT,'.')[0]}</dd>
							<dd class="pd_kpn kpnNo">${ vo.KPN_NO_STR }</dd>
							<dd class="pd_num1 sraIndvPasgQcn">${ vo.SRA_INDV_PASG_QCN }</dd>
							<dd class="pd_pay1 lowsSbidLmtAm textNumber"> ${(vo.LOWS_SBID_LMT_AM eq '' || vo.LOWS_SBID_LMT_AM == null || vo.LOWS_SBID_LMT_AM <= 0 ) ? '-' : vo.LOWS_SBID_LMT_AM <= 0 ? '0' : fn:split(vo.LOWS_SBID_LMT_UPR,'.')[0]}</dd>
							<dd class="pd_pay2 sraSbidAm textNumber">${(vo.SRA_SBID_UPR eq '' || vo.SRA_SBID_UPR == null || vo.SRA_SBID_UPR <= 0 ) ? '-' : fn:split(vo.SRA_SBID_UPR,'.')[0]}</dd>
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