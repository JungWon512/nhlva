<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>
<script src="https://webrtc.github.io/adapter/adapter-latest.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@remotemonster/sdk/remon.min.js"></script>
<script src="/static/js/socket.io/socket.io.js"></script>
<style type="text/css">
	@media only all and (max-width: 1024px) {
		.draggable {min-height:243px !important; }
	}
	@media only all and (max-width: 768px) {
		.draggable {min-height:243px !important; }
	}
</style>
<!-- //auction_list s : 경매 응찰 본 화면 -->
<div class="auction_list has_auction_see">
	<input type="hidden" id="naBzPlc"	value="${johapData.NA_BZPLC}" />
	<input type="hidden" id="naBzPlcNo"	value="${johapData.NA_BZPLCNO}"/>
	<input type="hidden" id="webPort" value="${johapData.WEB_PORT}" />
	<input type="hidden" id="kkoSvcId" value="${johapData.KKO_SVC_ID}" />
	<input type="hidden" id="kkoSvcKey" value="${johapData.KKO_SVC_KEY}" />
	<input type="hidden" id="kkoSvcCnt" value="${johapData.KKO_SVC_CNT}" />
	<input type="hidden" id="trmnAmnno" value="${trmnAmnno}" />
	
	<!-- //auction_area s -->
	<div class="auction_area">
		<!-- //calculator_head_pc s -->
		<div class="calculator_head_pc">
			<dl>
				<dt>${johapData.CLNTNM}<span>|</span>참가번호 <b class="join-num" style="font-weight:700;"></b>
					<span>|</span>
					<a href="javscript:;" class="m_sound">소리</a>
				</dt>
				<dd>
					<div class="auc-txt">
						<div class="info_board"></div>
						<div class="message_board" style="display:none;"></div>
					</div>
				</dd>
			</dl>
		</div>
		<!-- //calculator_head_pc e -->
		<!-- //auction_seeBox s -->
		<div class="auction_seeBox calculator_type">
			<!-- //videoSlide s -->
			<div class="seeBox_bottom vidioSlide">
				<div class="seeBox_slick">
					<ul class="slider">
						<li class="boarder">
							<div class="seeBox_slick_inner">
								<ul class="seeBox-cell">
									<li>
										<dl>
											<dt>번호</dt>
											<dd class="auctionNum"></dd>
										</dl>
									</li>
									<li>
										<dl>
											<dt>출하주</dt>
											<dd class="ftsnm"></dd>
										</dl>
									</li>
									<li>
										<dl>
											<dt>성별</dt>
											<dd class="sex"></dd>
										</dl>
									</li>
									<li>
										<dl>
											<dt>중량(Kg)</dt>
											<dd class="cowSogWt"></dd>
										</dl>
									</li>
								</ul>
								<ul class="seeBox-cell">
									<li>
										<dl>
											<dt>산차</dt>
										<dd class="matime"></dd>
										</dl>
									</li>
									<li>
										<dl>
											<dt>계대</dt>
											<dd class="sraIndvPasgQcn"></dd>
										</dl>
									</li>
									<li>
										<dl>
											<dt>어미</dt>
										<dd class="mcowDsc"></dd>
										</dl>
									</li>
									<li>
										<dl>
											<dt>최저가</dt>
											<dd class="lowsSbidLmtAm"></dd>
										</dl>
									</li>
								</ul>
								<ul class="seeBox-cell">
									<li>
										<dl>
											<dt>KPN</dt>
											<dd class="kpnNo"></dd>
										</dl>
									</li>
									<li class="cell_3">
										<dl>
											<dt>비고</dt>
											<dd class="rmkCntn"></dd>
										</dl>
									</li>
								</ul>
							</div>
							<div class="mo_seeBox">
								<ul>
									<li>
										<dl>
											<dt>번호</dt>
											<dd class="auctionNum"></dd>
										</dl>
									</li>
									<li>
										<dl>
											<dt>성별</dt>
											<dd class="sex"></dd>
										</dl>
									</li>
									<li>
										<dl>
											<dt>출하주</dt>
											<dd class="ftsnm" style="overflow: hidden;text-overflow: ellipsis;white-space: nowrap;"></dd>
										</dl>
									</li>
									<li>
										<dl>
											<dt>중량</dt>
											<dd class="cowSogWt"></dd>
										</dl>
									</li>
									<li>
										<dl>
											<dt>산차</dt>
											<dd class="matime"></dd>
										</dl>
									</li>
									<li>
										<dl>
											<dt>어미</dt>
											<dd class="mcowDsc"></dd>
										</dl>
									</li>
									<li>
										<dl>
											<dt>계대</dt>
											<dd class="sraIndvPasgQcn"></dd>
										</dl>
									</li>
									<li>
										<dl>
											<dt>KPN</dt>
											<dd class="kpnNo"></dd>
										</dl>
									</li>
									<li>
										<dl>
											<dt>최저가</dt>
											<dd class="lowsSbidLmtAm"></dd>
										</dl>
									</li>
									<li class="harf-list">
										<dl>
											<dt>비고</dt>
											<dd>
												<p class="rmkCntn move-txt" style="width:150px"></p>
											</dd>
										</dl>
									</li>
									<li style="display:none;">
										<dl>
											<dt>친자여부</dt>
											<dd class="dnaYn"></dd>
										</dl>
									</li>
									<li style="display:none;">
										<dl>
											<dt>지역명</dt>
											<dd class="sraPdRgnnm"></dd>
										</dl>
									</li>
								</ul>
							</div>
						</li>
						<c:forEach begin="1" end="${johapData.KKO_SVC_CNT}" varStatus="st">
							<li class="video_item" style="width: 100%;height: auto;">
								<video id="remoteVideo${st.index }" style="width: 100%;background: black;height: 100%;" poster="/static/images/assets/no_video_18980.png" muted="muted" autoplay playsinline webkit-playsinline>
									Your browser does not support HTML5 video.
								</video>
							</li>
						</c:forEach>
					</ul>
				</div>
			</div>
			<!-- //videoSlide e -->
		</div>
		<!-- //auction_seeBox e -->
		<!-- //auction_calculator s -->
		<div class="auction_calculator">
			<div class="calculator_top">
				<div class="auc-txt">
					<div class="info_board" style="min-height:37px;"><span class="txt-yellow">경매 번호</span>를 입력하세요.</div>
					<div class="message_board" style="min-height:37px;z-index: -1000;position: absolute;top: 0px;left: 0px;background: rgb(230, 239, 255);min-width:100%;"></div>
					<a href="javascript:;" class="pop-btn btn_popup">팝업</a>
				</div>
				<dl class="aucNum num-bun" style="background: #ffaf00;">
					<dt>경매<br/>번호</dt>
					<dd><input type="text" class="aucNum active" id="auction_num" name="aucNum" style="background: #ffaf00;" placeholder="" readonly /><span>번</span></dd>
				</dl>
				<dl class="bidAmt" style="display:none;">
					<dt>응찰<br/>금액</dt>
					<dd><input type="text" class="bidAmt" id="gang-calculator" name="bidAmt" placeholder="0" readonly /></dd>
				</dl>
			</div>
			<!-- //calculator_top e -->
			<div class="calculator_bottom">
				<ul class="calculator-cell">
					<li><button class="num" value="7">7</button></li>
					<li><button class="num" value="8">8</button></li>
					<li><button class="num" value="9">9</button></li>
					<li><button class="num_back" value="num_back">←</button></li>
				</ul>
				<ul class="calculator-cell ty3">
					<li><button class="num" value="4">4</button></li>
					<li><button class="num" value="5">5</button></li>
					<li><button class="num" value="6">6</button></li>
				</ul>
				<ul class="calculator-cell ty3">
					<li><button class="num" value="1">1</button></li>
					<li><button class="num" value="2">2</button></li>
					<li><button class="num" value="3">3</button></li>
				</ul>
				<ul class="calculator-cell ty3">
					<li><button class="num_cancle btn_bid_cancel" value="clear">취소</button></li>
					<li><button class="num" value="0">0</button></li>
					<li><button class="num_before btn_before" value="before" disabled="disabled">전 화면</button></li>
				</ul>
				<ul class="calculator-cell btn-summit">
					<li class="aucNum"><button class="num_summit btn_confirm">확인</button></li>
					<li class="bidAmt" style="display:none;"><button class="num_summit btn_bid" disabled="disabled">응찰</button></li>
				</ul>
			</div>
			<!-- //calculator_bottom e -->
		</div>
		<!-- //auction_calculator e -->
		<!-- //list_table s -->
		<div class="list_table auction_see">
			<div class="list_head">
				<dl>
					<dd class="date">경매일자</dd>
					<dd class="num"><span class="w_view_in">경매</span>번호</dd>
					<dd class="name">출하주</dd>
					<dd class="pd_ea">개체번호</dd>
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
				<c:choose>
					<c:when test="${!empty auctionList}">
						<c:forEach items="${auctionList}" var="vo" varStatus="st">
						<li class="${st.index == 0?'act':'' }">
							<dl>
								<dd class="date aucDt">${ vo.AUC_DT_STR }</dd>
								<dd class="num aucPrgSq">${ vo.AUC_PRG_SQ }</dd>
								<dd class="name ftsnm" style="white-space: nowrap;overflow: hidden;text-overflow: ellipsis;">${ vo.SRA_PDMNM }</dd>
								<dd class="pd_ea sraIndvAmnno">${ vo.SRA_INDV_AMNNO_FORMAT }</dd>
								<dd class="pd_sex indvSexC">${ vo.INDV_SEX_C_NAME }</dd>
								<dd class="pd_kg cowSogWt">${fn:split(vo.COW_SOG_WT,'.')[0]}</dd>
								<dd class="pd_kpn kpnNo">${ vo.KPN_NO_STR }</dd>
								<dd class="pd_num1 sraIndvPasgQcn">${ vo.SRA_INDV_PASG_QCN }</dd>
								<dd class="pd_pay1 lowsSbidLmtAm">${vo.LOWS_SBID_LMT_AM <= 0 ? '0' : fn:split(vo.LOWS_SBID_LMT_UPR,'.')[0]}</dd>
								<dd class="pd_pay2 sraSbidAm">${fn:split(vo.SRA_SBID_UPR,'.')[0]}</dd>
								<dd class="pd_state selSts">${ vo.SEL_STS_DSC_NAME }</dd>
								<dd class="pd_etc rmkCntn">${ vo.RMK_CNTN }</dd>
							</dl>
						</li>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<li><dl><dd>경매 출품 정보가 없습니다.</dd></dl></li>
					</c:otherwise>
				</c:choose>
				</ul>
			</div>
		</div>
		<!-- //list_table e -->
	</div>
	<!-- //auction_area e -->
	<!-- //auction_area e -->
</div>
<!-- //auction_list e -->
<!-- //pop_auction s : 예정조회, 결과조회, 응찰내역 팝업 -->
<div id="" class="modal-wrap pop_auction">
	<div class="modal-content"></div>
</div>
<!-- //pop_auction e -->