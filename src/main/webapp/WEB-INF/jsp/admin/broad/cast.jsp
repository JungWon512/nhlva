<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>

<script src="https://cdn.jsdelivr.net/npm/@remotemonster/sdk/remon.min.js"></script>

<div class="container">
  	<h4 class="font-weight-light text-center text-lg-left mt-4 mb-0">영상송출</h4>
  	<hr class="mt-2 mb-2">
	<!--begin::Row-->
	<div class="row">
		<input type="hidden" id="naBzplc" name="naBzplc" value="${johapData.NA_BZPLC}" />
		<input type="hidden" id="svcId" name="svcId" value="${johapData.KKO_SVC_ID}" />
		<input type="hidden" id="svcKey" name="svcKey" value="${johapData.KKO_SVC_KEY}" />
		<div class="col-lg-6">
			<!--begin::Stats Widget 1-->
			<div class="card card-custom card-stretch gutter-b">
				<!--begin::Body-->
				<div class="card-body d-flex align-items-center justify-content-between pt-7 flex-wrap" style="height:300px;">
					<video id="remoteVideo1" autoplay muted style="width:100%;height:100%;background-color:black;"></video>
				</div>
				<!--end::Body-->
				<!-- begin::Footer -->
				<div class="card-footer d-flex align-items-center justify-content-between flex-wrap py-4" style="user-select: auto;">
					<!-- begin::input-group -->
					<div class="input-group">
						<span class="font-weight-bolder font-size-lg mr-2 pt-3">채널 1 : </span>
						<!-- begin::custom-select -->
						<select class="custom-select device-area mr-2" data-view-id="remoteVideo1" name="selectRemoteVideo1">
							<option value="" selected>장치를 선택하세요</option>
						</select>
						<!-- end::custom-select -->
						<!-- begin::toolbar -->
						<div class="btn-toolbar" role="toolbar">
							<div class="btn-group mr-2" role="group" aria-label="방송시작">
								<button type="button" class="btn btn-primary btn_start" data-view-id="remoteVideo1">방송시작</button>
							</div>
							<div class="btn-group" role="group" aria-label="영상중지">
								<button type="button" class="btn btn-danger btn_stop" data-view-id="remoteVideo1" disabled>영상중지</button>
							</div>
						</div>
						<!-- end::toolbar -->
					</div>
					<!-- end::input-group -->
				</div>
				<!-- end::Footer -->
			</div>
			<!--end::Stats Widget 1-->
		</div>
		<div class="col-lg-6">
			<!--begin::Stats Widget 2-->
			<div class="card card-custom card-stretch gutter-b">
				<!--begin::Body-->
				<div class="card-body d-flex align-items-center justify-content-between pt-7 flex-wrap" style="height:300px;">
					<video id="remoteVideo2" autoplay muted style="width:100%;height:100%;background-color:black;"></video>
				</div>
				<!--end::Body-->
				<!-- begin::Footer -->
				<div class="card-footer d-flex align-items-center justify-content-between flex-wrap py-4" style="user-select: auto;">
					<!-- begin::input-group -->
					<div class="input-group">
						<span class="font-weight-bolder font-size-lg mr-2 pt-3">채널 2 : </span>
						<!-- begin::custom-select -->
						<select class="custom-select device-area mr-2" data-view-id="remoteVideo2" name="selectRemoteVideo2">
							<option value="" selected>장치를 선택하세요</option>
						</select>
						<!-- end::custom-select -->
						<!-- begin::toolbar -->
						<div class="btn-toolbar" role="toolbar">
							<div class="btn-group mr-2" role="group" aria-label="방송시작">
								<button type="button" class="btn btn-primary btn_start" data-view-id="remoteVideo2">방송시작</button>
							</div>
							<div class="btn-group" role="group" aria-label="영상중지">
								<button type="button" class="btn btn-danger btn_stop" data-view-id="remoteVideo2" disabled>영상중지</button>
							</div>
						</div>
						<!-- end::toolbar -->
					</div>
					<!-- end::input-group -->
				</div>
				<!-- end::Footer -->
			</div>
			<!--end::Stats Widget 2-->
		</div>
	</div>
	<!--end::Row-->
	<!--begin::Row-->
	<div class="row">
		<div class="col-lg-6">
			<!--begin::Stats Widget 3-->
			<div class="card card-custom card-stretch gutter-b">
				<!--begin::Body-->
				<div class="card-body d-flex align-items-center justify-content-between pt-7 flex-wrap" style="height:300px;">
					<video id="remoteVideo3" autoplay muted style="width:100%;height:100%;background-color:black;"></video>
				</div>
				<!--end::Body-->
				<!-- begin::Footer -->
				<div class="card-footer d-flex align-items-center justify-content-between flex-wrap py-4" style="user-select: auto;">
					<!-- begin::input-group -->
					<div class="input-group">
						<span class="font-weight-bolder font-size-lg mr-2 pt-3">채널 3 : </span>
						<!-- begin::custom-select -->
						<select class="custom-select device-area mr-2" data-view-id="remoteVideo3" name="selectRemoteVideo3">
							<option value="" selected>장치를 선택하세요</option>
						</select>
						<!-- end::custom-select -->
						<!-- begin::toolbar -->
						<div class="btn-toolbar" role="toolbar">
							<div class="btn-group mr-2" role="group" aria-label="방송시작">
								<button type="button" class="btn btn-primary btn_start" data-view-id="remoteVideo3">방송시작</button>
							</div>
							<div class="btn-group" role="group" aria-label="영상중지">
								<button type="button" class="btn btn-danger btn_stop" data-view-id="remoteVideo3" disabled>영상중지</button>
							</div>
						</div>
						<!-- end::toolbar -->
					</div>
					<!-- end::input-group -->
				</div>
				<!-- end::Footer -->
			</div>
			<!--end::Stats Widget 3-->
		</div>
		<div class="col-lg-6">
			<!--begin::Stats Widget 4-->
			<div class="card card-custom card-stretch gutter-b">
				<!--begin::Body-->
				<div class="card-body d-flex align-items-center justify-content-between pt-7 flex-wrap" style="height:300px;">
					<video id="remoteVideo4" autoplay muted style="width:100%;height:100%;background-color:black;"></video>
				</div>
				<!--end::Body-->
				<!-- begin::Footer -->
				<div class="card-footer d-flex align-items-center justify-content-between flex-wrap py-4" style="user-select: auto;">
					<!-- begin::input-group -->
					<div class="input-group">
						<span class="font-weight-bolder font-size-lg mr-2 pt-3">채널 4 : </span>
						<!-- begin::custom-select -->
						<select class="custom-select device-area mr-2" data-view-id="remoteVideo4" name="selectRemoteVideo4">
							<option value="" selected>장치를 선택하세요</option>
						</select>
						<!-- end::custom-select -->
						<!-- begin::toolbar -->
						<div class="btn-toolbar" role="toolbar">
							<div class="btn-group mr-2" role="group" aria-label="방송시작">
								<button type="button" class="btn btn-primary btn_start" data-view-id="remoteVideo4">방송시작</button>
							</div>
							<div class="btn-group" role="group" aria-label="영상중지">
								<button type="button" class="btn btn-danger btn_stop" data-view-id="remoteVideo4" disabled>영상중지</button>
							</div>
						</div>
						<!-- end::toolbar -->
					</div>
					<!-- end::input-group -->
				</div>
				<!-- end::Footer -->
			</div>
			<!--end::Stats Widget 4-->
		</div>
	</div>
	<!--end::Row-->
</div>
<!--end::Container-->
