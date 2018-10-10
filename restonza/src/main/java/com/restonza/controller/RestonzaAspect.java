/**
 * 
 */
package com.restonza.controller;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author flex-grow developers
 *
 */
@Aspect
@Component
public class RestonzaAspect {
	private final static Logger logger = LoggerFactory.getLogger(RestonzaAspect.class);

	@AfterReturning(pointcut = "execution(public * *.execute*(..))", returning = "result")
	public void logAfterReturning(JoinPoint joinPoint, Object result) {

		logger.info("logAfterReturning() is running!");
		logger.info("hijacked arguments : "+ Arrays.toString(joinPoint.getArgs()));
		logger.info("hijacked : " + joinPoint.getSignature().getName());
		logger.info("Method returned value is : " + result.toString());
		logger.info("******");
	}
}
