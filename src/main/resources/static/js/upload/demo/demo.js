/*
 * JavaScript Load Image Demo JS
 * https://github.com/blueimp/JavaScript-Load-Image
 *
 * Copyright 2013, Sebastian Tschan
 * https://blueimp.net
 *
 * Licensed under the MIT license:
 * https://opensource.org/licenses/MIT
 */

/* global loadImage, $ */

$(function () {
	'use strict'

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
		var previewList = resultNode.find("li > canvas");
		thumbNode.empty();
		for (var img of previewList) {
			var thumbnail = loadImage.scale(img, {maxWidth:120});
			var content = $("<li></li>").addClass($(img).parent().attr("class").split(" ")[0]).append(thumbnail);
			thumbNode.append(content);
		};
	}

	/**
	 * Displays metadata
	 *
	 * @param {object} [data] Metadata object
	 */
	function displayMetaData(data) {
		if (!data) return;
		metaNode.data(data);
		var exif = data.exif;
		var iptc = data.iptc;
		if (exif) {
			var thumbnail = exif.get('Thumbnail');
			if (thumbnail) {
//				displayThumbnailImage(thumbNode, thumbnail.get('Blob'), {
//					orientation: exif.get('Orientation')
//				});
			}
			displayTagData(metaNode, exif.getAll(), 'TIFF');
		}
		if (iptc) {
			displayTagData(metaNode, iptc.getAll(), 'IPTC');
		}
	}

	/**
	 * Removes meta data from the page
	 */
	function removeMetaData() {
//		metaNode.hide().removeData().find('table').remove();
//		thumbNode.hide().empty();
	}

	/**
	 * Updates the results view
	 *
	 * @param {*} img Image or canvas element
	 * @param {object} [data] Metadata object
	 * @param {boolean} [keepMetaData] Keep meta data if true
	 */
	function updateResults(img, data, keepMetaData, node) {
		var isCanvas = window.HTMLCanvasElement && img instanceof HTMLCanvasElement
		if (!keepMetaData) {
			removeMetaData();
//			if (data) {
////				displayMetaData(data);
//			}
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
			if (data.exif) {
				// Reset Exif Orientation data:
				loadImage.writeExifData(data.imageHead, data, 'Orientation', 1)
			}
			img.toBlob(function (blob) {
				if (!blob) return;
				loadImage.replaceHead(blob, data.imageHead, function (newBlob) {
//					content.attr('src', loadImage.createObjectURL(newBlob)).attr('download', 'image.jpg');
				})
			}, 'image/jpeg')
		}
		
		makeThumbnailImage();
	}

	/**
	 * Displays the image
	 *
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
	 * Handles drop and file selection change events
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
	 * Handles URL change events
	 */
	function urlChangeHandler() {
		var url = $(this).val();
		if (url) displayImage(url);
	}

	$(document)
		.on('dragover', function (e) {
			e.preventDefault();
			if (event.dataTransfer) event.dataTransfer.dropEffect = 'copy';
		})
		.on('drop', fileChangeHandler);

	fileInputNode.on('change', fileChangeHandler);

	urlNode.on('change paste input', urlChangeHandler);

	orientationNode.on('change', function () {
		var img = resultNode.find('li.active > canvas')[0];
		if (img) {
			updateResults(
				loadImage.scale(img, {
					maxWidth: resultNode.width() * (window.devicePixelRatio || 1),
					pixelRatio: window.devicePixelRatio,
					orientation: Number(orientationNode.val()) || true,
					imageSmoothingEnabled: imageSmoothingNode.is(':checked')
				}),
				metaNode.data(),
				true,
				resultNode.find("li.active")
			)
		}
	});

	editNode.on('click', function (event) {
		event.preventDefault();
		var imgNode = resultNode.find('img, canvas');
		var img = imgNode[0];
		var pixelRatio = window.devicePixelRatio || 1;
		var margin = img.width / pixelRatio >= 140 ? 40 : 0;
			// eslint-disable-next-line new-cap
		imgNode
			.Jcrop({
				setSelect: [
					margin,
					margin,
					img.width / pixelRatio - margin,
					img.height / pixelRatio - margin
				],
				onSelect: function (coords) {
					coordinates = coords;
				},
				onRelease: function () {
					coordinates = null;
				}
			},
				function () {
				jcropAPI = this
			})
			.parent()
			.on('click', function (event) {
				event.preventDefault()
			})
	})

	cropNode.on('click', function (event) {
		event.preventDefault()
		var img = resultNode.find('img, canvas')[0]
		var pixelRatio = window.devicePixelRatio || 1
		if (img && coordinates) {
			updateResults(
				loadImage.scale(img, {
					left: coordinates.x * pixelRatio,
					top: coordinates.y * pixelRatio,
					sourceWidth: coordinates.w * pixelRatio,
					sourceHeight: coordinates.h * pixelRatio,
					maxWidth: resultNode.width() * pixelRatio,
					contain: true,
					pixelRatio: pixelRatio,
					imageSmoothingEnabled: imageSmoothingNode.is(':checked')
				}),
				metaNode.data(),
				true
			)
			coordinates = null
		}
	})

	cancelNode.on('click', function (event) {
		event.preventDefault()
		if (jcropAPI) {
			jcropAPI.release()
			jcropAPI.disable()
		}
	})
})