<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>

<style type="text/css">
.admin_auc_main .main_search ul {
	margin: 0px 0;
	display: flex;
	flex-direction: row;
	flex-wrap: wrap;
	align-content: center;
	justify-content: flex-start;
	overflow: scroll;
}

.admin_auc_main .main_search ul li {
	padding: 5px;
}
</style>

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
	<p>
		<input type="checkbox" id="image-smoothing" checked /> <label
			for="image-smoothing">Image smoothing</label>
	</p>
	<h2>Result</h2>
	<p id="actions" style="display: none;">
		<button type="button" id="edit">Edit</button>
		<button type="submit" id="crop">Crop</button>
		<button type="reset" id="cancel">Cancel</button>
	</p>
	<figure id="result">
		<figcaption style="display: none;">
			Loading images from File objects requires support for the <a
				href="https://developer.mozilla.org/en-US/docs/Web/API/URL">URL</a>
			or <a
				href="https://developer.mozilla.org/en-US/docs/Web/API/FileReader">FileReader</a>
			API.
		</figcaption>
	</figure>
	<div id="meta" style="display: none;">
		<h3>Image metadata</h3>
		<figure id="thumbnail" style="display: none;"></figure>
	</div>
</div>
