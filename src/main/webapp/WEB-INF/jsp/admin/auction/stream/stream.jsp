<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>
<script src="https://webrtc.github.io/adapter/adapter-latest.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@remotemonster/sdk/remon.min.js"></script>
<script src="/static/js/socket.io/socket.io.js"></script>

<input type="hidden" id="token" value="${token }"/>
<input type="hidden" id="naBzPlc" value="${johapData.NA_BZPLC}" />
<input type="hidden" id="aucDsc" value="${johapData.AUC_DSC}" />
<input type="hidden" id="kkoSvcId" value="${johapData.KKO_SVC_ID}" />
<input type="hidden" id="kkoSvcKey" value="${johapData.KKO_SVC_KEY}" />
<input type="hidden" id="kkoSvcCnt" value="${johapData.KKO_SVC_CNT}" />
<input type="hidden" id="webPort" value="${johapData.WEB_PORT}" />
<input type="hidden" id="listAucNum" value="${count.LIST_AUCNUM}" />
<table class="youtube-table tblAuctionSt">
	<colgroup>
		<col width="18%">
		<col width="18%">
		<col width="64%">
	</colgroup>
	<tbody>
		<tr>
			<td class="seller">
				<p class="fz40">${johapData.CLNTNM}</p>
			</td>
			<td colspan="2" rowspan="2">
				<table class="inner-table">
					<colgroup>
						<col width="20%">
						<col width="20%">
						<col width="20%">
						<col width="20%">
						<col width="20%">
					</colgroup>
					<tbody>
						<tr>
							<td>
								<p class="txt-green fz40">출하주</p>
								<p class="fz60 ftsnm"> - </p>
							</td>
							<td>
								<p class="txt-green fz40">성별</p>
								<p class="fz60 sex"> - </p>
							</td>
							<td>
								<p class="txt-green fz40">중량(Kg)</p>
								<p class="fz60 cowSogWt"> - </p>
							</td>
							<td>
								<p class="txt-green fz40">어미</p>
								<p class="fz60 mcowDsc"> - </p>
							</td>
							<td>
								<p class="txt-green fz40">KPN</p>
								<p class="fz60 kpnNo"> - </p>
							</td>
						</tr>
						<tr>
							<td>
								<p class="txt-green fz40">계대</p>
								<p class="fz60 sraIndvPasgQcn"> - </p>
							</td>
							<td>
								<p class="txt-green fz40">산차</p>
								<p class="fz60 matime"> - </p>
							</td>
							<td>
								<p class="txt-green fz40">최저가</p>
								<p class="fz60 lowsSbidLmtAm"> - </p>
							</td>
							<td colspan="2">
								<p class="txt-green fz40">비고</p>
								<p class="fz60 move-txt rmkCntn" style="width:100%"> - </p>
								<!-- width 값에 따라 속도 조정 -->
							</td>
						</tr>
					</tbody>
				</table>
			</td>
		</tr>
		<tr>
			<td class="bg-blue val"><p class="fz180 auctionNum"> - </p></td>
		</tr>
		<tr class="st">
			<td class="count-td" colspan="2">
<!-- 				<p class="txt-count">경매마감 <span id="countdown"><b></b></span></p> -->
				<p class="txt-count">경매 대기중</p>
			</td>
			<td class="complate" style="display:none;">
				<p class="txt-green fz60">낙찰금액</p>
				<p class="fz120 tdBiddAmt">300</p>
			</td>
			<td class="complate" style="display:none;">
				<p class="txt-green fz60">낙찰번호</p>
				<p class="fz120 tdBiddNum">2</p>
			</td>
			<td rowspan="2">
				<div class="seeBox_bottom vidioSlide">
					<div class="seeBox_slick">
						<ul class="slider">									
							<c:forEach begin="1" end="${(johapData.KKO_SVC_CNT eq '' or johapData.KKO_SVC_CNT == null) ? '0' : johapData.KKO_SVC_CNT}" varStatus="st">
								<li class="video_item" style="width: 100%;height: auto;">
									<video id="remoteVideo${st.index }" style="width: 100%;background: black;max-height:50vh;" poster="/static/images/assets/no_video_18980.png" muted="muted" autoplay playsinline webkit-playsinline>
										Your browser does not support HTML5 video.
									</video>
								</li>
							</c:forEach>
						</ul>
					</div> 
				</div>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<div class="seeBox_gp">
					<div class="chart" data-percent="${count.CNT == 0 ? '0' : count.CNT_PERCENT }">
						<span class="count">${count.CNT_PERCENT }%</span>						
					</div>					
					<ul class="chart_label">
						<li class="gp_tit1">완료 <span>${count.CNT_STAND_ELSE }</span></li>
						<li class="gp_tit2">대기 <span>${count.CNT_STAND }</span></li>
					</ul>
				</div>
			</td>
		</tr>
	</tbody>
</table>