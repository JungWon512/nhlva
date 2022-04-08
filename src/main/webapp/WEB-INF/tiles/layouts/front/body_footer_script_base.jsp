<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate value="${now}" pattern="yyyyMMddHH" var="version" />

<script type="text/javascript" src="/static/js/guide/jquery-1.12.4.js"></script>
<script type="text/javascript" src="/static/js/guide/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="/static/js/guide/jquery.easing.1.3.min.js"></script>
<!-- <script type="text/javascript" src="/static/js/guide/jquery-ui.js"></script> -->
<script type="text/javascript" src="/static/js/guide/jquery.mmenu.min.all.js"></script>
<script type="text/javascript" src="/static/js/guide/slick.js"></script>
<script type="text/javascript" src="/static/js/guide/jquery.placeholder.ls.js"></script>
<script type="text/javascript" src="/static/js/guide/jquery.mCustomScrollbar.js"></script>
<script type="text/javascript" src="/static/js/guide/jquery.selectric.min.js"></script>
<script type="text/javascript" src="/static/js/guide/jquery.easypiechart.js"></script>

<script type="text/javascript" src="/static/js/guide/default.js?v=${version}"></script>
<script src="/static/js/common/commons.js?v=${version}"></script>
<script src="/static/js/common/utils.js?v=${version}"></script>
