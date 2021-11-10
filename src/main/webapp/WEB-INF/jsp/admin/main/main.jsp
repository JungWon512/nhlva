<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>
<!-- Page Content -->
<div class="container">
	<h4 class="font-weight-light text-center text-lg-left mt-4 mb-0">Main</h4>
	<hr class="mt-2 mb-2">  
	<div class="card-deck">
		<div class="card">
			<div class="card-body">
				<a href="javascript:pageMove('/admin/task/main');" class="card-link">경매업무</a>
			</div>
		</div>
		<div class="card">
			<div class="card-body">
				<a href="javascript:pageMove('/admin/broad/cast');" class="card-link">영상송출</a>
			</div>
		</div>
		<div class="card">
			<div class="card-body">
				<a href="javascript:pageMove('/admin/auction/monster');" class="card-link">모니터링</a>
			</div>
		</div>
		<div class="card">
			<div class="card-body">
				<a href="javascript:pageMove('/admin/auction/board');" class="card-link">멀티비젼</a>
			</div>
		</div>
<!-- 		<div class="card"> -->
<!-- 			<div class="card-body"> -->
<!-- 				<a href="javascript:pageMove('/admin/auction/aucNotice');" class="card-link">공지사항</a> -->
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 	    <div class="card"> -->
<!-- 	    <div class="card-body"> -->
<!-- 	      <a href="javascript:pageMove('/admin/report/list');" class="card-link">접속현황</a> -->
<!-- 	    </div> -->
<!-- 	  </div> -->
	</div>  
</div>
<!-- /.container -->