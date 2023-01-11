<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<style type="text/css">
	#preview_list > div.active {display : block !important;}
	#preview_list > div {display : none !important;}
	#preview_list canvas {
		width: 100% !important;
	}
</style>
<div class="admin_new_reg admin_pic_reg">
	<c:if test="${!empty imgList}">
		<c:forEach items="${imgList}" var="ivo" varStatus="s">
			<input type="hidden" name="file_url" value="${ivo.FILE_URL}" />
		</c:forEach>
	</c:if>
		<form name="frm" action="" method="post" autocomplete="off">
		<input type="hidden" name="aucDt" value="${sogCowInfo.AUC_DT}" />
			<input type="hidden" name="naBzplc" value="<sec:authentication property="principal.naBzplc"/>" />
			<input type="hidden" name="aucObjDsc" value="${sogCowInfo.AUC_OBJ_DSC}" />
			<input type="hidden" name="oslpNo" value="${sogCowInfo.OSLP_NO}" />
			<input type="hidden" name="ledSqno" value="${sogCowInfo.LED_SQNO}" />
			<input type="hidden" name="aucPrgSq" value="${sogCowInfo.AUC_PRG_SQ}" />
			<input type="hidden" name="sraIndvAmnno" value="${sogCowInfo.SRA_INDV_AMNNO}" />
			
		<div class="save-box">
			<div class="info">
				<em>${sogCowInfo.AUC_OBJ_DSCNM} |</em>
				<em>${sogCowInfo.INDV_SEX_CNM} |</em>
				<em>${fn:substring(sogCowInfo.SRA_INDV_AMNNO, 3, 6)} ${fn:substring(sogCowInfo.SRA_INDV_AMNNO, 6, 10)} ${fn:substring(sogCowInfo.SRA_INDV_AMNNO, 10, 14)} ${fn:substring(sogCowInfo.SRA_INDV_AMNNO, 14, 15)} </em>
			</div>
			<button type="button" class="btn-save btn_save">저장</button>
			</div>
			
		<div class="file-add-box">
			<div id="thumbnail_list" class="inner">
				<div class="add-item add-pic">
					<label for="file-input">사진 추가 <span class="num">0 / 8</span></label>
					<input id="file-input" type="file" accept="image/*" capture="camera" multiple="multiple" max="8" />
				</div>
			</div>
		</div>
		<div id="preview_list" class="cow-imgBox"></div>
		<div class="top-btns">
			<button type="button" class="btn_delete">삭제</button>
			<button type="button" class="btn_rotate">회전</button>
	</div>
	</form>
</div>
