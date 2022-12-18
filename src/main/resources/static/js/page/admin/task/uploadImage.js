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
			$(".btn_save").on(clickEvent, function() {
				fnSave();
			});
		};
		
		var fnSave = function() {
			var drawCanvas = $("#result").find("canvas")[0];
			console.log({imgUpload:drawCanvas.toDataURL('image/png')});
			$.ajax({
				url: '/office/task/uploadImageAjax',
				type: 'POST',
				data : JSON.stringify({imgUpload:drawCanvas.toDataURL('image/png')}),
				dataType: 'json',
				beforeSend: function (xhr) {
					xhr.setRequestHeader("Accept", "application/json");
					xhr.setRequestHeader("Content-Type", "application/json");
				},
				success : function() {
				},
				error: function(xhr, status, error) {
					console.log(xhr, status, error);
				}
			}).done(function (body) {
				console.log(body);
			});
		};
		
		
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
