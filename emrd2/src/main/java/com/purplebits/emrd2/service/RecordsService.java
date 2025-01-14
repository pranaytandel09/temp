package com.purplebits.emrd2.service;

import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import com.purplebits.emrd2.dto.PaginationResponseDTO;
import com.purplebits.emrd2.dto.RecordDTO;
import com.purplebits.emrd2.dto.RecordsPaginationWithSearchRequestDTO;
import com.purplebits.emrd2.dto.RecordsViewDTO;
import com.purplebits.emrd2.entity.RecordsView;

public interface RecordsService {

	RecordDTO updateRecord(RecordDTO recordDTO);

	//RecordDTO fetchRecordById(Integer recordId);
	RecordDTO fetchRecordById(String recordId);

	List<RecordDTO> uploadRecords(List<MultipartFile> files);

	String createSearchQuery(RecordsPaginationWithSearchRequestDTO query);

	PaginationResponseDTO<RecordsViewDTO> getAllRecordsSearchBased(Integer pageNumber, Integer pageSize, String sortBy,
			String sortType, Specification<RecordsView> spec);

	List<RecordDTO> fetchRecordsForEncryptionProcessing();

	List<RecordDTO> fetchRecordsForOcrProcessing();

	Resource downloadRecordById(String recordId, int loggedInUserId);

	RecordDTO uploadExtractedData(String barcodeId, Map<String, String> ocrExtractedData);

}
