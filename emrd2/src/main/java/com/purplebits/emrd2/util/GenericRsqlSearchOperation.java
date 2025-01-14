/**
 * Description of the file.
 * 
 *  Deep Singh Tomar
 *  PurpleDocs
 * 12-Mar-2024 4:30:39 pm
 */
package com.purplebits.emrd2.util;

/**Created By @Deep For Company - PurpleDocs
 * 
 */

import java.util.Arrays;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.RSQLOperators;


public enum GenericRsqlSearchOperation {

	EQUAL(RSQLOperators.EQUAL), NOT_EQUAL(RSQLOperators.NOT_EQUAL), GREATER_THAN(RSQLOperators.GREATER_THAN),
	GREATER_THAN_OR_EQUAL(RSQLOperators.GREATER_THAN_OR_EQUAL), LESS_THAN(RSQLOperators.LESS_THAN),
	LESS_THAN_OR_EQUAL(RSQLOperators.LESS_THAN_OR_EQUAL), IN(RSQLOperators.IN), NOT_IN(RSQLOperators.NOT_IN);

	private ComparisonOperator operator;

	GenericRsqlSearchOperation(final ComparisonOperator operator) {
		this.operator = operator;
	}

	public static GenericRsqlSearchOperation getSimpleOperator(final ComparisonOperator operator) {
		return Arrays.stream(values()).filter(operation -> operation.getOperator() == operator).findAny().orElse(null);
	}

	public ComparisonOperator getOperator() {
		return operator;
	}
	
	
}
