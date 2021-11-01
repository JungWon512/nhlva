<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>
<script src="https://webrtc.github.io/adapter/adapter-latest.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@remotemonster/sdk/remon.min.js"></script>
<script src="/static/js/socket.io/socket.io.js"></script>
<style>
	table tbody .val td {height : calc(100vh/5);}
</style>
<!--begin::Container-->
<div class="container-fluid" style="margin-top:10px">
	<input type="hidden" id="naBzPlc" value="${johapData.NA_BZPLC}" />
    <!--begin::Dashboard-->
    <!--begin::Row-->
    <div class="row">
        <div class="col-lg-12 align-self-stretch table-responsive">
        	<table class="table table-bordered tblAuctionSt">
        		<tbody>
        			<tr class="w-100vh">
        				<th scope="row" class="text-center h3" style="width:15%;">경매번호</th>
        				<td colspan ='2' rowspan='2' class="tdBoard text-center align-middle display-1" style="width:80%;">        					
        						<p class="font-weight-bolder">경매 대기중입니다.</p>        					
						</td>
        				<th scope="row" class="tdBiddAmt text-center h3 font-weight-bolder" style="display:none;width:40%;">낙찰금액</th>
        				<th scope="row" class="tdBiddNum text-center h3 font-weight-bolder" style="display:none;width:40%;">낙찰번호</th>
        			</tr>
        			<tr class="val">
        				<td class="text-center align-middle display-3 auctionNum">        					
        				</td>
        				<td class="text-center align-middle display-3 font-weight-bolder tdBiddAmt" style="display:none;"></td>
        				<td class="text-center align-middle display-3 font-weight-bolder tdBiddNum" style="display:none;"></td>
        			</tr>
       			</tbody>
        	</table>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-2">
        	<table class="table table-bordered tblAuctionBoard">
        		<tbody>
        			<tr>
        				<th class="text-center" scope="row">출하주</th>
        				<th class="text-center" scope="row">중량</th>
        			</tr>
        			<tr class="val">
        				<td class="text-center ftsnm"></td>
        				<td class="text-center cowSogWt"></td>
        			</tr>
        			<tr>
        				<th class="text-center" scope="row">KPN</th>
        				<th class="text-center" scope="row">어미</th>
        			</tr>
        			<tr class="val">
        				<td class="text-center kpnNo"></td>
        				<td class="text-center mcowDsc"></td>
        			</tr>
        			<tr >
        				<th class="text-center" scope="row">성별</th>
        				<th class="text-center" scope="row">최저가</th>
        			</tr>
        			<tr class="val">
        				<td class="text-center sex"></td>
        				<td class="text-center lowsSbidLmtAm"></td>
        			</tr>
        		</tbody>
        	</table>
        </div>
        <div class="col-lg-10">
            <!--begin::Stats Widget 1-->
            <div class="card card-custom card-stretch gutter-b">
                <!--begin::Body-->
                <div class="card-body  align-items-center justify-content-between pt-7 flex-wrap">
                    <!--begin::label-->
						<div id="carouselExampleIndicators" class="carousel slide" data-ride="carousel" style="width: 100%;height: 100%;">
						  <ol class="carousel-indicators vidioSlide" style="bottom: calc(100%/30);">
						  </ol>
						  <div class="carousel-inner vidioComp" style="width: 100%;height: 95%;">
						  </div>
						  <a class="carousel-control-prev" href="#carouselExampleIndicators" role="button" data-slide="prev">
						    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
						    <span class="sr-only">Previous</span>
						  </a>
						  <a class="carousel-control-next" href="#carouselExampleIndicators" role="button" data-slide="next">
						    <span class="carousel-control-next-icon" aria-hidden="true"></span>
						    <span class="sr-only">Next</span>
						  </a>
						</div>
                    <!--end::Chart-->
                </div>
                <!--end::Body-->
            </div>
            <!--end::Stats Widget 1-->
        </div>
    </div>
    <!--end::Row-->
</div>
<!--end::Container-->

<script>
</script>  