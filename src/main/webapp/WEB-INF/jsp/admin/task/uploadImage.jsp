<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<style type="text/css">
	.num_scr li+li a {
		display: inline-block;
		width: 100%;
		text-align: center;
		height: 40px;
		line-height: 40px;
		padding: 0 5px;
		background: #007eff;
		color: #fff;
		font-size: 18px;
		font-weight: 700;
		letter-spacing: -0.45px;
		border-radius: 5px;
		-webkit-border-radius: 5px;
		-moz-border-radius: 5px;
		-ms-border-radius: 5px;
		-o-border-radius: 5px;
	}
	.num_scr {
		padding: 5px 10px;
	}
	.num_scr li {
		width: 70%;
		float: left;
		padding-right: 5px;
	}
	.num_scr li p {
		line-height: 40px;
		background-color: #f5f5f5;
		border-radius: 5px;
		padding: 0 10px;"
	}
	.num_scr li+li {
		width: 30%;
		padding-left: 5px;
		padding-right: 0;
	}
	.admin_regist_form .btn_area {
		max-width: 600px;
		width: 60% !important;
		margin: 30px auto 0 !important;
		display: -webkit-box;
		display: -ms-flexbox;
		display: flex;
		-webkit-box-pack: center;
		-ms-flex-pack: center;
		justify-content: center;
		-webkit-box-align: center;
		-ms-flex-align: center;
		align-items: center;
		padding: 0 5px;
		float: right; 
	}
	.info_area {
		height : 50px;
	}
	.list_area {
		height : 100px;
		border: 1px solid #f5f5f5;
	}
	.list_area ul {
		display :flex;
		overflow: scroll;
	}
	.list_area img {
		padding : 5px 10px;
	}
	#preview_list li {
		display: none;
	}
	#preview_list li.active {
		display: block;
	}
	#preview_area canvas {
		width: 100% !important;
		heigth:100% !important;
	}
	#thumbnail_list canvas {
		width: 90px !important;
		height:90px !important;
	}
</style>
<div class="admin_auc_main">
	<!--  admin_regist_form [s] -->
	<div class="admin_regist_form">
		<form name="frm" action="" method="post" autocomplete="off">
			<input type="hidden" name="aucDt" value="${params.aucDt}" />
			<input type="hidden" name="naBzplc" value="<sec:authentication property="principal.naBzplc"/>" />
			<input type="hidden" name="aucObjDsc" value="${sogCowInfo.AUC_OBJ_DSC}" />
			<input type="hidden" name="oslpNo" value="${sogCowInfo.OSLP_NO}" />
			<input type="hidden" name="ledSqno" value="${sogCowInfo.LED_SQNO}" />
			<input type="hidden" name="aucPrgSq" value="${sogCowInfo.AUC_PRG_SQ}" />
			<input type="hidden" name="sraIndvAmnno" value="${sogCowInfo.SRA_INDV_AMNNO}" />
			
			<div class="info_area">
				<ul class="num_scr">
					<li><p>${sogCowInfo.AUC_OBJ_DSC} | ${sogCowInfo.INDV_SEX_C} | ${sogCowInfo.SRA_INDV_AMNNO}</p></li> 
					<li><a href="javascript:;" class="btn_save">저장</a></li>
				</ul>
			</div>
			<div class="info_area">
				<ul class="num_scr">
					<li><input type="text" name="file_url" value="http://www.nhlva.co.kr/static/images/guide/pc_banner.jpg" /></li>
					<li><input type="text" name="file_url" value="http://www.nhlva.co.kr/static/images/guide/new_mo_banner.jpg" /></li>
				</ul>
			</div>
			<input type="file" id="file-input" name="file-input" accept=".jpg, .png, .gif" multiple="multiple" max="5"/>
			
			<div class="list_area">
				<ul id="thumbnail_list" class="thumbnail_list"></ul>
			</div>
			<div id="preview_area" class="preview_area">
				<ul id="preview_list">
				</ul>
			</div>
			<div class="btn_area">
				<a href="javascript:;" class="btn_rotate">회전</a> 
			</div>
			<!-- div class="data" style="margin-top:90px;"><p>DATA</p><table style="width:100%;"><colgroup><col width="40%"/><col width="60%"/></colgroup></table></div>
			<div class="exif"><p>EXIF</p><table style="width:100%;"><colgroup><col width="40%"/><col width="60%"/></colgroup></table></div>
			<div class="iptc"><p>IPTC</p><table style="width:100%;"><colgroup><col width="40%"/><col width="60%"/></colgroup></table></div -->
			<div id="meta"></div>
		</form>
	</div>
	<!--  admin_regist_form [e] -->
</div>
