$(function() {
    

    var setLayout = function() {
    };

    var setBinding = function() {
		$(document).on('click','.btn_prev',function(){
			var form = $('<form></form>');
			form.attr('action', '/calendar'+window.location.search);
			form.attr('method', 'post');
			form.appendTo('body');			
			form.append($("<input type='hidden' value="+$('input[name=searchYm]').val()+" name='searchYm'>"));
			form.append($("<input type='hidden' value='prev'  name='flag'>"));
			form.submit();
		});
		$(document).on('click','.btn_next',function(){			
			var form = $('<form></form>');
			form.attr('action', '/calendar'+window.location.search);
			form.attr('method', 'post');
			form.appendTo('body');			
			form.append($("<input type='hidden' value="+$('input[name=searchYm]').val()+" name='searchYm'>"));
			form.append($("<input type='hidden' value='next'  name='flag'>"));
			form.submit();
		});
    };

    setLayout();
    
    setBinding();
});

