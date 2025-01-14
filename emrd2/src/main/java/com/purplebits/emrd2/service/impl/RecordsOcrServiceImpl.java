package com.purplebits.emrd2.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.purplebits.emrd2.dto.RecordDTO;
import com.purplebits.emrd2.service.RecordsOcrService;
import com.purplebits.emrd2.service.RecordsService;
import com.purplebits.emrd2.util.ObjectMapperUtils;

@Service
public class RecordsOcrServiceImpl implements RecordsOcrService {

	private final Logger logger = LogManager.getLogger(RecordsOcrServiceImpl.class);
	private final String className = RecordsOcrServiceImpl.class.getSimpleName();
	
	@Autowired
	private RecordsService recordsService;
	@Override
	public void performeOcr(List<RecordDTO> recordsForOcr) {
		logger.info("method to performe ocr of record invoked.");
		logger.debug(className + " performeOcr()");
		
		for(RecordDTO recordDTO : recordsForOcr) {
			
			RecordDTO record = recordsService.fetchRecordById(recordDTO.getRecordId());
			record.setOcrStatus(1);
			recordsService.updateRecord(ObjectMapperUtils.map(record, RecordDTO.class));
		}
	}

}
