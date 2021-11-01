<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>
	<script type="text/javascript" src="/static/js/guide/jquery-1.12.4.js"></script>
	<script src="/static/js/common/utils.js"></script>
	<script type="text/javascript">
		var script = "${script}";
		var message = "${message}";
		
		function initMessage() {
			if(script != "") {
				${script}
			}
			else {
				var url = "${returnUrl}";
				pageMove(url);
			}
		}
		
		if (message	!= "") {
			alert(message);
			initMessage();
		}
		else {
			initMessage();
		}
	</script>
</html>