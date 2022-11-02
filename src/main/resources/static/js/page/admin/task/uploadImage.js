; (function(win, $, COMMONS) {
	// Class definition
	var Main = function() {

		var clickEvent = (function() {
			if ('ontouchstart' in document.documentElement === true && navigator.userAgent.match(/Android/i)) {
				return 'touchstart';
			}
			else {
				return 'click';
			}
		})();

		var addEvent = function() {
//			$('#file1').on('change', dropChangeHandler);
			$('#file1').on('change', function(e){
				e.preventDefault();
				e = e.originalEvent;
				loadImage(e.target.files[0], function(img, data){
					console.log(data);
					for (var keys in data) {
						var html = [];
						console.log(typeof data[keys]);
						if (typeof data[keys] == "object") continue;
						html.push("<tr>");
						html.push("<th>" + keys + "</th><td>" + data[keys] + "</td>");
						html.push("</tr>");
						$(".data").find("table").append(html.join(""));
					}
					var exif = data.exif.getAll();
					for (var keys in exif) {
						var html = [];
						console.log(typeof exif[keys]);
						if (typeof exif[keys] == "object") {
							for (var keys2 in exif[keys]) {
								html.push("<tr>");
								html.push("<th>" + keys2 + "</th><td>" + exif[keys][keys2] + "</td>");
								html.push("</tr>");
							}
						}
						else {
							html.push("<tr>");
							html.push("<th>" + keys + "</th><td>" + exif[keys] + "</td>");
							html.push("</tr>");
						}
						$(".exif").find("table").append(html.join(""));
					}
					$("#image_area").append(img);
				},
				{maxWidth: 300, meta: true, orientation : true})
			});
		};
		
		function dropChangeHandler(e) {
			e.preventDefault()
			e = e.originalEvent
			var files = e.target.files;
			var filesArr = Array.prototype.slice.call(files);

			filesArr.forEach(function(f, index) {
				fileName = $('#file1').val();
				fileName = fileName.slice(fileName.indexOf(".") + 1).toLowerCase();

				if (fileName != "jpg" && fileName != "png" && fileName != "gif" && fileName != "bmp" && fileName != "apng" && fileName != "avif" && fileName != "bpg" && fileName != "dds" && fileName != "dng" && fileName != "exr" && fileName != "jpeg" && fileName != "heif" && fileName != "psd" && fileName != "raw" && fileName != "rgbe" && fileName != "tiff" && fileName != "webp") {
					alert('이미지 파일만 업로드 가능합니다.');
					$('#file1').val("");
					return;
				}

				var reader = new FileReader();
				reader.onload = function(e) {
					var file = f;
					var options = {
						maxWidth: 500,
						canvas: true,
						pixelRatio: window.devicePixelRatio,
						downsamplingRatio: 0.5,
						orientation: true
					}
					console.log(file);
					if (!file) {
						return
					}

					displayImage(file, options)

				}
				reader.readAsDataURL(f);

			});
		}

		function displayImage(file, options) {
			currentFile = file
			if (!loadImage(file, updateResults, options)) {
				result.children().replaceWith()
			}
		}

		function updateResults(img, data) {
			var fileName = currentFile.name
			var href = img.src
			var dataURLStart
			var content
			if (!(img.src || img instanceof HTMLCanvasElement)) {

			} else {

				displayMetaData(data);
			}
		}

		function displayMetaData(data) {

			if (!data) return
			var exif = data.exif;
			var iptc = data.iptc;
			
			$(".data").text(JSON.stringify(data));
			$(".exif").text(JSON.stringify(exif));
			$(".iptc").text(JSON.stringify(iptc));

			if (exif) {
				if (exif.get('Orientation') == null) {
					orientation = 1;
				} else {
					orientation = exif.get('Orientation');
				}
			} else {
				orientation = 1;
			}

			console.log(orientation);
		}

		return {
			// public functions
			init: function() {
				addEvent();
			}
		};
	}();

	jQuery(document).ready(function() {
		Main.init();
	});
})(window, window.jQuery);
