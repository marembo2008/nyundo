package com.anosym.nyundo.element;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Extends UIElement as a convenience.
 *
 * @author mochieng
 */
public final class UIText extends UIElement {

    private final String content;

    public UIText(@Nonnull final String content, @Nonnull final UIElement parentElement) {
        super("text-element", checkNotNull(parentElement, "The UIText requires a parent element"));
        this.content = checkNotNull(content, "UIText content must not be null");
    }

    @Nonnull
    public String getContent() {
        return content;
    }

    @Override
    public Appendable renderXml(int spacing) {
        return new StringBuilder(Objects.toString(content, ""));
    }

}
