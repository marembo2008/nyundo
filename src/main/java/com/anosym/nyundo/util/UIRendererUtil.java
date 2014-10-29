package com.anosym.nyundo.util;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.anosym.nyundo.annotation.Component;
import com.anosym.nyundo.annotation.Renderer;
import com.anosym.nyundo.component.UIComponent;
import com.anosym.nyundo.renderer.UIRenderer;
import com.google.common.base.Strings;
import javax.annotation.Nonnull;

/**
 *
 * @author mochieng
 */
public final class UIRendererUtil {

    private UIRendererUtil() {
    }

    public static UIRendererKey getRendererKey(@Nonnull final UIRenderer renderer) {
        checkNotNull(renderer, "The renderer must be specified");
        Renderer rendererId = renderer.getClass().getAnnotation(Renderer.class);
        checkArgument(rendererId != null, "The renderer: {0} must be annotated by @Renderer annotation", renderer);
        checkState(!Strings.isNullOrEmpty(rendererId.namespace()), "The renderer: {0} must specify its namespace ", renderer);

        return new UIRendererKey(rendererId.namespace(), rendererId.component());
    }

    /**
     * We return two renderer keys, the first key is the full component key, the second key only contains the namespace.
     *
     * The first key takes priority while looking for the component renderer. If no specific renderer is defined for the
     * specific key, we look up a renderer defined for the namespace.
     *
     * @param component
     *
     * @return
     */
    public static UIPair<UIRendererKey, UIRendererKey> getRendererKey(@Nonnull final UIComponent component) {
        checkNotNull(component, "The componet must be specified");
        final Component componentId = component.getClass().getAnnotation(Component.class);
        checkArgument(componentId != null, "The component: {0} must be annotated by @Component annotation", component);
        checkState(!Strings.isNullOrEmpty(componentId.namespace()), "The component: {0} must specify its namespace ", component);

        return new UIPair(new UIRendererKey(componentId.namespace(), componentId.name()), new UIRendererKey(componentId.namespace(), null));
    }
}
