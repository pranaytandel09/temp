package com.purplebits.emrd2.rsql;

import org.springframework.data.jpa.domain.Specification;

import cz.jirutka.rsql.parser.ast.AndNode;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.OrNode;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;

public class TaskManagerSearchRsqlVisitor<T> implements RSQLVisitor<Specification<T>, Void> {

	private TaskManagerSearchRsqlSpecBuilder<T> builder;

	public TaskManagerSearchRsqlVisitor() {
		builder = new TaskManagerSearchRsqlSpecBuilder<>();
	}

	@Override
	public Specification<T> visit(final AndNode node, final Void param) {
		
		return builder.createSpecification(node);
	}

	@Override
	public Specification<T> visit(final OrNode node, final Void param) {
		return builder.createSpecification(node);
	}

	@Override
	public Specification<T> visit(final ComparisonNode node, final Void params) {
		return builder.createSpecification(node);
	}
}