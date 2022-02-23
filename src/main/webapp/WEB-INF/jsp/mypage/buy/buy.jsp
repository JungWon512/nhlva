<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<input type="hidden" id="loginNo" name="loginNo" value="${inputParam.loginNo}"/>
<input type="hidden" id="naBzPlcNo" value="${johapData.NA_BZPLCNO}"/>
<input type="hidden" id="johpCd" value="${johapData.NA_BZPLC}"/>
<div class="auction_list">
	<h3>구매내역</h3>
	<div class="tab_list">
		<ul>
			<li><a href="javascript:;" class="act" data-tab-id="buy">구매내역</a></li>
			<li><a href="javascript:;" data-tab-id="bid">응찰내역</a></li>
		</ul>
	</div>
	<!-- //tab_list e -->
	<div class="tab_area auction_list buy_list buy" style="display:none;">
		<div class="list_search">
			<ul class="radio_group">
	            <li>
	                <input type="radio" name="searchAucObjDscBuy" checked id="ra1" value=""><label for="ra1">전체</label>
	            </li>
	            <li>
	                <input type="radio" name="searchAucObjDscBuy" id="ra2" value="1" <c:if test="${inputParam.searchAucObjDscBuy eq '1'}">
	                checked
	                </c:if>><label for="ra2">송아지</label>
	            </li>
	            <li>
	                <input type="radio" name="searchAucObjDscBuy" id="ra3" value="2" <c:if test="${inputParam.searchAucObjDscBuy eq '2'}">
	                       checked
	                </c:if>><label for="ra3">비육우</label>
	            </li>
	            <li>
	                <input type="radio" name="searchAucObjDscBuy" id="ra4" value="3" <c:if test="${inputParam.searchAucObjDscBuy eq '3'}">
	                       checked
	                </c:if>><label for="ra4">번식우</label>
	            </li>
			</ul>
			<ul class="sch_area">
	            <li class="txt">
	            	<input type="text" name="searchTxtBuy" id="searchTxtBuy" maxlength="4" placeholder="개체" value="${inputParam.searchTxtBuy}"/>
	           	</li>
				<li class="date">
	                <select name="searchDateBuy" id="searchDateBuy">
		                <option value=""> 선택 </option>
		                <c:forEach items="${ dateList }" var="vo">
		                	<option value="${vo.AUC_DT }" ${inputParam.searchDate eq vo.AUC_DT ? 'selected' : ''}> ${vo.AUC_DT_STR } </option>
		                </c:forEach>
	                </select>
				</li>
				<li class="btn">
					<a href="javascript:;" class="list_sch sch_buy">검색</a>
				</li>
			</ul>
		</div>
		<!-- //list_search e -->
		<div class="list_downs">
			<ul>
				<li><a href="javascript:;" class="btn_printBuy"><span class="ico_print">인쇄하기</span></a></li>
				<li><a href="javascript:;" class="btn_excelBuy"><span class="ico_excel">엑셀다운</span></a></li>
			</ul>
		</div>
		<div class="sum_table">
			<div>
				<dl>
					<dt><p>암</p></dt>
					<dd>
						<p class="cowCnt">${buyCnt.CNT_SEX_W}두</p>
					</dd>
				</dl>
				<dl>
					<dt><p>수</p></dt>
					<dd>
						<p class="cowCnt">${buyCnt.CNT_SEX_M}두</p>
					</dd>
				</dl>
				<dl>
					<dt><p>기타</p></dt>
					<dd>
						<p class="cowCnt">${buyCnt.CNT_SEX_ETC}두</p>
					</dd>
				</dl>
			</div>
			<div class="sumTxt">
				<fmt:formatNumber value="${totPrice.SRA_SBID_AM}" type="number" var="TOT_SRA_SBID_AM"/>
				<p>총 금액 : ${empty TOT_SRA_SBID_AM?'0':TOT_SRA_SBID_AM} 원</p>
			</div>
		</div>
		<!-- //list_downs e -->
		<div class="list_table auction_result">
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
					<c:if test="${buyList.size() <= 0}">
						<li>
							<dl>
								<dd>
									검색결과가 없습니다.
								</dd>
							</dl>
						</li>
					</c:if>
					<c:forEach items="${buyList }" var="item" varStatus ="st">
						<li>
							<dl>
								<dd class="date">${ item.AUC_DT_STR }</dd>
								<dd class="num">${ item.AUC_PRG_SQ }</dd>
								<dd class="name">${ item.FTSNM }</dd>
								<dd class="pd_ea">${ item.SRA_INDV_AMNNO_FORMAT }</dd>
								<dd class="pd_sex">${ item.INDV_SEX_C_NAME }</dd>
								<dd class="pd_kg">
									<c:choose>
										<c:when test="${empty item.COW_SOG_WT || item.COW_SOG_WT <= 0}">-</c:when>
										<c:otherwise>
											<fmt:formatNumber value="${fn:split(item.COW_SOG_WT,'.')[0]}" type="number" />
										</c:otherwise>
									</c:choose>
								</dd>
								<dd class="pd_kpn">${ item.KPN_NO_STR }</dd>
								<dd class="pd_num1">${ item.SRA_INDV_PASG_QCN }</dd>
								<dd class="pd_pay1">
									<c:choose>
										<c:when test="${empty item.LOWS_SBID_LMT_AM || item.LOWS_SBID_LMT_AM <= 0}">-</c:when>
										<c:otherwise>
											<fmt:formatNumber value="${fn:split(item.LOWS_SBID_LMT_UPR,'.')[0]}" type="number" />
										</c:otherwise>
									</c:choose>
								</dd>
								<dd class="pd_pay2">
									<c:choose>
										<c:when test="${empty item.SRA_SBID_UPR || item.SRA_SBID_UPR <= 0}">-</c:when>
										<c:otherwise>
											<fmt:formatNumber value="${fn:split(item.SRA_SBID_UPR,'.')[0]}" type="number" />
										</c:otherwise>
									</c:choose>
								</dd>
								<dd class="pd_state">${ item.SEL_STS_DSC_NAME }</dd>
								<dd class="pd_etc">${ item.RMK_CNTN }</dd>
							</dl>
						</li>
					</c:forEach>
				</ul>
			</div>
		</div>
		<!-- //auction_buy e -->
	</div>
	<!-- //tab_area e -->
	<div class="tab_area bid" style="display:none;">
		<div class="list_search">
			<ul class="radio_group">
	            <li>
	                <input type="radio" name="searchAucObjDscBid" checked id="ra5" value=""><label for="ra5">전체</label>
	            </li>
	            <li>
	                <input type="radio" name="searchAucObjDscBid" id="ra6" value="1" <c:if test="${inputParam.searchAucObjDsc eq '1'}">
	                checked
	                </c:if>><label for="ra6">송아지</label>
	            </li>
	            <li>
	                <input type="radio" name="searchAucObjDscBid" id="ra7" value="2" <c:if test="${inputParam.searchAucObjDsc eq '2'}">
	                       checked
	                </c:if>><label for="ra7">비육우</label>
	            </li>
	            <li>
	                <input type="radio" name="searchAucObjDscBid" id="ra8" value="3" <c:if test="${inputParam.searchAucObjDsc eq '3'}">
	                       checked
	                </c:if>><label for="ra8">번식우</label>
	            </li>
			</ul>
			<ul class="sch_area">
	            <li class="txt">
	            	<input type="text" name="searchTxtBid" id="searchTxtBid" maxlength="4" placeholder="개체" value="${inputParam.searchTxtBid}"/>
	           	</li>
				<li class="date">
	                <select name="searchDateBid" id="searchDateBid">
		                <option value="" ${(inputParam.searchDate == null || inputParam.searchDate == '')? 'selected' : ''}> 선택 </option>                
		                <c:forEach items="${ dateList }" var="vo">
		                	<option value="${vo.AUC_DT }" ${inputParam.searchDate eq vo.AUC_DT ? 'selected' : ''}> ${vo.AUC_DT_STR } </option>
		                </c:forEach>
	                    
	                </select>
				</li>
				<li class="btn">
					<a href="javascript:;" class="list_sch sch_bid">검색</a>
				</li>
			</ul>
		</div>
		<!-- //list_search e -->
		<div class="list_downs">
			<ul>
				<li><a href="javascript:;"><span class="ico_print">인쇄하기</span></a></li>
				<li><a href="javascript:;"><span class="ico_excel">엑셀다운</span></a></li>
			</ul>
		</div>
		<!-- //list_downs e -->
		<div class="list_table auction_bid">
			<div class="list_head">
				<dl>
					<dd class="date">경매일자</dd>
					<dd class="num"><span class="w_view_in">경매</span>번호</dd>
					<dd class="pd_ea">개체<span class="w_view_in">번호</span></dd>
					<dd class="pd_sex">성별</dd>
					<dd class="pd_kg">중량<span class="w_view_in">(kg)</span></dd>
					<dd class="pd_pay1">예정가</dd>
					<dd class="pd_pay3">응찰가</dd>
					<dd class="pd_pay2">낙찰가</dd>
					<dd class="pd_state">경매상태</dd>
				</dl>
			</div>
			<div class="list_body">
				<ul class="mCustomScrollBox">
					<c:if test="${bidList.size() <= 0}">
						<li>
							<dl>
								<dd>
									검색결과가 없습니다.
								</dd>
							</dl>
						</li>
					</c:if>
					<c:forEach items="${bidList }" var="item" varStatus ="st">
						<li>
							<dl>
								<dd class="date">${item.AUC_DT_STR }</dd>
								<dd class="num">${item.AUC_PRG_SQ }</dd>
								<dd class="pd_ea">${item.SRA_INDV_AMNNO_FORMAT }</dd>
								<dd class="pd_sex">${ item.INDV_SEX_C_NAME}</dd>
								<dd class="pd_kg">
									<c:choose>
										<c:when test="${empty item.COW_SOG_WT || item.COW_SOG_WT <= 0}">-</c:when>
										<c:otherwise>
											<fmt:formatNumber value="${fn:split(item.COW_SOG_WT,'.')[0]}" type="number" />
										</c:otherwise>
									</c:choose>
								</dd>
								<dd class="pd_pay1">
									<c:choose>
										<c:when test="${empty item.LOWS_SBID_LMT_AM || item.LOWS_SBID_LMT_AM <= 0}">-</c:when>
										<c:otherwise>
											<fmt:formatNumber value="${fn:split(item.LOWS_SBID_LMT_UPR,'.')[0]}" type="number" />
										</c:otherwise>
									</c:choose>
								</dd>
								<dd class="pd_pay3">
									<c:choose>
										<c:when test="${empty item.ATDR_AM || item.ATDR_AM <= 0}">-</c:when>
										<c:otherwise>
											<fmt:formatNumber value="${fn:split(item.ATDR_AM,'.')[0]}" type="number" />
										</c:otherwise>
									</c:choose>
								</dd>
								<dd class="pd_pay2">
									<c:choose>
										<c:when test="${empty item.SRA_SBID_UPR || item.SRA_SBID_UPR <= 0}">-</c:when>
										<c:otherwise>
											<fmt:formatNumber value="${fn:split(item.SRA_SBID_UPR,'.')[0]}" type="number" />
										</c:otherwise>
									</c:choose>
								</dd>
								<dd class="pd_state">${item.SEL_STS_DSC_NAME }</dd>
							</dl>
						</li>
					</c:forEach>
				</ul>
			</div>
		</div>
		<!-- //auction_bid e -->
	</div>
	<!-- //tab_area e -->
</div>
<!-- //auction_list e -->