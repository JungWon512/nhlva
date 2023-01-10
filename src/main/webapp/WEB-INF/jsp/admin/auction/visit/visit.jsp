<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>
<!--begin::Container-->
<style>
.overlay_modal {
  display: block;
  position: fixed;
  left: 0;
  top: 0;
  width: 100%;
  bottom: 0;
  background: #000;
  opacity: 0.2;
  z-index: 10000;
}
</style>
<div class="modal" tabindex="-1" role="dialog" style="z-index: 10001;">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-body">        
		<div class="progress">
		  <div class="progress-bar" role="progressbar" style="width: 0%;" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100"></div>
		</div>
      </div>
    </div>
  </div>
</div>
<div class="container">
	<div class="row">
		<div class="col-xl-12">
			<!--begin::Notice-->
			<div class="card card-custom gutter-b">
				<div class="card-header">
					<div class="card-title">
						<h3 class="card-label">접속현황</h3>
					</div>
				</div>
				<div class="card-body">
					<div class="input-group mb-3" style="width:45%;float:left;">
						<div class="input-group-prepend">
							<span class="input-group-text" id="inputGroup-sizing-default">날짜</span>
						</div>
						<input id="searchYear" type="text" class="form-control" aria-label="검색년도" aria-describedby="검색년도" maxlength="6"/>
					</div>
					<div class="btn-group" role="group" aria-label="Basic example" style="float:right;">
						<button type="button" id="btnSearch" class="btn btn-secondary" maxlength=6>검색</button>
					</div>
				</div>
				<div class="card-body">
					<!--begin: Datatable-->
<%--					<div class="datatable table-striped datatable-bordered datatable-head-custom" id="kt_datatable"></div>--%>
					<table id="example" class="table table-striped table-bordered" style="width:100%">
						<thead>
						<tr>
							<th width="">번호</th>
							<th width="">IP</th>
							<th width="">AGENT</th>
							<th width="">REFFER</th>
							<th width="">DTM</th>
						</tr>
						</thead>
						<tbody>
							<c:forEach items="${ resultData }" var="vo">
								<tr data-seqNo="${vo.SEQ_NO}">
									<td>${vo.SEQ_NO}</td>
									<td>${vo.VISIT_IP}</td>
									<td>${vo.VISIT_AGENT}</td>
									<td>${vo.VISIT_REFER}</td>
									<td>${vo.VISIT_DTM}</td>
								</tr>
							</c:forEach>


					</table>
					<!--end: Datatable-->
				</div>
			</div>
		</div>
	</div>
</div>
<!--${data} -->
<!--end::Container-->
<input type="hidden" id="johpCdVal" value="${johapData.NA_BZPLC}" />