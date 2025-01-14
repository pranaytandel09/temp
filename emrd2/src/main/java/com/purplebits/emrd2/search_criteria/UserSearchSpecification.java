package com.purplebits.emrd2.search_criteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.purplebits.emrd2.entity.UserDetailsView;


public class UserSearchSpecification implements Specification<UserDetailsView> {

	/**
	* Created By @Deep For Company - PurpleDocs
	*/
	private static final long serialVersionUID = 1L;
	private AuditTrailSearchCriteria criteria;

	public UserSearchSpecification(final AuditTrailSearchCriteria criteria) {
		super();
		this.criteria = criteria;
	}

	public AuditTrailSearchCriteria getCriteria() {
		return criteria;
	}

	@Override
	public Predicate toPredicate(Root<UserDetailsView> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		switch (criteria.getOperation()) {
		case EQUALITY:
			return builder.equal(root.get(criteria.getKey()), criteria.getValue());
		case NEGATION:
			return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
		case GREATER_THAN:
			return builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
		case LESS_THAN:
			return builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
		case LIKE:
			return builder.like(root.get(criteria.getKey()), criteria.getValue().toString());
		case STARTS_WITH:
			return builder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
		case ENDS_WITH:
			return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
		case CONTAINS:
			return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
		default:
			return null;
		}
	}
}
