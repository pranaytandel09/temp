package com.purplebits.emrd2.repositories;


import com.purplebits.emrd2.entity.TaskManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskManagerRepository extends JpaRepository<TaskManager, Integer> {
}
