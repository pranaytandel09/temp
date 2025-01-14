package com.purplebits.emrd2.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.purplebits.emrd2.entity.UserGroupDetails;


public interface UserGroupsDetailsRepository extends JpaRepository<UserGroupDetails, Integer>, JpaSpecificationExecutor<UserGroupDetails>{
	
	
}
