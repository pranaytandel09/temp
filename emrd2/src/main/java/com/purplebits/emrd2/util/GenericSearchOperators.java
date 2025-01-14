/**
 * Description of the file.
 * 
 *  Deep Singh Tomar
 *  PurpleDocs
 * 12-Mar-2024 4:32:15 pm
 */
package com.purplebits.emrd2.util;

/**
 * Created By @Deep For Company - PurpleDocs
 */
public enum GenericSearchOperators {

	EQUALITY, NEGATION, GREATER_THAN, LESS_THAN, LIKE, STARTS_WITH, ENDS_WITH, CONTAINS;

	public static final String[] SIMPLE_OPERATION_SET = { ":", "!", ">", "<", "~" };

	public static final String OR_PREDICATE_FLAG = "'";

	public static final String ZERO_OR_MORE_REGEX = "*";

	public static final String OR_OPERATOR = "OR";

	public static final String AND_OPERATOR = "AND";

	public static final String LEFT_PARANTHESIS = "(";

	public static final String RIGHT_PARANTHESIS = ")";

	public static GenericSearchOperators getSimpleOperation(final char input) {
		switch (input) {
		case ':':
			return EQUALITY;
		case '!':
			return NEGATION;
		case '>':
			return GREATER_THAN;
		case '<':
			return LESS_THAN;
		case '~':
			return LIKE;
		default:
			return null;
		}
	}
}
