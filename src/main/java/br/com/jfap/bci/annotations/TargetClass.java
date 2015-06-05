package br.com.jfap.bci.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Classes Annotated with @TargetClass will be treated as injectors by the framework.
 * @author Andre Conde
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TargetClass {
	String canonicalName();
}
