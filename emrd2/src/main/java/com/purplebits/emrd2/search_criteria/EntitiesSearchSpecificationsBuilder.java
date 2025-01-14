package com.purplebits.emrd2.search_criteria;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.purplebits.emrd2.entity.Entities;
import com.purplebits.emrd2.util.GenericSearchOperators;


public class EntitiesSearchSpecificationsBuilder {

	private final List<AuditTrailSearchCriteria> params;

	public EntitiesSearchSpecificationsBuilder() {
		params = new ArrayList<>();
	}

	// API

	public final EntitiesSearchSpecificationsBuilder with(final String key, final String operation,
			final Object value, final String prefix, final String suffix) {
		return with(null, key, operation, value, prefix, suffix);
	}

	public final EntitiesSearchSpecificationsBuilder with(final String orPredicate, final String key,
			final String operation, final Object value, final String prefix, final String suffix) {
		GenericSearchOperators op = GenericSearchOperators
				.getSimpleOperation(operation.charAt(0));
		if (op != null) {
			if (op == GenericSearchOperators.EQUALITY) { // the operation may be complex operation
				final boolean startWithAsterisk = prefix != null
						&& prefix.contains(GenericSearchOperators.ZERO_OR_MORE_REGEX);
				final boolean endWithAsterisk = suffix != null
						&& suffix.contains(GenericSearchOperators.ZERO_OR_MORE_REGEX);

				if (startWithAsterisk && endWithAsterisk) {
					op = GenericSearchOperators.CONTAINS;
				} else if (startWithAsterisk) {
					op = GenericSearchOperators.ENDS_WITH;
				} else if (endWithAsterisk) {
					op = GenericSearchOperators.STARTS_WITH;
				}
			}
			params.add(new AuditTrailSearchCriteria(orPredicate, key, op, value));
		}
		return this;
	}

	public Specification<Entities> build() {
		if (params.isEmpty())
			return null;

		Specification<Entities> result = new EntitiesSearchSpecification(params.get(0));

		for (int i = 1; i < params.size(); i++) {
			result = params.get(i).isOrPredicate()
					? Specification.where(result).or(new EntitiesSearchSpecification(params.get(i)))
					: Specification.where(result).and(new EntitiesSearchSpecification(params.get(i)));
		}

		return result;
	}

	public final EntitiesSearchSpecificationsBuilder with(
			EntitiesSearchSpecification spec) {
		params.add(spec.getCriteria());
		return this;
	}

	public final EntitiesSearchSpecificationsBuilder with(AuditTrailSearchCriteria criteria) {
		params.add(criteria);
		return this;
	}
}
