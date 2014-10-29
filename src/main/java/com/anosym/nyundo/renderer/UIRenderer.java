package com.anosym.nyundo.renderer;

import com.anosym.nyundo.component.UIComponent;
import com.anosym.nyundo.element.UIElement;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A UIRenderer. All renderers must implement this interface in one way or another.
 *
 * The main purpose of a renderer is like xml transformation. It transforms {@link UIComponent} to {@link UIElement}
 *
 * This approach prevents developers from writing html/xml markup from their renderer code.
 *
 * @author mochieng
 */
public interface UIRenderer {

    UIElement render(@Nonnull final UIComponent component, @Nonnull final UIRendererContext context);

    UIElement createElement(@Nonnull final UIComponent component, @Nullable final UIElement parentElement);
}
