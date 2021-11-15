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
<!-- 				<li class="sort"> -->
<!-- 					<select name="" id=""> -->
<!-- 						<option value="">정렬</option> -->
<!-- 					</select> -->
<!-- 				</li> -->	
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
		<!-- //list_downs e -->
<!-- 		<div class="list_table auction_buy"> -->
		<div class="list_table auction_result">
<!-- 			<div class="list_head"> -->
<!-- 				<dl> -->
<!-- 					<dd class="date">경매일자</dd> -->
<!-- 					<dd class="num"><span class="w_view_in">참가</span>번호</dd> -->
<!-- 					<dd class="pd_total">총금액</dd> -->
<!-- 					<dd class="pd_ea">두수</dd> -->
<!-- 					<dd class="pd_pay">평균낙찰</dd> -->
<!-- 					<dd class="pd_view">상세</dd> -->
<!-- 				</dl> -->
<!-- 			</div> -->
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
								<dd class="name">${ item.SRA_PDMNM }</dd>
								<dd class="pd_ea">${ item.SRA_INDV_AMNNO_FORMAT }</dd>
								<dd class="pd_sex">${ item.INDV_SEX_C_NAME }</dd>
								<dd class="pd_kg">${ fn:split(item.COW_SOG_WT,'.')[0] }</dd>
								<dd class="pd_kpn">${ item.KPN_NO_STR }</dd>
								<dd class="pd_num1">${ item.SRA_INDV_PASG_QCN }</dd>
								<dd class="pd_pay1">${item.LOWS_SBID_LMT_AM <= 0 ? '0' : fn:split(item.LOWS_SBID_LMT_UPR,'.')[0]}</dd>
								<dd class="pd_pay2">${ fn:split(item.SRA_SBID_UPR,'.')[0] }</dd>
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
	                <input type="radio" name="searchAucObjDscBid" checked id="ra1" value=""><label for="ra1">전체</label>
	            </li>
	            <li>
	                <input type="radio" name="searchAucObjDscBid" id="ra2" value="1" <c:if test="${inputParam.searchAucObjDsc eq '1'}">
	                checked
	                </c:if>><label for="ra2">송아지</label>
	            </li>
	            <li>
	                <input type="radio" name="searchAucObjDscBid" id="ra3" value="2" <c:if test="${inputParam.searchAucObjDsc eq '2'}">
	                       checked
	                </c:if>><label for="ra3">비육우</label>
	            </li>
	            <li>
	                <input type="radio" name="searchAucObjDscBid" id="ra4" value="3" <c:if test="${inputParam.searchAucObjDsc eq '3'}">
	                       checked
	                </c:if>><label for="ra4">번식우</label>
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
					<dd class="pd_pay2">낙찰가</dd>
					<dd class="pd_pay3">응찰가</dd>
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
								<dd class="pd_kg">${ fn:split(item.COW_SOG_WT,'.')[0] }</dd>
								<dd class="pd_pay1">${item.LOWS_SBID_LMT_AM <= 0 ? '0' : fn:split(item.LOWS_SBID_LMT_UPR,'.')[0]}</dd>
								<dd class="pd_pay2">${ fn:split(item.SRA_SBID_UPR,'.')[0] }</dd>
								<dd class="pd_pay3">${ fn:split(item.ATDR_UPR,'.')[0] }</dd>
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