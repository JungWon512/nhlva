<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>
<script type="text/javascript" src="/static/js/common/commons.js"></script>
<style type="text/css">
.main-head{
	height: 150px;
	background: #FFF;
}
.sidenav {
	height: 100%;
	background-color: #3F4254;
	overflow-x: hidden;
	padding-top: 20px;
}
.main {
	padding: 0px 10px;
}
@media screen and (max-height: 450px) {
	.sidenav {padding-top: 15px;}
}
@media screen and (max-width: 450px) {
	.login-form{
		margin-top: 10%;
	}
	.register-form{
		margin-top: 10%;
	}
}
@media screen and (min-width: 768px){
	.main{
		margin-left: 40%; 
	}
	.sidenav{
		width: 40%;
		position: fixed;
		z-index: 1;
		top: 0;
		left: 0;
	}
	.login-form{
		margin-top: 80%;
	}
	.register-form{
		margin-top: 20%;
	}
}
@media screen and (max-width: 768px){
	.sidenav{
		height: 40%;
		padding-top: 0;
	}
}
.login-main-text{
	margin-top: 20%;
	padding: 60px;
	color: #fff;
}
.login-main-text h2{
	font-weight: 500;
	font-size: 2.5rem;
}

.btn-black{
	background-color: #000 !important;
	color: #fff;
}
</style>

<div class="sidenav">
	<div class="login-main-text">
		<h2>NH 가축시장</h2>
<!-- 		<p>Login or register from here to access.</p> -->
	</div>
</div>
<div class="main">
	<div class="col-md-6 col-sm-12">
		<div class="login-form">
			<form class="form-signin">
				<div class="form-group">
					<label>아이디</label>
					<input type="text" id="usrid" name="usrid" class="form-control" placeholder="이름" required autofocus />
				</div>
				<div class="form-group">
					<label>비밀번호</label>
					<input type="password" id="pw" name="pw" class="form-control" placeholder="비밀번호" required />
				</div>
				<button type="button" class="btn btn-black action-submit">로그인</button>
			</form>
		</div>
	</div>
</div>
