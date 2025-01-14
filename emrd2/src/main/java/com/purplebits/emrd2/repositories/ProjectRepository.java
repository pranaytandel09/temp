package com.purplebits.emrd2.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.purplebits.emrd2.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Integer>{
	
	@Query("SELECT p FROM Project p WHERE p.projectName= :projectName AND p.entity.entityId= :entityId")
	Optional<Project> getByProjectNameAndEntityId(@Param("projectName")String projectName,@Param("entityId") Integer entityId);

	@Query("SELECT p FROM Project p WHERE p.projectDisplayName= :projectDisplayName AND p.entity.entityId= :entityId")
	Optional<Project> getByProjectDisplayNameAndEntityId(@Param("projectDisplayName")String projectName,@Param("entityId") Integer entityId);
	
	@Query("SELECT p FROM Project p WHERE p.entity.entityId= :entityId")
	Page<Project> findAllByEntityId(@Param("entityId")Integer entityId, Pageable pageable);

	@Query("SELECT p.projectDetails FROM Project p WHERE p.projectId= :projectId")
	String findProjectDetailsByProjectId(@Param("projectId")Integer projectId);
}
