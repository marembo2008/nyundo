package com.anosym.nyundo.util;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.anosym.nyundo.UIStringable;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The comparison here is based on ancestral hierarchy.
 *
 * @author mochieng
 */
public final class UIComponentId extends UIStringable {

    private static final Splitter PARENT_COMPONENT_ID_SPLITTER = Splitter.on(':').omitEmptyStrings().trimResults();
    private static final Joiner CANONICAL_COMPONENT_ID_JOINER = Joiner.on(":");
    private UIComponentId parentId;
    private final String simpleId;
    private boolean absolute;

    public UIComponentId(@Nullable final UIComponentId parentId, @Nonnull final String simpleId, final boolean absolute) {
        this(simpleId);
        if (parentId != null) {
            checkState((absolute && parentId.isAbsolute()) || (!absolute),
                    "UIComponentId parent must be absolute if id is absolute");
        }
        this.parentId = parentId;
        this.absolute = absolute;
    }

    public UIComponentId(@Nonnull final String simpleId) {
        checkArgument(!Strings.isNullOrEmpty(simpleId), "UIComponentId simpleId must not be null or empty.");
        this.simpleId = simpleId;
    }

    @Nonnull
    @SuppressWarnings("null") //It cannot be null
    public static UIComponentId fromCanonicalId(@Nonnull final String canonicalId) {
        checkArgument(!Strings.isNullOrEmpty(canonicalId), "CanonicalId must not be null nor empty");

        final boolean absolute = canonicalId.trim().startsWith(":");
        final Iterable<String> simpleIds = PARENT_COMPONENT_ID_SPLITTER.split(canonicalId);
        UIComponentId componentId = null;
        for (String simpleId : simpleIds) {
            componentId = new UIComponentId(componentId, simpleId, absolute);
        }
        return componentId;
    }

    /**
     * True if the componentid has a reference to the most outer component.
     *
     * @return
     */
    public boolean isAbsolute() {
        return absolute;
    }

    public String getSimpleId() {
        return simpleId;
    }

    public UIComponentId getParentId() {
        return parentId;
    }

    public void setParentId(@Nonnull final UIComponentId parentId) {
        checkNotNull(parentId, "The parent componentId must not be null");

        this.parentId = parentId;
        this.absolute = this.parentId.absolute;
    }

    public final String getCanonicalId() {
        if (this.parentId != null) {
            return CANONICAL_COMPONENT_ID_JOINER.join(this.parentId.getCanonicalId(), this.simpleId);
        }
        return this.simpleId;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.parentId);
        hash = 89 * hash + Objects.hashCode(this.simpleId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UIComponentId other = (UIComponentId) obj;
        if (!Objects.equals(this.parentId, other.parentId)) {
            return false;
        }
        return Objects.equals(this.simpleId, other.simpleId);
    }

    /**
     * Returns an iterable, whose first element is the root-parent of this id, and whose last element is this id.
     *
     * @return
     */
    public Deque<UIComponentId> getAncestryStack() {
        return queueFromRootParent(this, new ArrayDeque<UIComponentId>());
    }

    private static Deque<UIComponentId> queueFromRootParent(@Nonnull final UIComponentId componentId,
            @Nonnull final Deque<UIComponentId> queue) {
        checkNotNull(componentId, "UIComponentId must not be null");
        checkNotNull(queue, "Queue must not be null");
        queue.push(componentId);
        if (componentId.getParentId() != null) {
            queueFromRootParent(componentId.getParentId(), queue);
        }
        return queue;
    }

}
