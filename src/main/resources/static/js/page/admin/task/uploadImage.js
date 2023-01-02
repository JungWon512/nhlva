;(function (win, $, COMMONS) {
// Class definition
	var Entry = function () {
		
		var resultNode = $('#preview_list');
		var metaNode = $('#meta');
		var thumbNode = $('#thumbnail_list');
		var actionsNode = $('#actions');
		var orientationNode = $('#orientation');
		var imageSmoothingNode = $('#image-smoothing');
		var fileInputNode = $('#file-input');
		var urlNode = $('#url');
		var editNode = $('#edit');
		var cropNode = $('#crop');
		var cancelNode = $('#cancel');
		var coordinates;
		var jcropAPI;
	
		/**
		 * Displays tag data
		 *
		 * @param {*} node jQuery node
		 * @param {object} tags Tags map
		 * @param {string} title Tags title
		 */
		function displayTagData(node, tags, title) {
			if (active != "prod") {
				var table = $('<table></table>').css("width", "100%");
				var row = $('<tr></tr>');
				var cell = $('<td></td>');
				var headerCell = $('<th colspan="2"></th>');
				var prop;
				table.append(row.clone().append(headerCell.clone().text(title)))
				for (prop in tags) {
					if (Object.prototype.hasOwnProperty.call(tags, prop)) {
						if (typeof tags[prop] === 'object') {
							displayTagData(node, tags[prop], prop);
							continue;
						}
						table.append(row.clone().append(cell.clone().text(prop)).append(cell.clone().text(tags[prop])));
					}
				}
				node.append(table).show();
			}
		}
	
		/**
		 * Displays the thumbnal image
		 *
		 * @param {*} node jQuery node
		 * @param {string} thumbnail Thumbnail URL
		 * @param {object} [options] Options object
		 */
		function displayThumbnailImage(node, thumbnail, options) {
			if (thumbnail) {
				var link = $("<li></li>").appendTo(node);
				loadImage(thumbnail, function (img) {
						link.append(img);
						node.show();
					},
					options);
			}
		}
		
		function makeThumbnailImage() {
			console.log(arguments);
			var previewList = resultNode.find("li > canvas");
			thumbNode.empty();
			for (var img of previewList) {
				var thumbnail = loadImage.scale(img, {maxWidth:120});
				var content = $("<li></li>").addClass($(img).parent().attr("class").split(" ")[0]).append(thumbnail);
				thumbNode.append(content);
			};
		}
	
		/**
		 * Updates the results view
		 * @param {*} img Image or canvas element
		 * @param {object} [data] Metadata object
		 * @param {boolean} [keepMetaData] Keep meta data if true
		 */
		function updateResults(img, data, keepMetaData, node) {
			console.log(img.toDataURL());
			console.log(data.imageHead);
			var orientation = -1;
			var isCanvas = window.HTMLCanvasElement && img instanceof HTMLCanvasElement
			if (!keepMetaData) {
				if (isCanvas) {
					actionsNode.show();
				}
				else {
					actionsNode.hide();
				}
			}
	
			if (!(isCanvas || img.src)) {
				resultNode.children().replaceWith($('<span>Loading image file failed</span>'));
				return;
			}
			
			if(!node) {
				var seqno = resultNode.find("li:last").length == 0 ? 0 : parseInt(resultNode.find("li:last").data("seqno"));
				var content = $('<li></li>').attr("data-seqno", seqno + 1).addClass("image" + seqno).addClass(resultNode.find("li").length == 0 ? "active" : "").append(img);
				resultNode.append(content);
			}
			else {
				node.children().replaceWith(img);
			}
			if (data.imageHead) {
				img.toBlob(function (blob) {
					if (!blob) return;
					loadImage.replaceHead(blob, data.imageHead);
				}, 'image/jpeg')
			}
			
			makeThumbnailImage();
		}
	
		/**
		 * Displays the image
		 * @param {File|Blob|string} file File or Blob object or image URL
		 */
		function displayImage(file , options) {
			if (!options) {
				options = {
					maxWidth: resultNode.width(),
					canvas: true,
					pixelRatio: window.devicePixelRatio,
					downsamplingRatio: 0.5,
					orientation: Number(orientationNode.val()) || true,
					imageSmoothingEnabled: imageSmoothingNode.is(':checked'),
					meta: true
				}
			}
			
			if (!loadImage(file, updateResults, options)) {
				removeMetaData();
				resultNode.children().replaceWith($('<span>' + 'Your browser does not support the URL or FileReader API.' + '</span>'))
			}
		}
	
		/**
		 * 파일 변경 이벤트
		 * @param {event} event Drop or file selection change event
		 */
		function fileChangeHandler(e) {
			e.preventDefault();
			var originalEvent = e.originalEvent;
			var target = originalEvent.dataTransfer || originalEvent.target;
			var files = target && target.files;
			if (!files) {
				return;
			}
			var filesArr = Array.prototype.slice.call(files);
			
			filesArr.forEach(function(f, index) {
				var arrExt = ["jpg", "png", "gif", "jpeg"];
				var fileName = fileInputNode.val();
				var ext = fileName.slice(fileName.indexOf(".") + 1).toLowerCase();
	 
				if (!arrExt.includes(ext)) {
					alert('이미지 파일만 업로드 가능합니다.');
					fileInputNode.val("");
					return;
				}
				var reader = new FileReader();
				reader.onload = function(e) {
					var file = f;
					var options = {
						maxWidth: resultNode.width(),
						canvas: true,
						pixelRatio: window.devicePixelRatio,
						downsamplingRatio: 0.5,
						orientation: true,
						meta: true
					}
					if (!file) {
						return;
					}
					displayImage(file, options)
				}
				reader.readAsDataURL(f);
			});
			
			makeThumbnailImage();
		}
		
		/**
		 * 파일 URL 변경 이벤트
		 */
		function urlChangeHandler() {
			var url = $(this).val();
			if (url) displayImage(url);
		}
		
		var addBtnEvent = function(){
			// 저장 이벤트
			$(".btn_save").on('click', function(){
				if ($(this).hasClass("disabled")) return;
				fnSave();
			});
			
			$(".btn_back").on('click', function(){
				history.back();
			});
			
			// 썸네일 클릭시 원본 이미지 보이기
			$(document).on("click", "#thumbnail_list > li", function() {
				var name = $(this).attr("class");
				$("#preview_list > li").removeClass("active");
				$("#preview_list > li." + name).addClass("active");
			});
			
			// 파일 변경 이벤트
			$('#file-input').on('change', fileChangeHandler);
			
			// 이미지 회전
			$(".btn_rotate").on("click", function() {
				var img = resultNode.find('li.active > canvas')[0];
				if (img) {
					updateResults(
						loadImage.scale(img, {
							maxWidth: resultNode.width() * (window.devicePixelRatio || 1),
							pixelRatio: window.devicePixelRatio,
							orientation: Number(6) || true,
							imageSmoothingEnabled: true,
							crossOrigin : 'Anonymous'
						}),
						metaNode.data(),
						true,
						resultNode.find("li.active")
					)
				}
			});
			
			$("input[name='file_url']").on('change paste input', function(){
				var url = $(this).val();
				if (url) displayImage(url);
			});
		};
		
		var fnSave = function() {
			$(".btn_save").addClass("disabled");
			
			var frm = $("form[name='frm']").serializeObject();
			
			var originFiles = [];

			var originList = $("#preview_list").find("canvas, img");
			for (origin of originList) {
				var obj = new Object();
				var name = $(origin).parent().attr("class").split(" ")[0];
				obj["name"] = name;
				if (origin.tagName == "CANVAS") {
					var thumbnail = $("#thumbnail_list").find("li." + name).find("canvas")[0];
					obj["type"] = "canvas";
					obj["origin"] = origin.toDataURL();
					obj["thumbnail"] = (thumbnail != undefined) ? thumbnail.toDataURL() : "";
					originFiles.push(obj);
				}
				else {
					obj["type"] = "image";
					obj["name"] = name;
					obj["filePath"] = $(origin).siblings("input[name='file_path']").val();
					obj["fileNm"] = $(origin).siblings("input[name='file_nm']").val();
					obj["fileExtNm"] = $(origin).siblings("input[name='file_ext_nm']").val();
					originFiles.push(obj);
				}
			}

			if (originFiles.length > 0) {
				frm["files"] = originFiles;
			}
			
			$.ajax({
				url : '/office/task/uploadImageAjax',
				data : JSON.stringify(frm),
				type : 'POST',
				dataType : 'json',
				beforeSend: function (xhr) {
					xhr.setRequestHeader("Accept", "application/json");
					xhr.setRequestHeader("Content-Type", "application/json");
				}
			}).done(function (body) {
				modalAlert("", body.message);
				$(".btn_save").removeClass("disabled");
				return;
			});
		
		};
		
		var addImage = function() {
			$("input[name='file_url']").each(function(){
				var url = $(this).val();
				if (url) displayImage(url);
			});
		};

		return {
			// public functions
			init: function () {
				addBtnEvent();
				addImage();
			}
		};
	}();

	jQuery(document).ready(function () {
		Entry.init();
	});
})(window, window.jQuery);
