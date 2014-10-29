package com.anosym.nyundo.renderer;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.anosym.nyundo.UIStringable;
import com.anosym.nyundo.component.UIComponent;
import com.anosym.nyundo.element.UIElement;
import com.anosym.nyundo.util.UIComponentId;
import com.anosym.nyundo.util.UIPair;
import com.anosym.nyundo.util.UIRendererKey;
import com.anosym.nyundo.util.UIRendererUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 *
 * @author mochieng
 */
@ApplicationScoped
public class UIRenderingManager {

    private static final ThreadLocal<UIRendererContext> CURRENT_RENDERER_CONTEXT = new ThreadLocal<>();
    /**
     * A list of all known renderers.
     */
    @Inject
    @Any
    private Instance<UIRenderer> renderers;
    /**
     * Faster lookup by renderer key.
     */
    private Map<UIRendererKey, UIRenderer> rendererCache;

    @PostConstruct
    void setRenderers() {
        ImmutableMap.Builder<UIRendererKey, UIRenderer> builder = ImmutableMap.builder();
        for (UIRenderer renderer : renderers) {
            builder.put(UIRendererUtil.getRendererKey(renderer), renderer);
        }
        rendererCache = builder.build();
    }

    @Nonnull
    public UIElement render(@Nonnull final UIComponent component) {
        checkNotNull(component, "The component to render must not be null");
        UIRendererContext currentContext = CURRENT_RENDERER_CONTEXT.get();

        if (currentContext == null) {
            //Create new context if none exists.
            currentContext = new UIRendererContextImpl(this);
            CURRENT_RENDERER_CONTEXT.set(currentContext);
        }
        return currentContext.render(component);
    }

    @Nonnull
    private UIRenderer getRenderer(@Nonnull final UIComponent component) {
        final UIPair<UIRendererKey, UIRendererKey> rendererKeys = UIRendererUtil.getRendererKey(component);
        if (rendererCache.containsKey(rendererKeys.getFirst())) {
            return rendererCache.get(rendererKeys.getFirst());
        }
        if (rendererCache.containsKey(rendererKeys.getSecond())) {
            return rendererCache.get(rendererKeys.getSecond());
        }
        throw new IllegalArgumentException("The following component (" + component + ") with UIRendererKey: (" + rendererKeys + ") does not have renderer defined");
    }

    private static final class UIRendererContextImpl extends UIStringable implements UIRendererContext {

        private final UIPair<UIComponent, UIElement> rootComponentElementPair;
        private final Stack<UIPair<UIComponent, UIElement>> currentComponentElementPairStack;
        private final UIRenderingManager renderingManager;

        public UIRendererContextImpl(final UIRenderingManager renderingManager) {
            this.renderingManager = renderingManager;
            this.rootComponentElementPair = new UIPair<>();
            this.currentComponentElementPairStack = new Stack<>();
        }

        @Override
        @Nullable
        public UIComponent getCurrentComponent() {
            if (!currentComponentElementPairStack.isEmpty()) {
                return currentComponentElementPairStack.peek().getFirst();
            }
            return null;
        }

        @Override
        @Nullable
        public UIElement getCurrentElement() {
            if (!currentComponentElementPairStack.isEmpty()) {
                return currentComponentElementPairStack.peek().getSecond();
            }
            return null;
        }

        @Override
        public UIElement render(@Nonnull final UIComponent component) {
            checkNotNull(component, "Component to render must not be null");
            final UIRenderer renderer = renderingManager.getRenderer(component);
            final UIPair<UIComponent, UIElement> current = new UIPair<>(component, renderer.createElement(component, getCurrentElement()));
            if (this.rootComponentElementPair.isEmpty()) {
                //set the current and the root to current.
                this.rootComponentElementPair.setFirst(component);
                this.rootComponentElementPair.setSecond(current.getSecond());
            }
            this.currentComponentElementPairStack.push(current);
            try {
                return renderer.render(component, this);
            } finally {
                onRenderComplete();
            }
        }

        private void onRenderComplete() {
            checkState(!this.currentComponentElementPairStack.isEmpty(), "There is no current component under render");
            this.currentComponentElementPairStack.pop();
        }

        @Override
        @Nullable
        public UIComponent getRootComponent() {
            return rootComponentElementPair.getFirst();
        }

        @Override
        @Nullable
        public UIElement getRootElement() {
            return rootComponentElementPair.getSecond();
        }

        @Override
        @Nullable
        public UIComponent findComponent(@Nonnull String canonicalId) {
            UIComponentId componentId = UIComponentId.fromCanonicalId(canonicalId);
            return findComponent(componentId);
        }

        @Nullable
        @Override
        public UIComponent findComponent(@Nonnull final UIComponentId componentId) {
            final UIComponent component;
            if (componentId.isAbsolute()) {
                component = getRootComponent();
            } else {
                component = getCurrentComponent();
            }
            if (component != null) {
                final Deque<UIComponentId> parentQueue = componentId.getAncestryStack();
                return searchChildComponent(ImmutableList.of(component), parentQueue);
            }
            return null;
        }

        @Nullable
        private UIComponent searchChildComponent(@Nonnull final List<UIComponent> components,
                @Nonnull final Deque<UIComponentId> componentIdParentStack) {
            checkNotNull(components, "Componentlists must not be null");
            checkArgument(!components.isEmpty(), "Componentlists must not be empty");
            checkNotNull(componentIdParentStack, "Component id to search must not be null");

            if (!componentIdParentStack.isEmpty()) {
                final UIComponentId possibleNext = componentIdParentStack.pop();
                for (UIComponent child : components) {
                    if (child.getComponentId().equals(possibleNext)) {
                        return searchChildComponent(ImmutableList.of(child), componentIdParentStack);
                    }
                }
                //if we ever reach here, we did not find anything
                return null;
            }
            return components.get(0);
        }

    }
}
