package com.ishift.auction.util;

import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AlarmTalkForm {

	private static Logger log = LoggerFactory.getLogger(AlarmTalkForm.class);
	
	public String getAlarmTalkTemplateToJson(String templateCode, Map<String, Object> paramMap) {
		JSONObject jobj = new JSONObject();
		
		jobj.put("msg_content", paramMap.get("MSG_CNTN"));
		// NHKKO00259 : 출하주, NHKKO00260 : 중도매인
		if ("NHKKO00259".equals(templateCode) || "NHKKO00260".equals(templateCode)) {
			jobj.put("btn_name_1", paramMap.getOrDefault("BTN_NM","버튼명"));
			jobj.put("btn_type_1", "AL");
			
			String appLink = paramMap.getOrDefault("BTN_URL", "https://nhauction.page.link/JwzKzHb6hMzbEXk88").toString();
			jobj.put("btn_content_1", appLink +"$|$" + appLink);
		}
		
		log.debug(jobj.toString());
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
