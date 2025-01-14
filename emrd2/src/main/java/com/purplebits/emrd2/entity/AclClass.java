package com.purplebits.emrd2.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.purplebits.emrd2.util.JsonUtils;

@Entity
@Table(name = "acl_class")
public class AclClass {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "class", nullable = false, unique = true)
	private String className; // Fully Qualified Class Name

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public String toString() {
		return JsonUtils.createGson().toJson(this);
	}

}
