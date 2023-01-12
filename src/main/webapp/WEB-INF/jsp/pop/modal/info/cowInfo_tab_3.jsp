<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>
			

<div class="slide-area">
	<input type="hidden" id="imgTotal" value="${ imgList.size() }"/>
	<div class="slide-detail">
		<c:if test="${ imgList.size() <= 0 }">
			<div class="item" name="sample_0"><img src="/static/images/guide/v2/sample01.jpg" alt=""></div>
        </c:if>
		<c:forEach items="${ imgList }" var="item" varStatus="st">
			<div class="item" name="${item.NA_BZPLC}_${item.OSLP_NO}_${item.IMG_SQNO}"><img src="${item.FILE_URL}" alt=""></div>
		</c:forEach>
	</div>
	<span class="slideNumber"></span>
</div>
<div class="slide-btn-box">
	<button type="button" class="btnAllImage">전체보기</button>
</div>

<script>
$(function() {
	var total = $('#imgTotal').val();
	if(total != '0'){
		$('.slideNumber').text('1/' + total);
		var check1 = $('.slide-detail').hasClass('slick-initialized');
		if (!check1) {
			setTimeout(function() { 
				$(".slide-detail").slick({
					dots: false,
					adaptiveHeight: true,
					arrows: false
				});
			}, 200);
		}
		$('.slide-detail').on('afterChange', function(event, slick, currentSlide, nextSlide) {
			var now = currentSlide + 1;
			$('.slideNumber').text(now + '/ ' + total);
		});		
	}else{
		$('.slideNumber').text('0/ ' + total);		
	}

	$(document).on('click','.btnAllImage',function(e){
		var total = $('#imgTotal').val();
		if(total <= 0) return;
		//var target = 'cowImageDetail';			
		//window.open('',target, 'width=600, height=800, toolbar=no, menubar=no, scrollbars=no, resizable=yes');
		var form = document.frm;
		form.action = "/info/cowImageDetail";
		//form.target=target;
		form.submit();		
	});
});
</script>