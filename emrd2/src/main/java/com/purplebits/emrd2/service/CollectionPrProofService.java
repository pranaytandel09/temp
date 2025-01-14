package com.purplebits.emrd2.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.purplebits.emrd2.dto.CollectionPrProofDTO;

public interface CollectionPrProofService {

	CollectionPrProofDTO uploadOcrRecord(MultipartFile file);

	void processFileForOcr(CollectionPrProofDTO collectionPrProofDTO);

	void refineOcrDataAndUpdateTables(CollectionPrProofDTO record);

	List<CollectionPrProofDTO> getRecordsForOcr();

	List<CollectionPrProofDTO> getRecordsForOcrDataRefinement();

	void processFileForOcr(Integer prProofId);

}
