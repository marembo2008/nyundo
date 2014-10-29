package com.anosym.nyundo.element;

import static com.anosym.nyundo.element.UIUtil.attachSpacing;
import static com.google.common.base.Preconditions.checkArgument;

import static com.google.common.base.Preconditions.checkNotNull;

import com.anosym.nyundo.UIStringable;
import com.google.common.base.Strings;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

/**
 * Attributes are only compared based on their names and namespaces. This is because no single element can have more than one attribute
 * with same name.
 *
 * @author mochieng
 */
public final class UIAttribute extends UIStringable implements UIRepresentable, Comparable<UIAttribute> {

    private final String name;
    private final String value;
    private final UINamespace namespace;

    public UIAttribute(@Nonnull final String name, @Nonnull final String value, @Nullable UINamespace namespace) {
        checkArgument(!Strings.isNullOrEmpty(name), "The attribute name must be specified");
        checkArgument(!Strings.isNullOrEmpty(value), "the attribute value must be specified");

        this.name = name;
        this.value = value;
        this.namespace = namespace;
    }

    public UIAttribute(@NotNull String name, @NotNull String value) {
        this(name, value, null);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public int compareTo(@Nonnull final UIAttribute o) {
        checkNotNull(o, "Cannot compare attribute to null object");

        return this.name.compareToIgnoreCase(o.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UIAttribute other = (UIAttribute) obj;
        return Objects.equals(this.name, other.name);
    }

    @Override
    public Appendable renderXml(final int leadingSpacing) {
        final String xmlnPrefix = namespace != null && namespace.isAttributeQualified() ? namespace.getXmlns() + ":" : "";
        return attachSpacing(new StringBuilder(), leadingSpacing)
                .append(xmlnPrefix).append(name).append("=\"").append(value).append("\"");
    }

}
