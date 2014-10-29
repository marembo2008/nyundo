package com.anosym.nyundo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.atteo.classindex.IndexAnnotated;

/**
 * Designates a component attribute that would be exposed in an xml view. If no attribute is annotated, then the context
 * will assume that every field attribute is an xml attribute, ignoring any final fields.
 *
 * Based on which property is annotated, the context will consider either field or method properties. If method
 * properties, these must follow the normal java bean convention.
 *
 * By default, the context assumes field property.
 *
 * @author mochieng
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@IndexAnnotated
public @interface Attribute {

    /**
     * The name of the attribute. If not specified, defaults to the simple name of the field/method property.
     *
     * @return
     */
    String name() default "";
}
