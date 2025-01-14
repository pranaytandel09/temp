/**
 * Description of the file.
 * 
 *  Deep Singh Tomar
 *  PurpleDocs
 * 12-Mar-2024 4:42:41 pm
 */
package com.purplebits.emrd2.search_criteria;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.purplebits.emrd2.util.GenericSearchOperators;


/**
 * Created By @Deep For Company - PurpleDocs
 */
public class AuditTrailSearchCriteria {
	private final String className = AuditTrailSearchCriteria.class.getSimpleName();
	private static final Logger logger = LogManager.getLogger(AuditTrailSearchCriteria.class);

	
	private String key;
	private GenericSearchOperators operation;
	private Object value;
	private boolean orPredicate;

	public AuditTrailSearchCriteria() {

	}

	public AuditTrailSearchCriteria(final String key, final GenericSearchOperators operation,
			final Object value) {
		super();
		this.key = key;
		this.operation = operation;
		this.value = value;
	}

	public AuditTrailSearchCriteria(final String orPredicate, final String key,
			final GenericSearchOperators operation, final Object value) {
		super();
		this.orPredicate = orPredicate != null
				&& orPredicate.equals(GenericSearchOperators.OR_PREDICATE_FLAG);
		this.key = key;
		this.operation = operation;
		this.value = value;
	}

	public AuditTrailSearchCriteria(String key, String operation, String prefix, String value, String suffix) {
		GenericSearchOperators op = GenericSearchOperators
				.getSimpleOperation(operation.charAt(0));
		if (op != null && op == GenericSearchOperators.EQUALITY) { // the operation may be complex operation
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
		
		this.key = key;
		this.operation = op;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(final String key) {
		this.key = key;
	}

	public GenericSearchOperators getOperation() {
		return operation;
	}

	public void setOperation(final GenericSearchOperators operation) {
		this.operation = operation;
	}

	public Object getValue() {
		return value;
	}

	 public void setValue(final Object value) {
	        if (value instanceof String && isNumeric((String) value)) {
	            this.value = Integer.parseInt((String) value);
	        } else {
	            this.value = value;
	        }
	    }
	 
	 
	// Utility method to check if a string is numeric
	    private boolean isNumeric(String str) {
	        if (str == null) {
	            return false;
	        }
	        try {
	            Integer.parseInt(str);
	            return true;
	        } catch (NumberFormatException e) {
	        	logger.error(className + " isNumeric() invoked for: "+e);
	            return false;
	        }
	    }

	public boolean isOrPredicate() {
		return orPredicate;
	}

	public void setOrPredicate(boolean orPredicate) {
		this.orPredicate = orPredicate;
	}

}
