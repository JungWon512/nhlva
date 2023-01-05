<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>
<script type="text/javascript" src="/static/js/common/commons.js"></script>
<div class="container">
    <div class="row">
      <div class="col-sm-9 col-md-7 col-lg-5 mx-auto">
        <div class="card my-5">
          <div class="card-body">
            <h5 class="card-title text-center">로그인</h5>
            <form class="form-signin">
              <div class="form-label-group">
                <input type="text" id="usrid" name="usrid" class="form-control" placeholder="이름" required autofocus>
                <input type="hidden" name="place" value="${place}" />
              </div>
              <div class="form-label-group">
                <input type="password" id="pw" name="pw" class="form-control" placeholder="비밀번호" required>
              </div>

              <div class="custom-control mb-3">
                <input type="checkbox" id="customCheck1">
                <label for="customCheck1">개인정보 동의</label>
              </div>
              <button class="btn btn-lg btn-primary btn-block text-uppercase action-submit" type="submit">로그인</button>
            </form>
          </div>
        </div>
      </div>
    </div>
</div>