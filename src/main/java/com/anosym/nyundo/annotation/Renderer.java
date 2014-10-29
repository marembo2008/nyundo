package com.anosym.nyundo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Qualifier;
import org.atteo.classindex.IndexAnnotated;

/**
 * If a class is annotated with this annotation, then it will act as a renderer for components within the specified
 * namespace.
 *
 * All Renderers with this annotation must implement {@link com.anosym.nyundo.component.renderer.Renderer}
 *
 * @author mochieng
 */
@Qualifier
@ApplicationScoped
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@IndexAnnotated
public @interface Renderer {

    /**
     * The namespace for which this Renderer is responsible. Null or empty value will lead to
     * {@link IllegalArgumentException}
     *
     * @return
     */
    String namespace();

    /**
     * If a component name has not been specified, then the renderer will be called for any component within the current
     * context which does not have a specific renderer attached to it.
     *
     * @return
     */
    String component() default "";

    /**
     * The name of the renderer. If not specified, will be uniquely generated from the simple class name of the
     * renderer.
     *
     * @return
     */
    String name() default "";
}
