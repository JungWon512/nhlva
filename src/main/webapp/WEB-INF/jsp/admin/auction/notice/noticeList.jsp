<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>
<!--begin::Container-->
<div class="container">
	<div class="row mt-20">
		<div class="col-xl-12">
			<!--begin::Notice-->
			<div class="card card-custom gutter-b">
				<div class="card-header">
					<div class="card-title">
						<h3 class="card-label h4">공지사항</h3>
					</div>
					<a href="javascript:pageMove('/admin/auction/aucNotice/${johapData.NA_BZPLC}/0');" class="btn btn-success h-25 mt-4 mr-2  new_notice">신규등록</a>
				</div>
				<div class="card-body">
					<!--begin: Datatable-->
<%--					<div class="datatable table-striped datatable-bordered datatable-head-custom" id="kt_datatable"></div>--%>
					<table id="example" class="table table-striped table-bordered" style="width:100%">
						<thead>
						<tr>
							<th width="10%">번호</th>
							<th >제목</th>
							<th width="10%">등록자</th>
							<th width="10%">등록일</th>
							<th width="10%">조회수</th>
							<th width="10%">삭제여부</th>
							<th width="20%">수정/ 삭제</th>
						</tr>
						</thead>
						<tbody>


							<c:forEach items="${ noticeData }" var="vo">
							<tr data-seqNo="${vo.SEQ_NO}">
								<td>${vo.SEQ_NO}</td>
								<td>${vo.TITLE}</td>
								<td>${vo.FSRGMN_ENO}</td>
								<td>${vo.FSRG_DTM}</td>
								<td> 0 </td>
								<td>${vo.DEL_YN?'Y':'N'}</td>
								<td>
									<a href="javascript:pageMove('/admin/auction/aucNotice/${johapData.NA_BZPLC}/${vo.SEQ_NO}');" class="modify-notice"><i class="la la-edit"></i>[수정]</a>
									<a href="javascript:;" class="delete-notice"><i class="la la-trash"></i> [삭제]</a>
								</td>
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