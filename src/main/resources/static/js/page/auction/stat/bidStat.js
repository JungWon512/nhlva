$(function() {
	var setLayout = function() {
		addCalendarEvent();
	};

	var setBinding = function() {
		$(".list_sch").click(function(){
			fnSearch();
		});
	};

	setLayout();
	setBinding();
});

var fnSearch = function(){	
	var form = $('form[name="frm"]');
	form.attr('action', '/stat'+window.location.search);
	form.submit();
}
