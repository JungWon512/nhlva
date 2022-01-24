<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>
<script src="https://webrtc.github.io/adapter/adapter-latest.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@remotemonster/sdk/remon.min.js"></script>

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
		<tr class="st">
			<td class="noCss">
				<div class="seeBox_bottom vidioSlide" style="max-height:99vh;width:99vw;">
					<div class="seeBox_slick">
						<ul class="slider">									
							<c:forEach begin="1" end="${(johapData.KKO_SVC_CNT eq '' or johapData.KKO_SVC_CNT == null) ? '0' : johapData.KKO_SVC_CNT}" varStatus="st">
								<li class="video_item" style="width: 100%;height: auto;">
									<video id="remoteVideo${st.index }" style="width: 100%;height: 95vh;background: black;" poster="/static/images/assets/no_video_18980.png" muted="muted" autoplay playsinline webkit-playsinline>
										Your browser does not support HTML5 video.
									</video>
								</li>
							</c:forEach>
						</ul>
					</div> 
				</div>
			</td>
		</tr>
	</tbody>
</table>