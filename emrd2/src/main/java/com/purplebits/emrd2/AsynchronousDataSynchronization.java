package com.purplebits.emrd2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.purplebits.emrd2.dto.CollectionPrProofDTO;
import com.purplebits.emrd2.dto.RecordDTO;
import com.purplebits.emrd2.service.CollectionPrProofService;
import com.purplebits.emrd2.service.RecordsEncryptionService;
import com.purplebits.emrd2.service.RecordsOcrService;
import com.purplebits.emrd2.service.RecordsService;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;

import javax.transaction.Transactional;

@Component
public class AsynchronousDataSynchronization {

	@Autowired
	private RecordsEncryptionService recordsEncryptionService;
	@Autowired
	private RecordsOcrService recordsOcrService;
	@Autowired
	private RecordsService recordsService;
	@Autowired
	private TaskExecutor ocrExecutor;
	@Autowired
	private TaskExecutor encryptionExecutor;
	@Autowired
	private TaskExecutor ocrRefinementExecutor;
	@Autowired
	private CollectionPrProofService collectionPrProofService;
	

//	@Scheduled(fixedRateString = "${scheduled.processing.interval}")
//	@Transactional
//	public void processRecords() {
//		List<RecordDTO> recordsToBeEncrpted = recordsService.fetchRecordsForEncryptionProcessing();
//		encryptionExecutor.execute(() -> recordsEncryptionService.performeEncryption(recordsToBeEncrpted));
//
//	}
	
	@Scheduled(fixedRateString = "${scheduled.processing.interval}")
	@Transactional
	public void processRecordsOcr() {
		List<CollectionPrProofDTO> recordsToBeOcr =collectionPrProofService.getRecordsForOcr();
		List<CollectionPrProofDTO> recordsForDataExtraction=collectionPrProofService.getRecordsForOcrDataRefinement();

		for (CollectionPrProofDTO collectionPrProofDTO : recordsToBeOcr) {
			ocrExecutor.execute(() -> collectionPrProofService.processFileForOcr(collectionPrProofDTO));
		}
		
for (CollectionPrProofDTO collectionPrProofDTO : recordsForDataExtraction) {
			ocrRefinementExecutor.execute(() -> collectionPrProofService.refineOcrDataAndUpdateTables(collectionPrProofDTO));
		}
	}


}
