package com.anosym.nyundo.util;

import com.anosym.nyundo.UIStringable;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 *
 * @author mochieng
 */
public final class UIRendererKey extends UIStringable {

    @Nonnull
    private final String namespace;
    private final String componentName;

    public UIRendererKey(String namespace, String componentName) {
        this.namespace = namespace;
        this.componentName = componentName;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getComponentName() {
        return componentName;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.namespace);
        hash = 59 * hash + Objects.hashCode(this.componentName);
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
        final UIRendererKey other = (UIRendererKey) obj;
        if (!Objects.equals(this.namespace, other.namespace)) {
            return false;
        }
        return Objects.equals(this.componentName, other.componentName);
    }

}
