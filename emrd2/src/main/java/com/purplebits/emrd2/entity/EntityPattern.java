package com.purplebits.emrd2.entity;

import javax.persistence.*;

@Entity
@Table(name = "table_entity_patterns")
public class EntityPattern {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer patternId;

    @Column(name = "entity_id", nullable = false)
    private Integer entityId;

    private String pattern;

    private String description;

    // Getters and Setters
    public Integer getPatternId() {
        return patternId;
    }

    public void setPatternId(Integer patternId) {
        this.patternId = patternId;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
