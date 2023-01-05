<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<form name="frm_secAply" id="frm_secAply">
<input type="hidden" id="loginNo" name="loginNo" value="${inputParam.loginNo}"/>
<input type="hidden" id="place" name="place" value="${johapData.NA_BZPLCNO}"/>
<input type="hidden" id="johpCd" name="johpCd" value="${johapData.NA_BZPLC}"/>

<div class="div_na_bzplc">
	<p class="title_text"><span class="txt_blue">*</span> 이용해지 신청 대상</p>
	<ul class="radio_group">
       <li class="select_na_bzplc">
           <input type="radio" name="selelctNaBzPlc" id="all_bzplc" value="ALL" checked><label for="all_bzplc">전체 조합</label>
       </li>
       <li class="select_na_bzplc">
           <input type="radio" name="selelctNaBzPlc" id="select_bzplc" value="${johapData.NA_BZPLC}"><label for="select_bzplc">해당 조합</label>
       </li>
    </ul>
	<p class="title_text mt30"><span class="txt_blue">*</span> 이용해지 신청 사유</p>
	<div class="inp">
		<textarea name="sec_reason"></textarea>
	</div>
	<div class="btn_area">
		<a href="javascript:;" class="btn_apply">신청하기</a>
	</div>
</div>
</form>