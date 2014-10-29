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
 * Two namespaces are equal iff their uris are equal.
 *
 * @author mochieng
 */
public final class UINamespace extends UIStringable implements UIRepresentable {

    public static enum FormDefault {

        QUALIFIED,
        UNQUALIFIED;
    }
    private final String xmlns;
    private final String uri;
    private final FormDefault elementFormDefault;
    private final FormDefault attributeFormDefault;

    public UINamespace(@Nullable final String xmlns, @Nonnull final String uri,
            @Nonnull final FormDefault elementFormDefault, @Nonnull final FormDefault attributeFormDefault) {
        checkNotNull(uri, "The namespace uri must be specified");
        checkNotNull(elementFormDefault, "The namespace element formdefault must be specified");
        checkNotNull(attributeFormDefault, "The namespace attribute formdefault must be specified");
        checkArgument(elementFormDefault == FormDefault.UNQUALIFIED || !Strings.isNullOrEmpty(xmlns),
                "Namespace defines element FormDefault.QUALIFIED, yet missing xmlns prefix");
        checkArgument(attributeFormDefault == FormDefault.UNQUALIFIED || !Strings.isNullOrEmpty(xmlns),
                "Namespace defines attribute FormDefault.QUALIFIED, yet missing xmlns prefix");

        this.xmlns = xmlns;
        this.uri = uri;
        this.elementFormDefault = elementFormDefault;
        this.attributeFormDefault = attributeFormDefault;
    }

    public UINamespace(@NotNull final String uri, final String xmlns) {
        this(xmlns, uri, FormDefault.UNQUALIFIED, FormDefault.UNQUALIFIED);
    }

    public String getXmlns() {
        return xmlns;
    }

    public String getUri() {
        return uri;
    }

    public boolean isElementQualified() {
        return elementFormDefault == FormDefault.QUALIFIED;
    }

    public boolean isAttributeQualified() {
        return attributeFormDefault == FormDefault.QUALIFIED;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.uri);
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
        final UINamespace other = (UINamespace) obj;
        return Objects.equals(this.uri, other.uri);
    }

    @Override
    public Appendable renderXml(final int leadingSpacing) {
        final StringBuilder namespace = attachSpacing(new StringBuilder(), leadingSpacing).append("xmlns");
        if (this.xmlns != null) {
            namespace.append(":").append(this.xmlns);
        }
        return namespace.append("=").append(uri);
    }

}
