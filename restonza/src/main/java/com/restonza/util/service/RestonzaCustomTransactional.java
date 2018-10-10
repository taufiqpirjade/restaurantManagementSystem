/**
 * 
 */
package com.restonza.util.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.transaction.annotation.Transactional;


/**
 * @author Flex-Grow developers
 * Custom annotation
 * Used for: Maintaining ACID property for DB
 * Usage: If we are dealing with multiple insert/update in a single flow
 * then we should add this annotation to method
 * Working: It roll backs parent transaction if child transaction fails
 */
@Target({ElementType.METHOD , ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Transactional(rollbackFor = Exception.class)
public @interface RestonzaCustomTransactional {  
}

