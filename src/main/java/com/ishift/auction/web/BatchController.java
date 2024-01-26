package com.ishift.auction.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ishift.auction.service.batch.BatchService;

@RestController
public class BatchController {
	@Autowired
	BatchService batchService; 

	@RequestMapping("/batch/agora/update/{status}")
	public Map<String, Object> updAgoraStatus(@PathVariable(name = "status") String status, @RequestParam final Map<String, Object> params) {
		Map<String, Object> result = new HashMap<>();
		result.put("success", true);
		try {
			batchService.updAgoraStatus(status);
		}catch(Exception e) {
			result.put("success", false);
			result.put("message", "아고라 상태 : "+status+" 변경중 에러.");			
		}
		return result;
	}
}
