package com.ishift.auction.batch;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ishift.auction.service.batch.BatchService;

@Component
public class TaskBatch {
	private static Logger log = LoggerFactory.getLogger(TaskBatch.class);
	@Autowired
	BatchService batchService;
	/**
	 * Agora Project Status : Disabled
	 * after Channel List Kicking
	 */
	
	public void closeAgora() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime date = LocalDateTime.now();
		log.info("s: closeAgora ::"+date.format(format));
		try {
			batchService.updAgoraStatus("close");
		}catch(Exception e) {
			log.error("Batch Agora Start error",e);
		}
		date = LocalDateTime.now();
		log.info("e: closeAgora ::"+date.format(format));
	}

	
	public void openAgora() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime date = LocalDateTime.now();
		log.info("s: openAgora ::"+date.format(format));
		try {
			batchService.updAgoraStatus("open");
		}catch(Exception e) {
			log.error("Batch Agora Start error",e);
		}
		date = LocalDateTime.now();
		log.info("e: openAgora ::"+date.format(format));
	}
}
