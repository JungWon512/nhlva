<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>
<!--begin::Header Menu-->
<div id="kt_header_menu" class="header-menu header-menu-mobile header-menu-layout-default">
    <!--begin:: MOBILE Header Nav-->
    <div class="container  mobile-only" style="display: none;">
        <div class="row">
            <!--begin::Close Icon-->
            <div class="col-xl-12 keen-padding-0 close-block">
                <!--begin::Notice-->
                <div class="card  gutter-b  mb-0">
                    <div class="card-body  px-0 keen-padding-0">
                        <div class="mb-1 mx-3">
                            <div class="row align-top mx-1">
                                <div class="col-11 ">
                                <p class="font-size-h2 py-0 my-0 font-weight-boldest" >
                                    서비스 이용을 위해
                                </p>
                                    <p class="font-size-h2 py-0 my-0 font-weight-boldest" >
                                        로그인해 주세요.
                                    </p>
                                </div>
                                <div class="col-1 " style="padding:1px;">
                                    <a href="javascript:;" class="btn-close" id="kt_header_mobile_toggle"> <img
                                            src="/static/images/btn-menu-close.png" width="25"/></a>
                                </div>
                            </div>
                            <div class="row align-top mx-5 mt-10 pb-8"  style="border-bottom: 1px solid #f1efef">

                                <div class="col-3 px-0 " onclick="javascript:pageMove('/home');">
                                    <div class="col-12 text-center">
                                        이미지
                                    </div>
                                    <div class="col-12 text-center font-size-h6 font-weight-boldest">
                                        지역선택
                                    </div>
                                </div>

                                <div class="col-3 px-0 " onclick="javascript:pageMove('/user/login');">
                                    <div class="col-12 text-center">
                                        이미지
                                    </div>
                                    <div class="col-12 text-center font-size-h6 font-weight-boldest">
                                        로그인
                                    </div>
                                </div>
                                <div class="col-3 px-0 " onclick="javascript:pageMove('/notice');">
                                    <div class="col-12 text-center">
                                        이미지
                                    </div>
                                    <div class="col-12 text-center font-size-h6 font-weight-boldest">
                                        공지사항
                                    </div>

                                </div>
                                <div class="col-3 px-0 "  onclick="javascript:pageMove('/guide');">
                                    <div class="col-12 text-center">
                                        이미지
                                    </div>
                                    <div class="col-12 text-center font-size-h6 font-weight-boldest">
                                        이용안내
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!--begin::Close Icon-->
            <!--begin::Close Icon-->
            <div class="col-xl-12 keen-padding-0 menu-block">
                <!--begin::Notice-->
                <div class="card  gutter-b">
                    <div class="card-body  py-0 ml-1 pl-1 keen-padding-0">
                        <div class="mb-7">
                            <div class="row align-items-center ml-1 mr-1">
                                <div class="col-10 ">
                                    <!--begin::Header Nav-->
                                    <ul class="menu-nav">
                                        <li class="mb-3">
                                            <a href="javascript:pageMove('/watch');"><p class="font-size-h4 font-weight-boldest color-000000"
                                                                style="display:inline;">경매 관전</p></a>
                                        </li>
                                        <li class="mb-3">
                                            <a href="javascript:goAuctionApp();"><p class="font-size-h4 font-weight-boldest  color-000000"
                                                                      style="display:inline;">경매 응찰</p></a>
                                        </li>
                                        <li class="mb-3">
                                            <a href="javascript:pageMove('/sales');"><p class="font-size-h4 font-weight-boldest  color-000000"
                                                                style="display:inline;">경매 예정 조회</p></a>
                                        </li>
                                        <li class="mb-3">
                                            <a href="javascript:pageMove('/results');"><p class="font-size-h4 font-weight-boldest  color-000000"
                                                                  style="display:inline;">경매 결과 조회</p></a>
                                        </li>
                                        <li class="mb-3">
                                            <a href="javascript:pageMove('/my/bid');"><p class="font-size-h4  font-weight-boldest color-000000"
                                                                 style="display:inline;">응찰 내역 조회</p></a>
                                        </li>
                                        <li class="mb-3">
                                            <a href="javascript:pageMove('/my/buy');"><p class="font-size-h4 font-weight-boldest  color-000000"
                                                                style="display:inline;">구매 내역 조회</p></a>
                                        </li>
                                        <li class="mb-3">
                                            <a href="javascript:pageMove('/my/entry');"><p class="font-size-h4 font-weight-boldest  color-000000"
                                                                   style="display:inline;">나의출장우 조회</p></a>
                                        </li>
                                        <li class="mb-3">
                                            <a href="javascript:pageMove('/my/entry');"><p class="font-size-h4 font-weight-boldest  color-000000"
                                                                                           style="display:inline;">마이페이지</p></a>
                                        </li>
                                    </ul>
                                    <!--end::Header Nav-->
                                </div>
                                <div class="col-2 " style="padding:1px;">

                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!--begin::Close Icon-->
            <!--begin::Close Icon-->
            <div class="col-xl-12 keen-padding-0 notice-block">
                <!--begin::Notice-->
                <div class="card  gutter-b">
                    <div class="card-body  keen-padding-0">
                        <div class="mb-7">
                            <div class="row align-items-center ml-1 mr-1">
                                <div class="col-2 ">

                                </div>
                                <div class="col-10 ">
                                    <div class="col-12 align-right">
                                        <a href="javascript:pageMove('/home');"><p class="font-size-h4  color-56afff text-right mr-8">Home</p>
                                        </a>
                                    </div>
                                    <div class="col-12 align-right">
                                        <a href="javascript:pageMove('/user/login');"><p class="font-size-h4  color-56afff text-right mr-8">Login</p>
                                        </a>
                                    </div>                                    
                                    <div class="col-12 align-right">
                                        <a href="javascript:pageMove('/notice');"><p class="font-size-h4  color-56afff text-right mr-8">공지사항</p>
                                        </a>
                                    </div>
                                    <div class="col-12 ">
                                        <a href="javascript:pageMove('/guide');"><p class="font-size-h4  color-56afff text-right  mr-8">
                                            이용가이드</p></a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!--begin::Close Icon-->
        </div>
    </div>
    <!--end::MOBILE Nav-->
    <!--begin:: PC Header Nav-->
    <div class="container pc-only mx-0 px-0">
        <div class="menu-nav row">
            <div class="col-lg-3">
                <div class="align-items-center">

                    <p class="font-weight-normal mb-1 color-7b7b7b" style="font-size: large">축산 스마트 경매</p>
                    <p class="display-4 font-weight-boldest mb-2">가축시장.kr</p>

                </div>
            </div>
            <div class="col-lg-9 pr-0">
                <div class="container-fluid  pr-0">
                    <div class="d-flex flex-row-reverse">
                        <div class="p-2"><a href="javascript:pageMove('/guide');">이용안내</a></div>
                        <div class="p-2"><a href="javascript:pageMove('/notice');">공지사항</a></div>
                        <div class="p-2"><a href="javascript:pageMove('/user/login');">로그인</a></div>
                        <div class="p-2"><a href="javascript:pageMove('/home');">지역선택</a></div>
                        <div class="p-2"><a href="javascript:pageMove('/office/main');">관리자</a></div>
                    </div>
                </div>
                <div class="container-fluid  pr-0">
                    <div class="d-flex flex-row-reverse">
                        <div class="p-2">
                            <a href="javascript:pageMove('/calendar');">
                            	<p class="display-5 font-weight-boldest mb-2 color-000000">일정안내</p>
                            </a>
                        </div>
                        <div class="p-2">
                            <a href="javascript:pageMove('/my/bid');">
                            	<p class="display-5 font-weight-boldest mb-2 color-000000">응찰내역조회</p>
                            </a>
                        </div>
                        <div class="p-2">
                            <a href="javascript:pageMove('/my/buy');">
                            	<p class="display-5 font-weight-boldest mb-2 color-000000">구매내역조회</p>
                            </a>
                        </div>
                        <div class="p-2">
                            <a href="javascript:pageMove('/my/entry');">
                            	<p class="display-5 font-weight-boldest mb-2 color-000000">나의 출장우 조회</p>
                            </a>
                        </div>
                        <div class="p-2">
                            <a href="javascript:pageMove('/results');">
                            	<p class="display-5 font-weight-boldest mb-2 color-000000">경매결과</p>
                            </a>
                        </div>
                        <div class="p-2">
                            <a href="javascript:pageMove('/sales');" >
                                <p class="display-5 font-weight-boldest mb-2 color-000000">경매예정</p>
                            </a>
                        </div>
                        <div class="p-2">
                            <a href="javascript:goAuctionApp();" >
                                <p class="display-5 font-weight-boldest mb-2 color-000000">경매응찰</p>
                            </a>
                        </div>
                        <div class="p-2">
                            <a href="javascript:pageMove('/watch');">
                                <p class="display-5 font-weight-boldest mb-2 color-000000">경매관전</p>
                            </a>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
    <!--end::Header Nav-->
</div>
<!--end::Header Menu-->

