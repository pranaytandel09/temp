package com.purplebits.emrd2.service.impl;

import com.purplebits.emrd2.dto.PaginationResponseDTO;
import com.purplebits.emrd2.entity.Entities;
import com.purplebits.emrd2.entity.Project;
import com.purplebits.emrd2.entity.RecordEntity;
import com.purplebits.emrd2.exceptions.ProjectNotFoundException;
import com.purplebits.emrd2.repositories.RecordRepository;
import com.purplebits.emrd2.util.ResponseMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BarcodeService {

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    Environment environment;

    @Transactional
    public PaginationResponseDTO<String> generateBarcodes(Integer entityId, Integer projectId, Integer count) {

		PaginationResponseDTO<String> response = new PaginationResponseDTO<>();
		
        Entities entity = entityManager.find(Entities.class, entityId);
        if (entity == null) {
            throw new EntityNotFoundException(environment.getProperty(ResponseMessages.ENTITY_NOT_EXIST));
        }

        Project project = entityManager.find(Project.class, projectId);
        if (project == null) {
            throw new ProjectNotFoundException(environment.getProperty(ResponseMessages.PROJECT_NOT_EXIST));
        }

        // Fetch last record for generating barcode sequence
        RecordEntity lastRecord = recordRepository.findTopByEntityOrderByRecordIdDesc(entity);
        int lastDocumentId = lastRecord != null ? extractDocumentId(lastRecord.getRecordId()) : 0;
        int batchNumber = lastRecord != null ? extractBatchNumber(lastRecord.getRecordId()) : 1;

        List<String> barcodes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            lastDocumentId++;
            if (lastDocumentId > 99) {
                lastDocumentId = 1;
                batchNumber++;
            }

            String barcode = String.format("E%dP%dB%02d%02d%02dD%02d",
                    entityId, projectId, batchNumber, LocalDate.now().getMonthValue(), LocalDate.now().getYear() % 100, lastDocumentId);

            // Create and save the record entity
            RecordEntity record = new RecordEntity();
            record.setEntity(entity);
            record.setProject(project);
            record.setRecordId(barcode);
            record.setBarcode(barcode); // Ensure unique constraint
            record.setScannedStatus(0); // Default to not scanned
            record.setOcrStatus(0);     // Default to not OCR-ed
            record.setUploadStatus(0);  // Default to not uploaded
            record.setNameTag("Default Name");

            recordRepository.save(record);
            barcodes.add(barcode);
        }
        response.setData(barcodes);
        return response;
    }

    private int extractDocumentId(String recordId) {
        return Integer.parseInt(recordId.substring(recordId.indexOf("D") + 1));
    }

    private int extractBatchNumber(String recordId) {
        return Integer.parseInt(recordId.substring(recordId.indexOf("B") + 1, recordId.indexOf("D")));
    }

}