package com.anosym.nyundo.component;

import static java.util.Objects.requireNonNull;

import com.anosym.nyundo.UIStringable;
import com.anosym.nyundo.annotation.Component;
import com.anosym.nyundo.util.UIComponentId;
import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author mochieng
 */
@Component(name = "component", namespace = "http://nyundo.anosym.com/ui")
public abstract class UIComponent extends UIStringable implements Iterable<UIComponent> {

    /**
     * Parent of this component may be null.
     */
    private UIComponent parent;
    /**
     * Children of this component. may be empty (but not null)
     */
    private final Map<UIComponentId, UIComponent> children;
    /**
     * The id for this component.
     */
    @NotNull
    private final UIComponentId componentId;

    protected UIComponent(@NotNull final String simpleId) {
        requireNonNull(simpleId, "UIComponent ID must not be null");

        this.children = new HashMap<>();
        this.componentId = new UIComponentId(simpleId);
    }

    protected UIComponent() {
        this(uniqueId());
    }

    @Nonnull
    public final Map<UIComponentId, UIComponent> getChildren() {
        return children;
    }

    @Nullable
    public UIComponent getParent() {
        return parent;
    }

    @Nonnull
    public UIComponentId getComponentId() {
        return componentId;
    }

    @Override
    public Iterator<UIComponent> iterator() {
        return getChildren().values().iterator();
    }

    public final void addChild(@Nonnull final UIComponent child) {
        Preconditions.checkNotNull(child, "child component must not be null");
        child.parent = this;
        getChildren().put(child.componentId, child);
    }

    private static String uniqueId() {
        Random r = new Random(System.currentTimeMillis());
        return String.valueOf(1000 + r.nextInt(1000)) + "!" + String.valueOf(r.nextInt(10000));
    }
}
