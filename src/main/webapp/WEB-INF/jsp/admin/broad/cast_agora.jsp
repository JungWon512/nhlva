<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>

<div class="video_transmission">
	<input type="hidden" id="naBzplc" name="naBzplc" value="${johapData.NA_BZPLC}" />
	<input type="hidden" id="svcId" name="svcId" value="${johapData.KKO_SVC_ID}" />
	<input type="hidden" id="svcKey" name="svcKey" value="${johapData.KKO_SVC_KEY}" />
	
	<h3>영상 송출</h3>
	<ul class="video_list">
		<li class="item">
			<div class="inner">
				<div class="video_box agora">
					<div id="remoteVideo1" class="player">
						<div style="width: 100%; height: 100%; position: relative; overflow: hidden; background-color: black;">
							<video autoplay muted style="height:300px;"></video>
						</div>
					</div>
				</div>
				<dl class="bot">
					<dt>채널 1</dt>
					<dd class="select_area">
						<select name="selectRemoteVideo1" id="select_id1" class="custom-select device-area" data-view-id="remoteVideo1">
							<option value="">장치를 선택하세요</option>
						</select>
					</dd>
					<dd class="btn_box">
						<button type="button" class="btn_start" data-view-id="remoteVideo1">방송 시작</button>
						<button type="button" class="btn_stop" data-view-id="remoteVideo1" disabled>영상 중지</button>
					</dd>
				</dl>
			</div>
		</li>
		<li class="item">
			<div class="inner">
				<div class="video_box agora">
					<div id="remoteVideo2" class="player">
						<div style="width: 100%; height: 100%; position: relative; overflow: hidden; background-color: black;">
							<video autoplay muted style="height:300px;"></video>
						</div>
					</div>
				</div>
				<dl class="bot">
					<dt>채널 2</dt>
					<dd class="select_area">
						<select name="selectRemoteVideo2" id="select_id2" class="custom-select device-area" data-view-id="remoteVideo2">
							<option value="">장치를 선택하세요</option>
						</select>
					</dd>
					<dd class="btn_box">
						<button type="button" class="btn_start" data-view-id="remoteVideo2">방송 시작</button>
						<button type="button" class="btn_stop" data-view-id="remoteVideo2" disabled>영상 중지</button>
					</dd>
				</dl>
			</div>
		</li>
		<li class="item">
			<div class="inner">
				<div class="video_box agora">
					<div id="remoteVideo3" class="player">
						<div style="width: 100%; height: 100%; position: relative; overflow: hidden; background-color: black;">
							<video autoplay muted style="height:300px;"></video>
						</div>
					</div>
				</div>
				<dl class="bot">
					<dt>채널 3</dt>
					<dd class="select_area">
						<select name="selectRemoteVideo3" id="select_id3" class="custom-select device-area" data-view-id="remoteVideo3">
							<option value="">장치를 선택하세요</option>
						</select>
					</dd>
					<dd class="btn_box">
						<button type="button" class="btn_start" data-view-id="remoteVideo3">방송 시작</button>
						<button type="button" class="btn_stop" data-view-id="remoteVideo3" disabled>영상 중지</button>
					</dd>
				</dl>
			</div>
		</li>
		<li class="item">
			<div class="inner">
				<div class="video_box agora">
					<div id="remoteVideo4" class="player">
						<div style="width: 100%; height: 100%; position: relative; overflow: hidden; background-color: black;">
							<video autoplay muted style="height:300px;"></video>
						</div>
					</div>
				</div>
				<dl class="bot">
					<dt>채널 4</dt>
					<dd class="select_area">
						<select name="selectRemoteVideo4" id="select_id4" class="custom-select device-area" data-view-id="remoteVideo4">
							<option value="">장치를 선택하세요</option>
						</select>
					</dd>
					<dd class="btn_box">
						<button type="button" class="btn_start" data-view-id="remoteVideo4">방송 시작</button>
						<button type="button" class="btn_stop" data-view-id="remoteVideo4" disabled>영상 중지</button>
					</dd>
				</dl>
			</div>
		</li>
	</ul>
</div>