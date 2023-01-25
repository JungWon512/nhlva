package com.ishift.auction.util;

import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AlarmTalkForm {

	private static Logger log = LoggerFactory.getLogger(AlarmTalkForm.class);
	
	@Deprecated
	public String getAlarmTalkTemplate(String templateCode, Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		
		if (!StringUtils.isEmpty(templateCode)) {
			sb.append("[스마트 가축시장]");
			sb.append("\\n");
			sb.append("\\n");
			
			switch (templateCode) {
				case "NHKKO00249" :
					sb.append(paramMap.getOrDefault("REVE_USR_NM","")+" 고객님, "+paramMap.getOrDefault("CLNTNM","")+" 스마트 가축시장입니다.\\n");
					sb.append(paramMap.getOrDefault("AUC_DT_STR","")+" 경매일 "+paramMap.getOrDefault("MSG","")+" 알려드립니다.\\n");
                    sb.append("\\n");
					sb.append("■ "+paramMap.getOrDefault("COW_INFO1","")+"\\n");
					sb.append("\\n");
					sb.append("자세한 내역은 가축시장 앱에서 확인 가능합니다.\\n");
					sb.append("\\n");
					sb.append("문의) "+paramMap.getOrDefault("CLNT_TELNO",""));
					break;
				case "NHKKO00250" :
					sb.append(paramMap.getOrDefault("REVE_USR_NM","")+" 고객님, "+paramMap.getOrDefault("CLNTNM","")+" 스마트 가축시장입니다.\\n");
					sb.append(paramMap.getOrDefault("AUC_DT_STR","")+" 경매일 "+paramMap.getOrDefault("MSG","")+" 알려드립니다.\\n");
					sb.append("\\n");
					sb.append("■ "+paramMap.getOrDefault("COW_INFO1","")+"\\n");
					sb.append("■ "+paramMap.getOrDefault("COW_INFO2","")+"\\n");
					sb.append("\\n");
					sb.append("자세한 내역은 가축시장 앱에서 확인 가능합니다.\\n");
					sb.append("문의) "+paramMap.getOrDefault("CLNT_TELNO",""));
					break;
				case "NHKKO00251" :
					sb.append(paramMap.getOrDefault("REVE_USR_NM","")+" 고객님, "+paramMap.getOrDefault("CLNTNM","")+" 스마트 가축시장입니다.\\n");
					sb.append(paramMap.getOrDefault("AUC_DT_STR","")+" 경매일 "+paramMap.getOrDefault("MSG","")+" 알려드립니다.\\n");
                    sb.append("\\n");
					sb.append("■ "+paramMap.getOrDefault("COW_INFO1","")+"\\n");
					sb.append("■ "+paramMap.getOrDefault("COW_INFO2","")+"\\n");
					sb.append("■ "+paramMap.getOrDefault("COW_INFO3","")+"\\n");
                    sb.append("\\n");
					sb.append("자세한 내역은 가축시장 앱에서 확인 가능합니다.\\n");
					sb.append("문의) "+paramMap.getOrDefault("CLNT_TELNO",""));
					break;
				case "NHKKO00252" :
					sb.append(paramMap.getOrDefault("REVE_USR_NM","")+" 고객님, "+paramMap.getOrDefault("CLNTNM","")+" 스마트 가축시장입니다.\\n");
					sb.append(paramMap.getOrDefault("AUC_DT_STR","")+" 경매일 "+paramMap.getOrDefault("MSG","")+" 알려드립니다.\\n");
					sb.append("\\n");
					sb.append("■ "+paramMap.getOrDefault("COW_INFO1","")+"\\n");
					sb.append("■ "+paramMap.getOrDefault("COW_INFO2","")+"\\n");
					sb.append("■ "+paramMap.getOrDefault("COW_INFO3","")+"\\n");
					sb.append("■ "+paramMap.getOrDefault("COW_INFO4","")+"\\n");
					sb.append("\\n");
					sb.append("자세한 내역은 가축시장 앱에서 확인 가능합니다.\\n");
					sb.append("문의) "+paramMap.getOrDefault("CLNT_TELNO",""));
					break;
				case "NHKKO00253" :
					sb.append(paramMap.getOrDefault("REVE_USR_NM","")+" 고객님, "+paramMap.getOrDefault("CLNTNM","")+" 스마트 가축시장입니다.\\n");
					sb.append(paramMap.getOrDefault("AUC_DT_STR","")+" 경매일 "+paramMap.getOrDefault("MSG","")+" 알려드립니다.\\n");
					sb.append("\\n");
					sb.append("■ "+paramMap.getOrDefault("COW_INFO1","")+"\\n");
					sb.append("■ "+paramMap.getOrDefault("COW_INFO2","")+"\\n");
					sb.append("■ "+paramMap.getOrDefault("COW_INFO3","")+"\\n");
					sb.append("■ "+paramMap.getOrDefault("COW_INFO4","")+"\\n");
					sb.append("■ "+paramMap.getOrDefault("COW_INFO5","")+"\\n");
					sb.append("\\n");
					sb.append("자세한 내역은 가축시장 앱에서 확인 가능합니다.\\n");
					sb.append("문의) "+paramMap.getOrDefault("CLNT_TELNO",""));
					break;
				case "NHKKO00259" :
					sb.append(paramMap.getOrDefault("REVE_USR_NM","")+" 고객님, "+paramMap.getOrDefault("CLNTNM","")+" 스마트 가축시장입니다.\\n");
					sb.append(paramMap.getOrDefault("AUC_DT_STR","")+" 경매일 "+paramMap.getOrDefault("MSG","")+" 알려드립니다.\\n");
					sb.append("\\n");
					sb.append("■ "+paramMap.getOrDefault("COW_INFO1","")+"\\n");
					sb.append("■ "+paramMap.getOrDefault("COW_INFO2","")+"\\n");
					sb.append("■ "+paramMap.getOrDefault("COW_INFO3","")+"\\n");
					sb.append("■ "+paramMap.getOrDefault("COW_INFO4","")+"\\n");
					sb.append("■ "+paramMap.getOrDefault("COW_INFO5","")+"\\n");
					sb.append("\\n");
					sb.append("자세한 내역은 가축시장 앱에서 확인 가능합니다.\\n");
					sb.append("문의) "+paramMap.getOrDefault("CLNT_TELNO",""));
					break;
				case "NHKKO00255" :
					sb.append("비밀번호 번경을 위한 인증번호입니다.\\n");
					sb.append("■인증번호 : #{인증번호}"+"\\n");
					sb.append("\\n");
					sb.append("인증번호의 유효시간은 3분이며, 시간 경과 시 재 인증을 요청하셔야 합니다.");
					break;
				case "NHKKO00256" :
					sb.append("#{조합명} 스마트 가축시장 입니다.\\n");
					sb.append("본인확인 인증번호는 #{인증번호} 입니다.\\n");
					sb.append("문의) #{조합전화번호}");
					break;
				case "NHKKO00257" :
					sb.append("고객님, 안녕하세요\\n");
					sb.append("농협경제지주 스마트가축시장 플랫폼입니다.\\n"); 
                    sb.append("\\n");
					sb.append("1년 이상 로그인하지 않은 회원님의 개인정보 보호를 위하여 『개인정보 보호법』 제39조의6(개인정보의 파기에 대한 특례)에 따라 분리보관 예정입니다. 휴면계정 전환 시 모든 서비스 이용이 제한됩니다.\\n");
                    sb.append("\\n");
					sb.append("■ 적용 대상 : 1년 이상 로그인하지 않은 회원\\n");
					sb.append("■ 적용 내용 : 개인정보 분리보관\\n");
					sb.append("■ 전환 예정일자 : #{전환예정일자}\\n");
					sb.append("■ 분리보관 항목 : 이름, 생년월일, 연락처, 주소\\n");
                    sb.append("\\n");
					sb.append("휴면계정 전환을 원하지 않을 경우 전환 예정일자까지 스마트가축시장 앱 또는 www.가축시장.kr에 로그인하면 계속 이용이 가능합니다.\\n"); 
                    sb.append("\\n");
					sb.append("※ 개인정보 처리 관련 정보성 안내 사항으로 문자서비스를 수신거부한 회원에게도 발송될 수 있음을 안내드립니다.");							
					break;
				case "NHKKO00258" :
					sb.append("고객님, 안녕하세요\\n");
					sb.append("농협경제지주 스마트가축시장 플랫폼입니다.\\n"); 
                    sb.append("\\n");
					sb.append("현재 고객님께서는 스마트가축시장 플랫폼 장기미이용 고객이십니다. 『개인정보 보호법』 제39조의6(개인정보의 파기에 대한 특례)에 따라 스마트가축시장 플랫폼 회원 자동탈퇴 및 개인정보 파기가 진행됨을 알려드립니다.\\n");
                    sb.append("\\n");
					sb.append("■ 적용 대상 : 장기미이용 회원\\n");
					sb.append("■ 적용 내용 : 회원 자동탈퇴 및 개인정보 파기\\n");
					sb.append("■ 파기 예정일자 : #{파기예정일자}\\n");
					sb.append("■ 파기 항목 : 이름, 생년월일, 연락처, 주소\\n");
                    sb.append("\\n");
					sb.append("자동탈퇴 및 개인정보 파기를 원하지 않을 경우 파기 예정일자 전까지 스마트가축시장 앱 또는 www.가축시장.kr에 로그인하면 계속 이용이 가능합니다.\\n"); 
                    sb.append("\\n");
					sb.append("※ 개인정보 처리 관련 정보성 안내 사항으로 문자서비스를 수신거부한 회원에게도 발송될 수 있음을 안내드립니다.");												
					break;
			}
		}
		
		return sb.toString();
	}
	
	public String getAlarmTalkTemplateToJson(String templateCode, Map<String, Object> paramMap) {
		JSONObject jobj = new JSONObject();
		
		// java단에서 알람톡 템플릿을 사용하지않음
		//jobj.put("msg_content", this.getAlarmTalkTemplate(templateCode, paramMap));
		
		jobj.put("msg_content", paramMap.get("MSG_CNTN"));
		// NHKKO00259 : 출하주, NHKKO00260 : 중도매인
		if ("NHKKO00259".equals(templateCode) || "NHKKO00260".equals(templateCode)) {
			jobj.put("btn_name_1", paramMap.getOrDefault("BTN_NM","버튼명"));
			jobj.put("btn_type_1", "AL");
			
			String appLink = paramMap.getOrDefault("BTN_URL", "https://nhauction.page.link/JwzKzHb6hMzbEXk88").toString();
			jobj.put("btn_content_1", appLink +"$|$" + appLink);
		}
		
		log.debug(jobj.toString());
		// gypark : 따옴표 앞에 역슬래쉬를 하나 더 추가해달라고 하셨음
		// json 형태를 인터페이스 측에서 받을 수 없는 상황이라.. 구분자로 역슬래쉬 하나씩 더 추가해서 구분하여 받기로 함
		return jobj.toString().replace("\"", "\\\"");
	}

	public String makeAlarmMsgCntn(Map<String, Object> item, String msgContent) throws Exception {
		msgContent = msgContent.replaceAll("#\\{고객명\\}", item.getOrDefault("REVE_USR_NM","").toString());
		msgContent = msgContent.replaceAll("#\\{조합명\\}", item.getOrDefault("CLNTNM","").toString());
		msgContent = msgContent.replaceAll("#\\{경매일\\}", item.getOrDefault("AUC_DT_STR","").toString());
		msgContent = msgContent.replaceAll("#\\{경매안내문구\\}", item.getOrDefault("MSG","").toString());
		msgContent = msgContent.replaceAll("#\\{출장우현황정보1\\}", item.getOrDefault("COW_INFO1","").toString());
		msgContent = msgContent.replaceAll("#\\{출장우현황정보2\\}", item.getOrDefault("COW_INFO2","").toString());
		msgContent = msgContent.replaceAll("#\\{출장우현황정보3\\}", item.getOrDefault("COW_INFO3","").toString());
		msgContent = msgContent.replaceAll("#\\{출장우현황정보4\\}", item.getOrDefault("COW_INFO4","").toString());
		msgContent = msgContent.replaceAll("#\\{출장우현황정보5\\}", item.getOrDefault("COW_INFO5","").toString());
		msgContent = msgContent.replaceAll("#\\{조합전화번호\\}", item.getOrDefault("CLNT_TELNO","").toString());
		msgContent = msgContent.replaceAll("#\\{전환예정일자\\}", item.getOrDefault("DORMDUE_DT","").toString());
		msgContent = msgContent.replaceAll("#\\{인증번호\\}", item.getOrDefault("ATTC_NO","").toString());
	
		return msgContent;
	}
}
