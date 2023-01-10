<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>
<script type="text/javascript" src="/static/assets/plugins/custom/ckeditor4/ckeditor.js"></script>
<!-- TEST ----->
<!--begin::Container-->
<div class="container">
    <div class="row">
        <div class="col-xl-12">
            <!--begin::Notice-->
            <div class="card card-custom gutter-b">
                <div class="card-header">
                    <div class="card-title">
                        <h3 class="card-label">${johapData.CLNTNM}</h3>
                    </div>
                </div>
                <div class="card-body">
                    <form class="kt-form">
                        <div class="kt-portlet__body">

                            <div class="form-group d-none">
                                <label>조합코드</label>
                                <input type="text" class="form-control editJohpCd" value="${paramVO.naBzplc}" disabled>
                            </div>
                            <div class="form-group d-none">
                                <label>순번</label>
                                <input type="text" class="form-control editSeqNo" value="${paramVO.seqNo}" disabled
                                       placeholder="Input seqNo">
                            </div>
                            <div class="form-group">
                                <label>제목</label>
                                <input type="text" class="form-control editTitle" value="${data.TITLE}"
                                       placeholder="제목을 입력하세요">
                            </div>
                            <div class="form-group">
                                <label>내용</label>
                                <textarea id="noticeContent" placeholder="">${data.CONTENT}</textarea>
                            </div>
                        </div>
                        <div class="kt-portlet__foot">
                            <div class="kt-form__actions float-right">
                                <a href="javascript:;" class="btn btn-success mr-2 submit_notice">Submit</a>
                                <a href="javascript:;" class="btn btn-secondary notice-cancel">Cancel</a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<input type="hidden" id="johpCdVal" value="${paramVO.naBzplc}"/>
<input type="hidden" id="seqNoVal" value="${paramVO.seqNo}"/>
<input type="hidden" id=place value="${place}"/>
