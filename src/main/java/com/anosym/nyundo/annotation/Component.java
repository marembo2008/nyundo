package com.anosym.nyundo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.atteo.classindex.IndexAnnotated;

/**
 * Designates a component. A component is fully specified by its namespace and name.
 *
 * @author mochieng
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@IndexAnnotated
@Inherited
public @interface Component {

    /**
     * The name of the component. unique within current context namespace.
     *
     * If not specified, defaults to the current simple name of the class.
     *
     * This is the xml tag for the component.
     *
     * @return
     */
    String name() default "";

    /**
     * The namespace for which the component belongs.
     *
     * @return
     */
    String namespace();
}
