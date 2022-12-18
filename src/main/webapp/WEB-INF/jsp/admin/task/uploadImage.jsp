<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>

<script src="https://sdk.amazonaws.com/js/aws-sdk-2.1261.0.min.js"></script>
<script>
	const endpoint = new AWS.Endpoint('https://kr.object.ncloudstorage.com');
	const region = 'kr-standard';
	const access_key = 'loqHvgq2A4WGx0D7feer';
	const secret_key = 'yrmIJmsF37g1BExQXk5CIhrMn1EG1h32qJyaTvzF';
</script>

<div class="admin_auc_main">
	<div class="btn_area">
		<a href="javascript:;" class="btn_save">저장</a>
	</div>
	<h2>File input</h2>
	<p>
		<label for="file-input">Select an image file:</label> <input
			type="file" id="file-input" />
	</p>
	<p>
		<label for="url">Or enter an image URL into the following
			field:</label> <input type="url" id="url" placeholder="Image URL" />
	</p>
	<p>
		Or <strong>drag &amp; drop</strong> an image file onto this webpage.
	</p>
	<h3>Options</h3>
	<h2>Result</h2>
	<figure id="result">
		<figcaption style="display: none;">
			Loading images from File objects requires support for the <a
				href="https://developer.mozilla.org/en-US/docs/Web/API/URL">URL</a>
			or <a
				href="https://developer.mozilla.org/en-US/docs/Web/API/FileReader">FileReader</a>
			API.
		</figcaption>
	</figure>
</div>
