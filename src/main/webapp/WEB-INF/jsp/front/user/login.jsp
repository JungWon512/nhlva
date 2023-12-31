<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>

<input type="hidden" name="kkoLoginResult" value="${kkoLoginResult}" />
<input type="hidden" name="kkoLoginResultMsg" value="${kkoLoginResultMsg}" />
<div class="login_area sms_authentication">
	<form name="frm" id="frm" method="post">
		<input type="hidden" name="place" value="${place}" />
		<input type="hidden" name="type" value="${empty type ? '0' : type}" />
<%-- 		<input type="hidden" name="rtnUrl" value="${rtnUrl}" /> --%>
		<div class="login_top" id="login_info">
			<h3>${johapData.CLNTNM}</h3>
			<dl>
				<dd><input type="text" id="userName" name="userName" placeholder="이름" maxlength="20" required autofocus/></dd>
			<c:choose>
			<c:when test="${type ne '1'}">
				<dd><input type="text" id="password" name="birthDate" placeholder="생년월일(6자리) 또는 사업자번호" maxlength="10" pattern="\d*" required inputmode="numeric"/></dd>
			</c:when>
			<c:otherwise>
				<dd><input type="text" id="password" name="telNo" placeholder="전화번호 또는 휴대전화번호" maxlength="11" pattern="\d*" required inputmode="numeric" /></dd>
			</c:otherwise>
			</c:choose>
			</dl>
			<ul class="agree_list">
				<li>
					<input type="checkbox" id="agree_chkAll"><label for="agree_chkAll" class="w30">전체 동의하기</label>
				</li>
				<li>
					<input type="checkbox" id="agree_chk1" class="agree_chk"><label for="agree_chk1">[필수] 개인정보 이용약관 동의</label>
					<a href="#" onclick="window.open('/privacy', '개인정보 처리 방침', 'width=800, height=700, toolbar=no, menubar=no, scrollbars=no, resizable=yes' );return false;"  class="agree_info" >팝업</a>
				</li>
				<li>
					<input type="checkbox" id="agree_chk2" class="agree_chk"><label for="agree_chk2">[필수] 가축시장 시스템 이용약관 동의</label>
					<a href="#" onclick="window.open('/agreement/new', '이용약관', 'width=800, height=700, toolbar=no, menubar=no, scrollbars=no, resizable=yes' );return false;"  class="agree_info" >팝업</a>
				</li>
				<li>
					<input type="checkbox" id="agree_chk3" class="agree_chk"><label for="agree_chk3">[필수] 가축시장 제3자 정보제공 동의</label>
					<a href="#" onclick="window.open('/thirdAgreement', '제3자 정보제공 동의', 'width=800, height=700, toolbar=no, menubar=no, scrollbars=no, resizable=yes' );return false;"  class="agree_info" >팝업</a>	
					<!-- 제3자 정보제공 동의 내용 팝업 필요함 -->
				</li>
			</ul>
			<a href="javascript:;" class="btn_login">로그인</a>
			<c:if test="${type ne '1' }">
				<div class="sns_login" style="display:none;">
					<a href="javascript:loginWithKakao();" class="btn_kakao">카카오 로그인</a>
				</div>
			</c:if>
		</div>
	</form>
	<form name="frm_auth" id="frm_auth" method="post">
		<input type="hidden" name="naBzplc" value="" />
		<input type="hidden" name="token" value="" />
		<input type="hidden" name="connChannel" value="" />
		<input type="hidden" name="place" value="${place}" />
		<input type="hidden" name="type" value="${empty type ? '0' : type}" />
		
		<div class="login_top" id="login_sms_auth" style="display:none;">
			<h3>${johapData.CLNTNM }</h3>
			<dl>
				<dd>
					<div class="authentication_number">
						<input type="text" name="authNumber" id="authNumber" placeholder="인증번호 4자리" maxlength="4" pattern="\d*" required inputmode="numeric" />
						<!-- <button type="button" class="btn_resend">재발송</button> -->
					</div>
					<p class="step_msg">휴대폰으로 수신된 인증번호를 입력하세요.</p>
					<p class="err_msg" style="display:none;">인증번호가 올바르지 않습니다.</p>
				</dd>
			</dl>
			<a href="javascript:;" class="btn_ok btn_confirm">확인</a>
			<a href="javascript:;" class="btn_cancel">취소</a>
		</div>
		<!-- //login_top e -->
		<div class="login_bottom">
			<p class="login_info">${johapData.CLNTNM} 이용문의</p>
			<p class="cs_phone">${johapData.TEL_NO }</p>
			<p class="cs_clock">월-금 9:00~18:00<br>(점심 12:00~13:00)</p>
		</div>
		<!-- //login_bottom e -->
	</form>
	<div id="" class="modal-wrap pop_Terms pop_agreement">
		<div class="modal-content">
			<button class="modal_popup_close" onclick="modalPopupClose('.pop_agreement');return false;">닫기</button>
			<h3>이용약관</h3>
			<div class="pop_TermsBox mCustomScrollBox">
				<p>
					제1조 (정의)<br/>
					&nbsp;&nbsp;1. “개설자”라 함은 「축산법」 제34조에 의거 가축시장을 개설한 축산업협동조합을(이하 “조합”이라 한다) 말한다.<br/>
					&nbsp;&nbsp;2. “가축거래”라 함은 가축의 매매를 말한다.<br/>
					&nbsp;&nbsp;3. “가축시장”이라 함은 가축거래를 위해 개설되는 시장으로서, 일정 공간과 시설을 마련하고 정기적 또는 지속적으로 개장되는 시장을 말한다.<br/>
					&nbsp;&nbsp;4. “대상가축”이라 함은 매매를 목적으로 가축시장에 출장된 한우를 말한다.<br/>
					&nbsp;&nbsp;5. “매도인”이라 함은 사육 중인 대상가축을 조합에 판매위탁을 요청한 생산자를 말한다.<br/>
					&nbsp;&nbsp;6. “매수인”이라 함은 가축시장에서 거래를 통하여 대상가축을 매입한 농업인 및 가축거래상인을 말한다.<br/>
					&nbsp;&nbsp;7. “사고적립금”이라 함은 가축시장에서 거래한 대상가축이 질병, 사고 등으로 인하여 피해를 본 경우 또는 매매대금을 회수하지 못한 경우 발생하는 손해를 보전하기 위하여 개설자가 정하여 조합에 적립하는 기금을 말한다.<br/>
					&nbsp;&nbsp;8. “출장”이라 함은 가축시장에 매매를 목적으로 대상가축을 출하하는 것을 말한다.<br/><br/>
					
					제2조 (대상가축신고)<br/>
					① 매도인은 대상가축의 특징(개월령, 외모, 혈통, 친자확인 등), 질병 및 약물투입, 기타 거래 가격 형성에 영향을 미칠 수 있는 사항 등을 개설자에게 신고하여야 한다.<br/>
					② 개설자는 제1항에 의한 신고사항을 가축시장 이용자에게 충분히 공표하여야 한다.<br/><br/>
					
					제3조 (가축의 검사)<br/>
					① 개설자는 가축시장 특성에 따라 기본적인 검사 기준을 정하여 운영할 수 있다.<br/>
					② 개설자는 가축거래 당사자의 요구가 있을 때에는 언제든지 대상가축의 질병여부 및 임신감정 등 경매가 원활히 진행될 수 있는 검사를 할 수 있다.<br/>
					③ 개설자는 가격사정 또는 수의사 검사 후 이상 징후 발견 시 매도인에게 즉시 알려야 하며, 가축시장 자체 방침에 의거 출장 제외할 수 있다.<br/>
					④ 개설자가 이상 징후 등 하자가축임을 알렸음에도 불구하고 경매가 진행될 경우에는 응찰자에게 반드시 알려야 한다. 이때 응찰자는 하자가축에 대한 내용을 인지하고 경락 받은 경우 반품·응찰포기 및 추후 이의제기를 할 수 없다.<br/><br/>
					
					제4조 (경매)<br/>
					① 경매는 공정하고 객관적인 가격이 형성될 수 있는 방법에 의하여야 한다.<br/>
					② 경매사는 경매 실시 전 대상가축의 축주, 품종 등과 경락 후 경락사항 등을 경매에 참가한 모든 사람에게 공표하여야 한다.<br/>
					③ 대상가축의 경락은 최고가격 응찰자로 한다. 다만, 가축시장 운영 특성에 따라 경매 시작 전매도인이 서면으로 거래 성립 예정가격을 제시한 경우에는 그 가격 미만으로 판매할 수 없다.<br/><br/>
					
					제5조 (예정가격 결정)<br/>
					① 경매 예정가격 결정을 위하여 예정가격사정위원을 운용할 수 있다.<br/>
					② 예정가격사정위원은 개설자가 지정하되, 인원은 2인 이상으로 운용하여야 한다.<br/>
					③ 예정가격은 예정가격사정위원이 정한 시세와 방법을 기준으로 한다.<br/><br/>
					
					제6조 (가축매매수수료 징수 및 정리)<br/>
					① 개설자는 가축시장 운영과 유지관리를 위하여 대상가축 매도인·매수인으로부터 가축매매수수료를 징수할 수 있으며, 영수증을 발급할 수 있다.<br/>
					② 개설자가 징수하는 가축매매수수료는 거래금액의 1,000분의 10이내에서 정액으로 정한다. 다만, 제1항 목적 이외에 출자적립금, 출하장려금, 운송지원비, 친자확인비, 백신접종비 등 기타 추가비용으로 가축매매수수료가 1,000분의 10을 초과할 경우 이사회 의결을 거쳐 따로 정할 수 있다.<br/>
					③ 제2항 거래금액의 기준은 개설자가 정한다.<br/>
					④ 가축매매수수료는 매도인수수료, 매수인수수료, 사고적립금, 운송료, 감정료 등으로 구분하여 정리 할 수 있다.<br/>
					  
					제7조 (대금의 정산관리)<br/>
					① 대금의 정산은 현금지급 또는 송금을 원칙으로 한다.<br/> 
					② 대금은 경락 후 즉시 정산하여야 한다. 다만, 가축시장 운영상 불가피한 경우 이사회의 승인을 얻어 대금결제기한을 별도로 정할 수 있다.<br/>
					③ 개설자는 경매대금 회수가 불가능 할 경우 응찰자의 재산조사를 실시하여 채권보전을 할 수 있는 조치를 하여야 한다.<br/><br/>
					
					제8조 (담합행위의 금지 등)<br/>
					① 가축시장에서 가축을 거래하고자 하는 자는 공정한 가격이 성립하는 것을 저해할 목적 또는 부정한 이익을 얻을 목적으로 담합하여서는 아니된다.<br/>
					② 개설자는 가축시장의 질서를 유지하고 제1항에 의한 부정행위를 방지하기 위하여 필요한 조치를 하여야 한다.<br/><br/>
					
					제9조 (가축시장 이용제한)<br/>
					개설자는 대상가축의 방역 및 공정한 거래확립을 위하여 일부가축의 입장을 금지하거나 다음 각 호에 해당하는 경우 가축시장 이용을 제한 할 수 있다.<br/> 
					&nbsp;&nbsp;1. 가축시장 업무를 방해하거나 질서를 문란하게 한 자 또는 그러할 우려가 있는 자<br/>
					&nbsp;&nbsp;2. 고의로 가축시장 시설을 파손한 자 또는 그러할 우려가 있는 자 <br/>
					&nbsp;&nbsp;3. 제11조 제1항에 의한 신고를 고의로 회피한 자<br/>
					&nbsp;&nbsp;4. 기타 정당한 사유 없이 가축거래 및 가축시장 시설이용에 관한 개설자의 지시사항을 준수하지 아니한 자<br/>
					&nbsp;&nbsp;5.「가축전염병 예방법」에 따라 격리 조치가 필요하다고 인정될 때<br/><br/>
					
					제10조 (경매 후 하자가축)<br/>
					① 경매 후 하자가축이라 함은 다음 각 호로 한다.<br/>
					&nbsp;&nbsp;1. 제2조 제1항에 의하여 신고를 정확히 하지 않은 개체<br/>
					&nbsp;&nbsp;2. 외관상 확인이 어려운 하자(프리마틴, 복강내 고환, 외고환, 맹목(장님) 등)<br/>
					&nbsp;&nbsp;3. 수의사진단서나 객관적으로 판단했을 때 분명히 이상이 있다고 판단되는 개체<br/>
					&nbsp;&nbsp;4. 기타 개설자가 정한 사항<br/>
					② 경매 후 낙찰자가 하자가축을 반송하고자 할 때는 경매당일로부터 개설자가 정한 기일 이내에 조합으로 연락해야 한다. 경매당일로부터 개설자가 정한 기일 이후 발생하는 하자에 대해서는 조합에서 책임을 지지 않는다.<br/>
					③ 하자가축 유형은 개설자가 정하여 공시·공포한다.<br/>
					④ 경매낙찰 후 개설자가 정한 기일 이내 경매진행시 공지하지 않은 하자가 발생하여 매수인이 이의를 제기하는 경우 매수인은 매도인(출하주)과 협의하여 가격을 조정하거나 반송할 수 있으며, 그에 따른 비용은 협의하여 정할 수 있다. 가격조정이 안 될 경우 개설자는 운영협의회를 통하여 가격을 조정하거나 반송 등을 할 수 있다. 단, 하자 내용을 경매 전 고지하여 이를 알고 거래된 개체와 수송 스트레스나 매수자 과실로 인한 하자는 이의제기를 할 수 없다.<br/><br/>
					개정일자 : 2021년 10월 7일
				</p>
			</div>
		</div>
		<!-- //modal-content e -->
	</div>
	<div id="" class="modal-wrap pop_Terms pop_privacy">
		<div class="modal-content">
			<button class="modal_popup_close" onclick="modalPopupClose('.pop_privacy');return false;">닫기</button>
			<h3>개인정보 처리방침</h3>
			<div class="pop_TermsBox mCustomScrollBox">
				<p>
					제1조 (개인정보의 처리 목적)<br/>
					개인정보를 다음의 목적을 위해 처리합니다. 처리한 개인정보는 다음의 목적 이외의 용도로는 사용되지 않으며 이용 목적이 변경될 시에는 사전 동의를 구할 예정입니다.<br/>
					당 플랫폼은 서비스 제공을 목적으로 “이용약관”에 제시하는 내용에 따라 최소한의 개인정보를 수집합니다.<br/>
					각 정보는 플랫폼을 통해 수집하며 수집목적은 다음과 같습니다.<br/>
					① 이름, 생년월일, 연락처 : 본인 식별을 통해 스마트 가축시장 플랫폼 참여 목적<br/><br/>
					
					제2조 (개인정보의 처리 및 보유기간)<br/>
					보유근거 : 개인정보보호법 제15조제1항에 의거 정보주체의 동의를 통한 개인정보 수집 및 이용<br/>
					보유기간 : 개인정보는 수집·이용 목적으로 명시한 범위 내에서 처리하며, 개인정보보호법 및 관련 법령에서 정하는 보유기간을 준용하여 이행<br/><br/>
					
					제3조 (개인정보의 제3자 제공에 관한 사항)<br/>
					스마트 가축시장 플랫폼은 원칙적으로 이용자의 개인정보를 제1조(개인정보 처리 목적)에서 명시한 범위 내에서 처리하며, 이용자의 사전 동의 없이는 본래의 범위를 초과하여 처리하거나 제3자에게 제공하지 않습니다. 단 다음의 경우에는 개인정보를 처리할 수 있습니다.<br/>
					- 법률에 특별한 규정이 있거나 법령상 의무를 준수하기 위하여 불가피한 경우<br/>
					- 공공기관이 법령 등에서 정하는 소관 업무의 수행을 위하여 불가피한 경우<br/>
					- 정보주체 또는 그 법정대리인이 의사표시를 할 수 없는 상태에 있거나 주소불명 등으로 사전 동의를 받을 수 없는 경우로서 명백히 정보주체 또는 제3자의 급박한 생명, 신체, 재산의 이익을 위하여 필요하다고 인정되는 경우<br/>
					- 통계작성 및 학술연구 등의 목적을 위하여 필요한 경우로서 특정 개인을 알아볼 수 없는 형태로 개인정보를 제공하는 경우<br/>
					- 개인정보를 목적 외의 용도로 이용하거나 이를 제3자에게 제공하지 아니하면 다른 법률에서 정하는 소관 업무를 수행할 수 없는 경우로서 보호위원회의 심의·의결을 거친 경우<br/>
					- 범죄의 수사와 공소의 제기 및 유지를 위하여 필요한 경우<br/>
					- 법원의 재판업무 수행을 위하여 필요한 경우<br/>
					- 형(刑) 및 감호, 보호처분의 집행을 위하여 필요한 경우<br/><br/>
					
					제4조 (개인정보처리의 위탁에 관한 사항)<br/>
					스마트 가축시장 플랫폼은 이용자의 동의 없이 해당 개인정보를 타인에게 위탁하지 않습니다. 향후 개인정보처리 위탁 필요가 생길 경우, 위탁대상자, 위탁업무내용, 위탁기간, 위탁계약내용(개인정보보호 관련 법규의 준수, 개인정보에 관한 제3자 제공 금지 및 책임부담 등을 규정)을 개인정보처리방침을 통해 고지하겠습니다. 또한 필요한 경우 사전동의를 받도록 하겠습니다.<br/><br/>
					
					제5조 (정보주체와 법정대리인의 권리, 의무 및 그 행사방법)<br/>
					이용자는 개인정보주체로서 다음과 같은 권리를 행사할 수 있습니다.<br/><br/>
					
					① 정보주체는 스마트 가축시장 플랫폼에 대해 언제든지 다음 각 호의 개인정보 보호 관련 권리를 행사할 수 있습니다.<br/>
					- 개인정보 열람요구<br/>
					- 오류 등이 있을 경우 정정 요구<br/>
					- 삭제요구<br/>
					- 처리정지 요구<br/><br/>
					
					② 제1항에 따른 권리 행사는 스마트 가축시장 플랫폼에 대해 개인정보 처리방법에 관한 고시 별지 제8호 서식에 따라 서면, 전자우편, 모사전송(FAX) 등을 통하여 하실 수 있으며 스마트 가축시장 플랫폼은 이에 대해 지체 없이 조치하겠습니다.<br/><br/>
					
					③ 정보주체가 개인정보의 오류 등에 대한 정정 또는 삭제를 요구한 경우에는 스마트 가축시장 플랫폼은 정정 또는 삭제를 완료할 때까지 당해 개인정보를 이용하거나 제공하지 않습니다.<br/><br/>
					
					④ 제1항에 따른 권리 행사는 정보주체의 법정대리인이나 위임을 받은 자 등 대리인을 통하여 하실 수 있습니다. 이 경우 개인정보 처리방법에 관한 고시 별지 제11호 서식에 따른 위임장을 제출하셔야 합니다.<br/><br/>
					
					제6조 (처리하는 개인정보의 항목)<br/>
					가. 실명인증<br/>
					- 공공 I-PIN 혹은 휴대폰번호 인증을 통한 본인 확인 정보<br/></br>
					
					나. 스마트 가축시장 플랫폼 참여이력<br/>
					- 필수항목 : 이름, 생년월일, 연락처, 주소<br/>
					- 선택항목 : 없음<br/><br/>
					
					제7조 (개인정보의 파기에 관한 사항)<br/>
					스마트 가축시장 플랫폼은 원칙적으로 개인정보 처리목적이 달성된 경우에는 지체없이 해당 개인정보를 파기합니다. 다만, 다른 법령에 따라 보존하여야 하는 개인정보는 파기 되지 아니합니다. 파기의 절차, 기한 및 방법은 다음과 같습니다.<br/><br/>
					
					가. 파기절차<br/>
					이용자가 입력한 정보는 목적 달성 후 별도의 DB에 옮겨져(종이의 경우 별도의 서류) 내부 방침 및 기타 관련 법령에 따라 일정기간 저장된 후 혹은 즉시 파기됩니다. 이 때, DB로 옮겨진 개인정보는 법률에 의한 경우가 아니고서는 다른 목적으로 이용되지 않습니다.<br/><br/>
					
					나. 파기기한<br/>
					이용자의 개인정보는 개인정보의 보유기간이 경과한 경우에는 그 종료한 날에, 개인정보의 처리목적 달성, 해당 업무의 폐지 등 그 개인정보가 불필요하게 되었을 때에는 처리가 불필요한 것으로 인정되는 날에 지체 없이 파기합니다.<br/><br/>
					
					다. 파기방법<br/>
					전자적 파일 형태의 정보는 기록을 재생할 수 없는 기술적 방법을 사용합니다. 종이에 출력된 개인정보는 분쇄기로 분쇄하거나 소각을 통하여 파기합니다.<br/><br/>
					
					제8조 (개인정보의 안전성 확보 조치)<br/>
					스마트 가축시장 플랫폼은 개인정보보호법 제29조 및 동법 시행령 제30조에 따라 다음과 같이 안전성 확보에 필요한 기술적/관리적 및 물리적 조치를 하고 있습니다.<br/><br/>
					
					1. 개인정보 취급 직원의 최소화 및 교육 : 개인정보를 취급하는 직원을 지정하고 담당자에 한정시켜 최소화 하고 개인정보 취급 직원에 대한 안전관리 교육을 실시하여 개인정보를 관리하는 대책을 시행하고 있습니다.<br/>
					2. 내부관리계획의 수립 및 시행 : 개인정보의 안전한 처리를 위하여 내부관리계획을 수립하고 시행하고 있습니다.<br/>
					3. 개인정보의 암호화 : 이용자의 개인정보 중 비밀번호는 암호화 되어 저장 및 관리되고 있어 본인만이 알 수 있으며 중요한 데이터는 파일 및 전송 데이터를 암호화 하는 등의 별도 보안기능을 사용하고 있습니다.<br/>
					4. 해킹 등에 대비한 기술적 대책 : 스마트 가축시장 플랫폼은 해킹이나 컴퓨터 바이러스 등에 의한 개인정보 유출 및 훼손을 막기 위하여 보안프로그램을 설치하고 주기적인 갱신·점검을 하며 외부로부터 접근이 통제된 구역에 시스템을 설치하고 기술적/물리적으로 감시 및 차단하고 있습니다.<br/>
					5. 개인정보에 대한 접근 제한 : 개인정보를 처리하는 데이터베이스시스템에 대한 접근권한의 부여,변경,말소를 통하여 개인정보에 대한 접근통제를 위하여 필요한 조치를 하고 있으며 침입차단시스템을 이용하여 외부로부터의 무단 접근을 통제하고 있습니다.<br/>
					6. 접속기록의 보관 및 위변조 방지 : 개인정보처리시스템에 접속한 기록을 최소 6개월 이상 보관, 관리하고 있으며, 접속 기록이 위변조 및 도난, 분실되지 않도록 보안기능을 사용하고 있습니다.<br/><br/>
					
					제9조 (개인정보 보호책임자)<br/>
					1. 스마트 가축시장 플랫폼은 개인정보 처리에 관한 업무를 총괄해서 책임지고, 개인정보 처리와 관련한 정보주체의 불만처리 및 피해구제 등을 위하여 아래와 같이 개인정보 보호책임자를 지정하고 있습니다.<br/>
					개인정보 보호책임자 : 축산지원부장<br/>
					개인정보 보호 담당부서<br/>
					부서명 : 축산지원부<br/>
					담당자 : 최진 차장<br/>
					연락처 : 02-2080-6554<br/>
					팩스 : 02-2080-6560<br/>
					2. 정보주체께서는 스마트 가축시장 플랫폼 서비스(또는 사업)을 이용하시면서 발생한 모든 개인정보 보호 관련 문의, 불만처리, 피해구제 등에 관한 사항을 개인정보 보호책임자 및 담당부서로 문의하실 수 있습니다. 스마트 가축시장 플랫폼은 정보주체의 문의에 대해 지체 없이 답변 및 처리해드릴 것입니다.<br/><br/>
					
					제10조 (권익침해 구제방법)<br/>
					개인정보주체는 개인정보침해로 인한 피해를 구제 받기 위하여 개인정보 분쟁조정위원회, 한국인터넷진흥원 개인정보 침해-신고센터 등에 분쟁해결이나 상담 등을 신청할 수 있습니다.<br/>
					개인정보 분쟁조정위원회 : 1833-6972 (www.kopico.go.kr)<br/>
					개인정보 침해신고센터 : (국번없이) 118 (privacy.kisa.or.kr)<br/>
					대검찰청 사이버수사과 : (국번없이) 1301, cid@spo.go.kr, (www.spo.go.kr)<br/>
					경찰청 사이버안전국 : (국번없이) 182, (cyberbureau.police.go.kr)<br/>
					또한, 개인정보의 열람, 정정·삭제, 처리정지 등에 대한 정보주체자의 요구에 대하여 공공기관의 장이 행한 처분 또는 부작위로 인하여 권리 또는 이익을 침해 받은 자는 행정심판법이 정하는 바에 따라 행정심판을 청구할 수 있습니다.<br/>
					중앙행정심판위원회(www.simpan.go.kr)의 전화번호 안내 참조<br/><br/>
					
					제11조 (개인정보 처리방침 변경)<br/>
					이 개인정보처리방침은 2021년 10월 8일로부터 적용되며, 법령 및 방침에 따른 변경내용의 추가, 삭제 및 정정이 있는 경우에는 변경사항의 시행 7일 전부터 공지사항을 통하여 고지할 것입니다.<br/><br/>
					개정일자 : 2021년 10월 7일
				</p>
			</div>
		</div>
		<!-- //modal-content e -->
	</div>
</div>
<!-- //login_area e -->

<script type="text/javascript" src="/static/js/common/core.min.js"></script>
<script type="text/javascript" src="/static/js/common/sha512.min.js"></script>

