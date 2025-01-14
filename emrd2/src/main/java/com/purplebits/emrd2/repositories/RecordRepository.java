package com.purplebits.emrd2.repositories;

import com.purplebits.emrd2.entity.Entities;
import com.purplebits.emrd2.entity.RecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends JpaRepository<RecordEntity, String> {

    RecordEntity findTopByEntityOrderByRecordIdDesc(Entities entity);

}
