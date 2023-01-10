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
	background-color: #000;
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

.login-main-text{
	margin-top: 20%;
	padding: 60px;
	color: #fff;
}
.login-main-text h2{
	font-weight: 300;
}

.btn-black{
	background-color: #000 !important;
	color: #fff;
}
</style>
<!-- <div class="container"> -->
<!--     <div class="row"> -->
<!--       <div class="col-sm-9 col-md-7 col-lg-5 mx-auto"> -->
<!--         <div class="card my-5"> -->
<!--           <div class="card-body"> -->
<!--             <h5 class="card-title text-center">로그인</h5> -->
<!--             <form class="form-signin"> -->
<!--               <div class="form-label-group"> -->
<!--                 <input type="text" id="usrid" name="usrid" class="form-control" placeholder="이름" required autofocus> -->
<%--                 <input type="hidden" name="place" value="${place}" /> --%>
<!--               </div> -->
<!--               <div class="form-label-group"> -->
<!--                 <input type="password" id="pw" name="pw" class="form-control" placeholder="비밀번호" required> -->
<!--               </div> -->

<!--               <div class="custom-control mb-3"> -->
<!--                 <input type="checkbox" id="customCheck1"> -->
<!--                 <label for="customCheck1">개인정보 동의</label> -->
<!--               </div> -->
<!--               <button class="btn btn-lg btn-primary btn-block text-uppercase action-submit" type="submit">로그인</button> -->
<!--             </form> -->
<!--           </div> -->
<!--         </div> -->
<!--       </div> -->
<!--     </div> -->
<!-- </div> -->

<!-- <div class="sidenav"> -->
<!-- 	<div class="login-main-text"> -->
<!-- 		<h1>농협 정보 시스템</h1> -->
<!-- 		<p>Login or register from here to access.</p> -->
<!-- 	</div> -->
<!-- </div> -->
<!-- <div class="main"> -->
<!-- 	<div class="col-md-6 col-sm-12"> -->
<!-- 		<div class="login-form"> -->
<!-- 			<form class="form-signin"> -->
<!-- 				<div class="form-group"> -->
<!-- 					<label>User Id</label> -->
<!-- 					<input type="text" id="usrid" name="usrid" class="form-control" placeholder="아이디" required autofocus> -->
<!-- 				</div> -->
<!-- 				<div class="form-group"> -->
<!-- 					<label>Password</label> -->
<!-- 					<input type="password" id="pw" name="pw" class="form-control" placeholder="비밀번호" required> -->
<!-- 				</div> -->
<!-- 				<button type="submit" class="btn btn-black">로그인</button> -->
<!-- 			</form> -->
<!-- 		</div> -->
<!-- 	</div> -->
<!-- </div> -->

<div class="container">

    <div class="row">
        <div class="col-md-4 offset-md-4">
        <div class="card text-center card  bg-default mb-3">
          <div class="card-header">
            LOGIN
          </div>
          <div class="card-body">
               <input type="text" id="userName" class="form-control input-sm chat-input" placeholder="Username" />
            </br>
            <input type="password" id="userPassword" class="form-control input-sm chat-input" placeholder="Password" />
          </div>
          <div class="card-footer text-muted">
            <a href="#" class="btn btn-secondary">LOGIN</a>
          </div>
        </div>
    </div>
    </div>
</div>