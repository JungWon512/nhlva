<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>

<style type="text/css">
	.admin_auc_main .main_search ul {
	    margin: 0px 0;
	    display: flex;
	    flex-direction: row;
	    flex-wrap: wrap;
	    align-content: center;
	    justify-content: flex-start;
	    overflow: scroll;
	}
	
	.admin_auc_main .main_search ul li {
		padding : 5px;
	}
</style>

<div class="admin_auc_main">
	<h3>경매일자 입력</h3>
<!-- 	<div class="main_search"> -->
<!-- 		<ul class="choice_area"> -->
<!-- 			<li> -->
<!-- 				<img src="/static/images/guide/favicon128.png" width="100px;" /> -->
<!-- 			</li> -->
<!-- 			<li> -->
<!-- 				<img src="/static/images/guide/favicon128.png" width="100px;" /> -->
<!-- 			</li> -->
<!-- 			<li> -->
<!-- 				<img src="/static/images/guide/favicon128.png" width="100px;" /> -->
<!-- 			</li> -->
<!-- 			<li> -->
<!-- 				<img src="/static/images/guide/favicon128.png" width="100px;" /> -->
<!-- 			</li> -->
<!-- 			<li> -->
<!-- 				<img src="/static/images/guide/favicon128.png" width="100px;" /> -->
<!-- 			</li> -->
<!-- 			<li> -->
<!-- 				<img src="/static/images/guide/favicon128.png" width="100px;" /> -->
<!-- 			</li> -->
<!-- 		</ul> -->
<!-- 	</div> -->
	<div id="image_area">
		<input type="file" name="file1" id="file1" value="" accept=".gif, .jpg, .png" />
	</div>
	<div class="data"><p>DATA</p><table style="width:100%;"><colgroup><col width="40%"/><col width="60%"/></colgroup></table></div>
	<div class="exif"><p>EXIF</p><table style="width:100%;"><colgroup><col width="40%"/><col width="60%"/></colgroup></table></div>
	<div class="iptc"><p>IPTC</p><table style="width:100%;"><colgroup><col width="40%"/><col width="60%"/></colgroup></table></div>
	
</div>