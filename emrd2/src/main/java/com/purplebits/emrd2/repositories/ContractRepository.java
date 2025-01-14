package com.purplebits.emrd2.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.purplebits.emrd2.entity.Contract;

public interface ContractRepository extends JpaRepository<Contract, Integer>{

	@Query("SELECT c FROM Contract c WHERE c.project.projectId= :projectId")
	Page<Contract> findAllByProjectId(@Param("projectId")Integer projectId, Pageable pageable);

}
