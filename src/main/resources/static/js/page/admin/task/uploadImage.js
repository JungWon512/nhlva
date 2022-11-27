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
