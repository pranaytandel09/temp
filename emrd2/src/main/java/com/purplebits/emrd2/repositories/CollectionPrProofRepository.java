package com.purplebits.emrd2.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.purplebits.emrd2.entity.CollectionPrProof;

public interface CollectionPrProofRepository extends JpaRepository<CollectionPrProof, Integer> {

	@Query("SELECT c FROM CollectionPrProof c WHERE c.record.recordId =: recordId")
	Optional<CollectionPrProof> findByRecordRecordId(@Param("recordId")String recordId);

	@Query("SELECT c FROM CollectionPrProof c WHERE c.isOcrDone = :isOcrDone")
	List<CollectionPrProof> findByIsOcrDone(@Param("isOcrDone")boolean isOcrDone);
	
	@Query("SELECT c FROM CollectionPrProof c WHERE c.isOcrDone = :isOcrDone AND c.isDataExtractionDone = :isDataExtractionDone")
	List<CollectionPrProof> findByIsOcrDoneAndIsDataExtractionDone(@Param("isOcrDone")boolean isOcrDone,
			@Param("isDataExtractionDone") boolean isDataExtractionDone);

}
