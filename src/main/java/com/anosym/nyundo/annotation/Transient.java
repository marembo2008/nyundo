package com.anosym.nyundo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.atteo.classindex.IndexAnnotated;

/**
 * Signifies to the context that the attribute is to be ignored for the current component.
 *
 * @see {@link Attribute} for more information.
 *
 * @author mochieng
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@IndexAnnotated
public @interface Transient {

}
