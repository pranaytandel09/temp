package com.purplebits.emrd2.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.purplebits.emrd2.entity.Entities;
import com.purplebits.emrd2.types.Status;

public interface EntitiesRepository extends JpaRepository<Entities, Integer>,JpaSpecificationExecutor<Entities> {

	@Query("SELECT e FROM Entities e WHERE e.entityName= :entityName")
	Optional<Entities> getByEntityName(@Param("entityName")String entityName);

	@Query("SELECT e FROM Entities e WHERE e.status= :status")
	Page<Entities> findByStatus(@Param("status")Status status, Pageable pageable);

}
