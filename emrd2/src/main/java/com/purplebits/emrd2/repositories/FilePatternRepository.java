package com.purplebits.emrd2.repositories;

import com.purplebits.emrd2.entity.FilePattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FilePatternRepository extends JpaRepository<FilePattern, Long> {
    Optional<FilePattern> findByEntityId(Long entityId);
}

