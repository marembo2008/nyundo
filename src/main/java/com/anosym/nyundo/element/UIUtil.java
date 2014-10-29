package com.anosym.nyundo.element;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;

/**
 *
 * @author mochieng
 */
public final class UIUtil {

    private UIUtil() {
    }

    @Nonnull
    public static StringBuilder attachSpacing(@Nonnull final StringBuilder xmlAppendable, final int spacingCount) {
        checkNotNull(xmlAppendable, "The xml appendable must not be null");

        for (int space = 0; space < spacingCount; space++) {
            xmlAppendable.append(" ");
        }
        return xmlAppendable;
    }
}
