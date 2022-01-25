<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>
<!-- admin_main [s] -->
<div class="admin_main">
	<h3>MAIN</h3>
	<h4>${johapData.CLNTNM} 관리자 메뉴</h4>
	<ul class="menu_list">
		<li class="menu_item mobile">
			<a href="javascript:pageMove('/office/task/main');">
				<span class="ico">
					<img src="/static/images/guide/ico_admin_menu_05.svg" alt="">
				</span>
				모바일업무
			</a>
		</li>
		<li class="menu_item mobile">
			<a href="javascript:pageMove('/office/broad/cast');">
				<span class="ico">
					<img src="/static/images/guide/ico_admin_menu_01.svg" alt="">
				</span>
				영상송출
			</a>
		</li>
		<li class="menu_item">
			<a href="javascript:pageMove('/office/auction/monster');">
				<span class="ico">
					<img src="/static/images/guide/ico_admin_menu_02.svg" alt="">
				</span>
				모니터링
			</a>
		</li>
		<li class="menu_item">
			<a href="javascript:pageMove('/office/auction/board');">
				<span class="ico">
					<img src="/static/images/guide/ico_admin_menu_03.svg" alt="">
				</span>
				멀티비젼
			</a>
		</li>
		<li class="menu_item">
			<a href="javascript:pageMove('/office/auction/stream');">
				<span class="ico">
					<img src="/static/images/guide/ico_admin_menu_04.svg" alt="">
				</span>
				유투브방송
			</a>
		</li>
		<li class="menu_item">
			<a href="javascript:pageMove('/office/auction/bidInfo');">
				<span class="ico">
					<img src="/static/images/guide/ico_admin_menu_04.svg" alt="">
				</span>
				낙찰정보
			</a>
		</li>
		<li class="menu_item">
			<a href="javascript:pageMove('/office/auction/videoStream');">
				<span class="ico">
					<img src="/static/images/guide/ico_admin_menu_04.svg" alt="">
				</span>
				영상
			</a>
		</li>
	</ul>
</div>
<!-- admin_main [s] -->