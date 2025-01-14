package com.purplebits.emrd2.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.purplebits.emrd2.entity.RecordEntity;

public interface RecordsRepository extends JpaRepository<RecordEntity, String> {

	@Query("SELECT r FROM RecordEntity r WHERE r.barcode =:barcode")
	Optional<RecordEntity> findByBarcode(@Param("barcode") String barcode);

	List<RecordEntity> findByUploadStatusAndPhysicalStatusAndPerformeEncryption(
            int uploadStatus, int physicalStatus, boolean performeEncryption);
	
	 List<RecordEntity> findByUploadStatusAndOcrStatusAndPerformeOCR(
	            int uploadStatus, int ocrStatus, boolean performeOCR);
}
