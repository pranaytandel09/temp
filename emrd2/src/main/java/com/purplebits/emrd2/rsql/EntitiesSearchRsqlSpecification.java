package com.purplebits.emrd2.rsql;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.purplebits.emrd2.util.GenericRsqlSearchOperation;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;

public class EntitiesSearchRsqlSpecification<T> implements Specification<T> {

	/**
	 * 
	 */
	 private static final long serialVersionUID = 1L;
	    private String property;
	    private ComparisonOperator operator;
	    private List<String> arguments;

	    public EntitiesSearchRsqlSpecification(final String property, final ComparisonOperator operator,
	                                            final List<String> arguments) {
	        super();
	        this.property = property;
	        this.operator = operator;
	        this.arguments = arguments;
	    }

	    @Override
	    public Predicate toPredicate(final Root<T> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {
	        final List<Object> args = castArguments(root);
	        final Object argument = args.get(0);
	        switch (GenericRsqlSearchOperation.getSimpleOperator(operator)) {

	            case EQUAL: {
	                if (argument instanceof String) {
	                    return builder.like(root.get(property), argument.toString().replace('*', '%'));
	                } else if (argument == null) {
	                    return builder.isNull(root.get(property));
	                } else if (root.get(property).getJavaType().isEnum()) {
	                    return builder.equal(root.get(property), argument);
	                } else {
	                    return builder.equal(root.get(property), argument);
	                }
	            }
	            case NOT_EQUAL: {
	                if (argument instanceof String) {
	                    return builder.notLike(root.<String>get(property), argument.toString().replace('*', '%'));
	                } else if (argument == null) {
	                    return builder.isNotNull(root.get(property));
	                } else if (root.get(property).getJavaType().isEnum()) {
	                    return builder.notEqual(root.get(property), argument);
	                } else {
	                    return builder.notEqual(root.get(property), argument);
	                }
	            }
	            case GREATER_THAN: {
	                return builder.greaterThan(root.<String>get(property), argument.toString());
	            }
	            case GREATER_THAN_OR_EQUAL: {
	                return builder.greaterThanOrEqualTo(root.<String>get(property), argument.toString());
	            }
	            case LESS_THAN: {
	                return builder.lessThan(root.<String>get(property), argument.toString());
	            }
	            case LESS_THAN_OR_EQUAL: {
	                return builder.lessThanOrEqualTo(root.<String>get(property), argument.toString());
	            }
	            case IN: {
	                return root.get(property).in(args);
	            }
	            case NOT_IN: {
	                return builder.not(root.get(property).in(args));
	            }
	        }

	        return null;
	    }

	    // === private

	    private List<Object> castArguments(final Root<T> root) {
	        final Class<? extends Object> type = root.get(property).getJavaType();

	        return arguments.stream().map(arg -> {
	            if (type.equals(Integer.class)) {
	                return Integer.parseInt(arg);
	            } else if (type.equals(Long.class)) {
	                return Long.parseLong(arg);
	            } else if (type.isEnum()) {
	                return Enum.valueOf((Class<Enum>) type, arg); // Convert String to Enum
	            } else {
	                return arg;
	            }
	        }).collect(Collectors.toList());
	    }
	}
