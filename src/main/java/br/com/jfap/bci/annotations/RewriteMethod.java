package br.com.jfap.bci.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Methods annotated with @RewriteMethod will be executed when Application Context is loaded
 * @author Conde
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RewriteMethod {
	String methodName();
}
