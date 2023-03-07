;(function (win, $, COMMONS) {
// Class definition
	var Entry = function () {
		
		const resultNode = $('#preview_list');
		const metaNode = $('#meta');
		const thumbNode = $('#thumbnail_list');
		const orientationNode = $('#orientation');
		const imageSmoothingNode = $('#image-smoothing');
		const fileInputNode = $('#file-input');
		const imgMaxCnt = 8;							// 업로드 가능 이미지 수
		const arrExt = ["jpg", "png", "gif", "jpeg"];	// 업로드 가능 확장자 리스트
		const limitSize = 10 * 1024 * 1024; 			// TODO :: 업로드 가능한 파일 최대 크기를 적용할지?
		
		// 썸네일 이미지 생성
		function makeThumbnailImage() {
			var previewList = resultNode.find("div.item > canvas");
			thumbNode.find(".add-item").not(".add-pic").remove();
			for (var img of previewList) {
				var thumbnail = loadImage.scale(img, {maxWidth:100});
				var content = $("<div class='add-item'></div>").addClass($(img).parent().attr("class").split(" ")[1]).append(thumbnail).append("<button type='button' class='btn-del'><span class='sr-only'>삭제</span></button>");
				thumbNode.append(content);
			};
			var imgCnt = resultNode.find("div.item > canvas").length;
			thumbNode.find(".num").text(`${imgCnt} / ${imgMaxCnt}`);
		}
	
		/**
		 * 원본 이미지 
		 * @param {*} img Image or canvas element
		 * @param {object} [data] Metadata object
		 * @param {boolean} [keepMetaData] Keep meta data if true
		 */
		function updateResults(img, data, keepMetaData, node) {
			if(!node) {
				var seqno = resultNode.find("div.item:last").length == 0 ? 0 : parseInt(resultNode.find("div.item:last").data("seqno"));
				var content = $("<div class='item'></div>").attr("data-seqno", seqno + 1).addClass("image" + seqno).addClass(resultNode.find("div").length == 0 ? "active" : "").append(img);
				resultNode.append(content);
			}
			else {
				node.children().replaceWith(img);
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
					maxWidth: resultNode.width() * 2,
					canvas: true,
					pixelRatio: 1,
					downsamplingRatio: 0.5,
					orientation: Number(orientationNode.val()) || true,
					imageSmoothingEnabled: imageSmoothingNode.is(':checked'),
					meta: true
				}
			}
			
			loadImage(file, updateResults, options);
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
			var currImgLength = $("#preview_list > div").length;
			
			if (!files) {
				return;
			}
			var filesArr = Array.prototype.slice.call(files);
			
//			if (currImgLength + filesArr.length > imgMaxCnt) {
//				modalAlert("", `이미지는 최대 ${imgMaxCnt} 장 까지<br/> 업로드 가능합니다.`);
//				fileInputNode.val("");
//				return;
//			}
			
			filesArr.forEach(function(f, index) {
				if(++currImgLength >= 9) {
					modalAlert("", `이미지는 최대 ${imgMaxCnt} 장 까지<br/> 업로드 가능합니다.`);
					return;
				};
				
				var fileName = fileInputNode.val();
				var ext = fileName.slice(fileName.indexOf(".") + 1).toLowerCase();
	 
				if (!arrExt.includes(ext)) {
					modalAlert("", "이미지 파일만 업로드 가능합니다.");
					fileInputNode.val("");
					return;
				}
				var reader = new FileReader();
				reader.onload = function(e) {
					var file = f;
					var options = {
						maxWidth: resultNode.width() * 2,
						canvas: true,
						pixelRatio: 1,
						downsamplingRatio: 0.5,
						orientation: true,
						meta: true
					}
					if (!file) {
						return;
					}
					displayImage(file, options);
				}
				reader.readAsDataURL(f);
			});
			
			makeThumbnailImage();
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
			$(document).on("click", "#thumbnail_list > div:not(.add-pic)", function() {
				var name = $(this).attr("class").split(" ")[1];
				$("#preview_list > div").removeClass("active");
				$("#preview_list > div." + name).addClass("active");
			});
			
			// 파일 변경 이벤트
			$('#file-input').on('change', fileChangeHandler);
			
			// 이미지 회전
			$(".btn_rotate").on("click", function() {
				var img = resultNode.find('div.active > canvas')[0];
				if (img) {
					updateResults(
						loadImage.scale(img, {
							maxWidth: resultNode.width() * (1 || 1),
							pixelRatio: 1,
							orientation: Number(6) || true,
							imageSmoothingEnabled: true,
							crossOrigin : 'Anonymous'
						}),
						metaNode.data(),
						true,
						resultNode.find("div.active")
					)
				}
			});
			
			// 썸네일 삭제 이벤트(원본 이미지와 함께 삭제)
			$(document).on("click", "#thumbnail_list .btn-del", function() {
				var name = $(this).parent().attr("class").split(" ")[1];
				modalComfirm(""
				, "삭제하시겠습니까?<br/>저장 후 최종삭제됩니다."
				, function(){
					delImage(name);
				});
				return;
			});
			
			// 원본 삭제 이벤트(썸네일 이미지와 함께 삭제)
			$(".btn_delete").on("click", function() {
				var name = $(resultNode).find(".active").attr("class").split(" ")[1];
				modalComfirm(""
				, "삭제하시겠습니까?<br/>저장 후 최종삭제됩니다."
				, function(){
					delImage(name);
				});
				return;
			});
			
			$(".btn_list_search").on("click", function(){
				fnListSearch();
			});
		};
		
		var fnListSearch = function() {
			var param = {
				Bucket: bucket_name
			}
			S3.listObjects({ Delimiter: "/", Bucket : bucket_name }, function(err, data) {
				if (err) {
					return alert("There was an error listing your albums: " + err.message);
				} else {
					console.log(data);
				}
			});
		}
		
		// 이미지 저장 : 사용x
		var fnSaveback = function() {
			$(".btn_save").addClass("disabled");
			
			var frm = $("form[name='frm']").serializeObject();
			
			var originFiles = [];

			var originList = $("#preview_list").find("canvas");
			for (origin of originList) {
				var obj = new Object();
				var name = $(origin).parent().attr("class").split(" ")[1];
				var thumbnail = thumbNode.find("div." + name).find("canvas")[0];
				
				// canvas 이미지를 base64인코딩 후 서버 전송
				obj["name"] = name;
				obj["type"] = "canvas";
				obj["origin"] = origin.toDataURL();
				obj["thumbnail"] = (thumbnail != undefined) ? thumbnail.toDataURL() : "";
				originFiles.push(obj);
			}

			frm["files"] = originFiles;
			
			$.ajax({
				url : '/office/task/uploadImageAjax',
				data : JSON.stringify(frm),
				type : 'POST',
				dataType : 'json',
				beforeSend: function (xhr) {
					xhr.setRequestHeader("Accept", "application/json");
					xhr.setRequestHeader("Content-Type", "application/json");
				},
				error:function(request,status,error){
					modalAlert("", "이미지 저장에 실패했습니다.");
				}
			}).done(function (body) {
				modalAlert("", body.message);
				$(".btn_save").removeClass("disabled");
				return;
			});
		};
		
		// 이미지 저장
		var fnSave = function() {
			fnShowLoadingImg();
			$(".btn_save").addClass("disabled");
			var uploadList = [];
			var failList = [];
			var upload = (async () => {
				// 폴더 생성
				var objectPath = [$("input[name='naBzplc']").val(), $("input[name='sraIndvAmnno']").val()];
				var folderName = objectPath.join("/");
				var fileList = $("#preview_list").find("canvas");
				console.log(fileList);
				for (file of fileList) {
					var name = $(file).parent().attr("class").split(" ")[1];
					
					var fileDataUrl = file.toDataURL("image/png");
					var fileName = uuidv4();
					var objectName = folderName + "/" + fileName + ".png";
					var obj = { filePath : folderName + "/", fileNm : fileName, fileExtNm : ".png" };
					// upload file
					await S3.putObject({Bucket: bucket_name, Key: objectName,ACL: 'public-read',// ACL을 지우면 전체 공개되지 않습니다.
										Body: dataURLtoBlob(fileDataUrl)
					})
					.promise()
					.then(() => {
						var thumbnail = thumbNode.find("div." + name).find("canvas")[0].toDataURL("image/png");
						var thumbObjectName = folderName + "/thumb/" + fileName + ".png";
							S3.putObject({Bucket: bucket_name, Key: thumbObjectName, ACL: 'public-read',// ACL을 지우면 전체 공개되지 않습니다.
										Body: dataURLtoBlob(thumbnail)
						}).promise();
					})
					.then(() => {uploadList.push(obj);})
					.catch(() => {(e) => console.log(e)});
				}
			})();
			
			upload.then(()=>{fnSaveUploadInfo(uploadList)}).catch(() => fnHideLoadingImg());
		};
		
		var fnSaveUploadInfo = async function(uploadList) {
			var frm = $("form[name='frm']").serializeObject();
			frm["uploadList"] = uploadList;
			
			$.ajax({
				url : '/office/task/uploadImageInfoAjax',
				data : JSON.stringify(frm),
				type : 'POST',
				dataType : 'json',
				beforeSend: function (xhr) {
					xhr.setRequestHeader("Accept", "application/json");
					xhr.setRequestHeader("Content-Type", "application/json");
				},
				error:function(request,status,error){
					fnHideLoadingImg();
					modalAlert("", "이미지 저장에 실패했습니다.");
				}
			}).done(function (body) {
				fnHideLoadingImg();
				modalAlert("", body.message);
				$(".btn_save").removeClass("disabled");
				return;
			});
		}
		
		// 이미지 url로 canvas 추가
		var addImage = function() {
			$("input[name='file_url']").each(function(){
//				displayImage($(this).val());
			});
			
			$("#preview_list > .item > img").each(function(){
				var target = $(this).parent();
				var image = new Image();
				image.src = $(this).attr("src");
				image.crossOrigin = 'Anonymous';
				
				image.onload = function(){
					$(target).append(loadImage.scale(image, {
															maxWidth: resultNode.width() * 2,
															canvas: true,
															pixelRatio: 1,
															downsamplingRatio: 0.5,
															orientation: true,
															meta: true
														})).find("img").remove();
				}
			});
			
			$("#thumbnail_list > .add-item > img").each(function(){
				var target = $(this).parent();
				var image = new Image();
				image.src = $(this).attr("src");
				image.crossOrigin = 'Anonymous';
				
				image.onload = function(){
					$(target).append(loadImage.scale(image, {maxWidth:100, canvas: true})).find("img").remove();
				}
			});
		};

		// 이미지 삭제
		var delImage = function(name) {
			thumbNode.find("div." + name).remove();
			resultNode.find("div." + name).remove();
			resultNode.find("div").removeClass("active");
			resultNode.find("div:first").addClass("active");
			
			var imgCnt = resultNode.find("div.item > canvas").length;
			thumbNode.find(".num").text(`${imgCnt} / ${imgMaxCnt}`);
		}

		return {
			init: function () {
				addBtnEvent();
				addImage();
//				new Promise(addImage).then(makeThumbnailImage);
			}
		};
	}();

	jQuery(document).ready(function () {
		Entry.init();
	});
})(window, window.jQuery);
