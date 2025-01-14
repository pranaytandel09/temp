package com.purplebits.emrd2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.purplebits.emrd2.entity.UserDetailsView;


public interface UserDetailsViewRepository extends JpaRepository<UserDetailsView, Integer>,JpaSpecificationExecutor<UserDetailsView>{

}
