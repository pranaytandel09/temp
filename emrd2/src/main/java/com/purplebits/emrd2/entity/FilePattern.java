package com.purplebits.emrd2.entity;

import javax.persistence.*;

@Entity
@Table(name = "file_patterns")
public class FilePattern {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patternId;

    @Column(nullable = false)
    private Long entityId;

    @Column(nullable = false)
    private String patternFormat;

    // Getters and Setters


    public Long getPatternId() {
        return patternId;
    }

    public void setPatternId(Long patternId) {
        this.patternId = patternId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getPatternFormat() {
        return patternFormat;
    }

    public void setPatternFormat(String patternFormat) {
        this.patternFormat = patternFormat;
    }
}
