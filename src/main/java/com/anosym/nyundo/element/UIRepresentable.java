package com.anosym.nyundo.element;

/**
 *
 * @author mochieng
 */
public interface UIRepresentable {

    /**
     * The leading spacing to add to the rendered xml if applicable.
     *
     * @param leadingSpacing
     *
     * @return
     */
    Appendable renderXml(final int leadingSpacing);
}
