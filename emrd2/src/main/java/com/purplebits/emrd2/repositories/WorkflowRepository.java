package com.purplebits.emrd2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.purplebits.emrd2.entity.Workflow;

public interface WorkflowRepository extends JpaRepository<Workflow, Integer> {

}
