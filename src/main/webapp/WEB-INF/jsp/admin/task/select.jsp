<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>
<style type="text/css">
	.card-deck .card {
	    margin-bottom: 12.5px;
	    width: 48%;
	    float: left;
	    margin: 3px 3px;
	}
</style>
<!-- Page Content -->
<div class="container">
<form name="frm_select" action="" method="post">
	<input type="hidden" name="aucDt" value="${params.aucDt}" />
	<input type="hidden" name="aucObjDsc" value="${params.aucObjDsc}" />
	<input type="hidden" name="regType" value="" />
</form>
	<h4 class="font-weight-light text-center text-lg-left mt-4 mb-0">${params.aucDt}</h4>
	<h4 class="font-weight-light text-center text-lg-left mt-4 mb-0">작업선택</h4>
	<hr class="mt-2 mb-2">
	<div class="card-deck">
		<div class="card">
			<div class="card-body">
				<a href="javascript:;" class="card-link btn_move" data-type="W">중량등록</a>
			</div>
		</div>
		<div class="card">
			<div class="card-body">
				<a href="javascript:;" class="card-link btn_move" data-type="L">하한가등록</a>
			</div>
		</div>
		<div class="card">
			<div class="card-body">
				<a href="javascript:;" class="card-link btn_move" data-type="N">계류대변경</a>
			</div>
		</div>
		<div class="card">
			<div class="card-body">
				<a href="javascript:;" class="card-link btn_move" data-type="I">정보조회</a>
			</div>
		</div>
		<div class="card">
			<div class="card-body">
				<a href="javascript:;" class="card-link btn_move" data-type="B">낙찰조회</a>
			</div>
		</div>
	</div>
</div>
<!-- /.container -->