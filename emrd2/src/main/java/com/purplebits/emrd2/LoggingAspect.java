package com.purplebits.emrd2;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
public class LoggingAspect {

	/**
	 * @author prayas
	 * @param joinPoint
	 * @return
	 * @throws Throwable
	 */
	private final String className = LoggingAspect.class.getSimpleName();
	private static final Logger logger = LogManager.getLogger(LoggingAspect.class);

	@Around("execution(* com.purpledocs.dao.*.*(..))")
	public Object logDaoMethod(ProceedingJoinPoint joinPoint) throws Throwable {
		logger.info(className + "log Dao  is invoked())");
		final org.apache.logging.log4j.Logger logger = LogManager.getLogger(joinPoint.getTarget().getClass().getName());
		Object retVal = null;

		try {
			StringBuffer messageStringBuffer = new StringBuffer();
			messageStringBuffer.append(joinPoint.getSignature().getName());
			messageStringBuffer.append("(");

			Object[] args = joinPoint.getArgs();
			for (int i = 0; i < args.length; i++) {
				messageStringBuffer.append(args[i]).append(",");
			}
			if (args.length > 0) {
				messageStringBuffer.deleteCharAt(messageStringBuffer.length() - 1);
			}
			messageStringBuffer.append(")");

			StopWatch stopWatch = new StopWatch();
			stopWatch.start();

			retVal = joinPoint.proceed();

			stopWatch.stop();
			messageStringBuffer.append(" execution time: ");
			messageStringBuffer.append(stopWatch.getTotalTimeMillis());
			messageStringBuffer.append(" ms;");

//			logger.info(messageStringBuffer.toString());
		} catch (Throwable ex) {
			StringBuffer errorMessageStringBuffer = new StringBuffer();
			logger.error(className + "logDaoMethod()" + ex);
			throw ex;
		}

		return retVal;
	}

	Logger log = LogManager.getLogger(this.getClass());

	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	public void controller() {
	}

	/*
	 * @Pointcut("execution(* *.*(..))") protected void allMethod() { }
	 * 
	 * @Pointcut("execution(public * *(..))") protected void
	 * loggingPublicOperation() { }
	 * 
	 * @Pointcut("execution(* *.*(..))") protected void loggingAllOperation() { }
	 */

	/*
	 * @Pointcut("within(org.learn.log..*)") private void
	 * logAnyFunctionWithinResource() { }
	 */

	// before -> Any resource annotated with @Controller annotation
	// and all method and function taking HttpServletRequest as first parameter
	// @Before("controller() && allMethod() && args(..,request)")
	@Before("controller()")
	public void logBefore(JoinPoint joinPoint) {

		/*
		 * log.debug("Entering in Method :  " + joinPoint.getSignature().getName());
		 * log.debug("Class Name :  " +
		 * joinPoint.getSignature().getDeclaringTypeName()); log.debug("Arguments :  " +
		 * Arrays.deepToString(joinPoint.getArgs())); log.debug("Target class : " +
		 * joinPoint.getTarget().getClass().getName());
		 */

//		log.debug("Entering in Method :  " + joinPoint.getSignature().getName() + "\n" + "Class Name :  "
//				+ joinPoint.getSignature().getDeclaringTypeName() + "\n" + "Arguments :  "
//				+ Arrays.deepToString(joinPoint.getArgs()));
//		log.info("Entering in Method :  " + joinPoint.getSignature().getName() + "\n" + "Class Name :  "
//				+ joinPoint.getSignature().getDeclaringTypeName() + "\n" + "Arguments :  "
//				+ Arrays.deepToString(joinPoint.getArgs()));
		/*
		 * if (null != request) { log.debug("Start Header Section of request ");
		 * log.debug("Method Type : " + request.getMethod()); Enumeration<String>
		 * headerNames = request.getHeaderNames(); while (headerNames.hasMoreElements())
		 * { String headerName = headerNames.nextElement(); String headerValue =
		 * request.getHeader(headerName); log.debug("Header Name: " + headerName +
		 * " Header Value : " + headerValue); } log.debug("Request Path info :" +
		 * request.getServletPath()); log.debug("End Header Section of request "); }
		 */
	}

	// After -> All method within resource annotated with @Controller annotation
	// and return a value
	@AfterReturning(pointcut = "controller()", returning = "result")
	public void logAfter(JoinPoint joinPoint, Object result) {
		String returnValue = this.getValue(result);
//		log.debug(className+"Method Return value : " + returnValue);
	}

	// After -> Any method within resource annotated with @Controller annotation
	// throws an exception ...Log it
	@AfterThrowing(pointcut = "controller()", throwing = "exception")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
//		log.error(className+"An exception has been thrown in " + joinPoint.getSignature().getName() + " ()", exception.getMessage());
		log.error("Cause : " + exception.getCause(), exception.getMessage());
	}

	// Around -> Any method within resource annotated with @Controller
	// annotation
	@Around("controller()")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

		long start = System.currentTimeMillis();
		try {
			String className = joinPoint.getSignature().getDeclaringTypeName();
			String methodName = joinPoint.getSignature().getName();
			Object result = joinPoint.proceed();
			long elapsedTime = System.currentTimeMillis() - start;
//			log.debug("Method " + className + "." + methodName + " ()" + " execution time : " + elapsedTime + " ms");

			return result;
		} catch (IllegalArgumentException e) {
			log.error(className + "logAround() " + e);
			throw e;
		}
	}

	private String getValue(Object result) {
		String returnValue = null;
		if (null != result) {
			if (result.toString().endsWith("@" + Integer.toHexString(result.hashCode()))) {
				returnValue = ReflectionToStringBuilder.toString(result);
			} else {
				returnValue = result.toString();
			}
		}

		return returnValue;
	}
}