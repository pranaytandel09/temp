package com.purplebits.emrd2.repositories;

import com.purplebits.emrd2.entity.EntityPattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntityPatternRepository extends JpaRepository<EntityPattern, Integer> {
    Optional<EntityPattern> findByEntityId(Integer entityId);
}


