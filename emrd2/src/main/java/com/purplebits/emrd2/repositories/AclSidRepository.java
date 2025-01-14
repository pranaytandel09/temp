package com.purplebits.emrd2.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.purplebits.emrd2.entity.AclSid;

public interface AclSidRepository extends JpaRepository<AclSid, Long> {
	Optional<AclSid> findBySid(String sid);
}
