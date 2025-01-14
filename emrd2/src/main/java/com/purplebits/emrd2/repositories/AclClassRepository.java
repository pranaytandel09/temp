package com.purplebits.emrd2.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.purplebits.emrd2.entity.AclClass;

public interface AclClassRepository extends JpaRepository<AclClass, Long> {
	Optional<AclClass> findByClassName(String className);
}
