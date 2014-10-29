package com.anosym.nyundo.element;

import static com.anosym.nyundo.element.UIUtil.attachSpacing;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.anosym.nyundo.UIStringable;
import com.anosym.nyundo.component.UIComponent;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;
import javax.annotation.Nonnull;

/**
 * This is a kind of transformation from {@link UIComponent} to an xml ui element, but not necessarily html.
 *
 * A renderer main goal is to convert {@link UIComponent} to {@link UIElement}.
 *
 * @author mochieng
 */
public class UIElement extends UIStringable implements Comparable<UIElement>, UIRepresentable {

    //Some constants
    private static final int SPACING_MARGIN = 4;

    private static final int CONSTANT_HASH_INC = 10;
    /**
     * This unique id is used to track the relative positioning of this element to its parent.
     */
    private int hashId;
    /**
     * The current element namespace.
     */
    private UINamespace targetNamespace;
    /**
     * The declared namespaces on this element.
     */
    private final Set<UINamespace> declaredNamespaces;
    /**
     * Children element of this element, in the order in which they were added.
     */
    private final List<UIElement> children;
    /**
     * The parent of this element. May be null.
     */
    private UIElement parentElement;
    /**
     * The attributes of this element. Never empty.
     */
    private final SortedSet<UIAttribute> attributes;
    /**
     * The name of the element. (its tag markup).
     */
    private final String name;

    public UIElement(@Nonnull final String name, final UIElement parentElement) {
        checkArgument(!Strings.isNullOrEmpty(name), "The UIElement markup name must not be null or empty");

        this.name = name;
        this.declaredNamespaces = new ConcurrentSkipListSet<>();
        this.hashId = 0;
        this.children = new ArrayList<>();
        this.parentElement = null;
        this.attributes = new ConcurrentSkipListSet<>();
        this.parentElement = parentElement;
        final UIElement thisChild = this;
        if (parentElement != null) {
            parentElement.addChild(thisChild);
        }
    }

    public final String getName() {
        return name;
    }

    public Set<UINamespace> getDeclaredNamespaces() {
        return Collections.unmodifiableSet(declaredNamespaces);
    }

    public void declareNamespace(@Nonnull final UINamespace namespace) {
        checkNotNull(namespace, "Namespace must not be null");
        checkArgument(Objects.equal(this.targetNamespace, namespace), "Specified namespace is the targetnamespace for this element");
        checkState(this.declaredNamespaces.add(namespace), "Specified namespace is already declared on this element");
    }

    public UINamespace getTargetNamespace() {
        return targetNamespace;
    }

    public void setTargetNamespace(@Nonnull final UINamespace targetNamespace) {
        checkNotNull(targetNamespace, "targetNamespace must not be null");
        //remove it from declared namespace if it exists.
        this.declaredNamespaces.remove(this.targetNamespace);
        declareNamespace(targetNamespace);

        this.targetNamespace = targetNamespace;
    }

    public int getChildrenCount() {
        return 0;
    }

    private synchronized int getNextChildHash() {
        return (this.hashId + CONSTANT_HASH_INC * children.size());
    }

    private synchronized void addChild(final UIElement child) {
        child.hashId = getNextChildHash();
        child.updateChildrenHashId();
        //then add to the set.
        children.add(child);
    }

    private void updateChildrenHashId() {
        for (UIElement child : children) {
            child.hashId = getNextChildHash();
            //Because this is constant increment, we do not need to reinitialize the list.
            child.updateChildrenHashId();
        }
    }

    public UIElement getParentElement() {
        return parentElement;
    }

    /**
     * Returns an unmodifiable view for the children of this set.
     *
     * @return
     */
    @Nonnull
    public Iterable<UIElement> getChildren() {
        return Collections.unmodifiableList(children);
    }

    /**
     * Returns an unmodifiable view of this elements attributes.
     *
     * @return
     */
    @Nonnull
    public Iterable<UIAttribute> getAttributes() {
        return Collections.unmodifiableSet(attributes);
    }

    public final void addAttribute(@Nonnull final UIAttribute attribute) {
        checkState(this.attributes.add(checkNotNull(attribute, "Attribute must not be null")),
                "Duplicate attribute definition: ",
                attribute.getName());
    }

    public final void addAttribute(@Nonnull final String key, @Nonnull final Object value) {
        checkArgument(!Strings.isNullOrEmpty(key), "Attribute key must not be null");
        checkNotNull(value, "Attribute value must not be null");

        addAttribute(new UIAttribute(key, String.valueOf(value)));
    }

    public boolean isElementQualified() {
        return targetNamespace != null && targetNamespace.isElementQualified();
    }

    private String getXmlns() {
        String xmlns = null;
        if (targetNamespace != null) {
            xmlns = targetNamespace.getXmlns();
        } else if (parentElement != null) {
            xmlns = parentElement.getXmlns();
        }
        return (xmlns == null || xmlns.trim().isEmpty()) ? null : xmlns;
    }

    /**
     * Returns the xml string for this element.
     *
     * This could be xhtml, or any well-formed xml document.
     *
     * @return
     */
    @Override
    public synchronized Appendable renderXml(final int spacing) {
        final StringBuilder xml = new StringBuilder();
        toXmlString(this, xml, spacing);
        return xml;
    }

    public synchronized Appendable renderXml() {
        return renderXml(0);
    }

    private static void toXmlString(@Nonnull final UIElement elem, @Nonnull final StringBuilder xml, final int spacing) {
        checkNotNull(elem, "The UIElement must not be null");
        checkNotNull(xml, "The appendable must not be null");

        //Attach spacing
        attachSpacing(xml, spacing);
        //Start xml
        xml.append("<");
        final StringBuilder markupTag;
        final String xmlns = elem.getXmlns();
        if (xmlns == null) {
            markupTag = new StringBuilder(elem.name);
        } else {
            markupTag = new StringBuilder(xmlns).append(":").append(elem.name);
        }
        xml.append(markupTag); //Open

        if (elem.targetNamespace != null) {
            // Append the namespace target.
            attachSpacing(xml, 2).append("targetNamespace=").append(elem.targetNamespace.getUri());
        }
        for (UINamespace namespace : elem.declaredNamespaces) {
            xml.append(namespace.renderXml(2));
        }
        for (UIAttribute attr : elem.attributes) {
            xml.append(attr.renderXml(2));
        }
        final boolean empty = elem.children.isEmpty();
        if (empty) {
            xml.append("/>"); //Close
        } else {
            xml.append(">");
        }
        xml.append("\n"); //Whether the xml content is empty or not, we require newline.
        // Render children.
        for (UIElement e : elem.children) {
            xml.append(e.renderXml(spacing + SPACING_MARGIN));
        }
        if (!empty) {
            xml.append("\n");
            attachSpacing(xml, spacing);
            xml.append("</").append(markupTag).append(">").append("\n"); // Close and start new line
        }
    }

    /**
     * Comparison based on the uielement's hashid.
     *
     * For Elements of the same parent, this is consistent with {@link #equals(java.lang.Object) }
     *
     * @param o
     *
     * @return
     */
    @Override
    public int compareTo(UIElement o) {
        return Integer.valueOf(hashId).compareTo(o.hashId);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + this.hashId;
        hash = 23 * hash + Objects.hashCode(this.name);
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
        final UIElement other = (UIElement) obj;
        if (this.hashId != other.hashId) {
            return false;
        }
        return Objects.equal(this.name, other.name);
    }

}
