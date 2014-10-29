package com.anosym.nyundo.util;

import com.anosym.nyundo.UIStringable;
import java.util.Objects;
import javax.annotation.Nullable;

/**
 *
 * @author mochieng
 */
public final class UIPair<First, Second> extends UIStringable {

    private First first;
    private Second second;

    public UIPair() {
    }

    public UIPair(First first, Second second) {
        this.first = first;
        this.second = second;
    }

    public static <First, Second> UIPair<First, Second> of(@Nullable final First first, @Nullable final Second second) {
        return new UIPair<>(first, second);
    }

    public First getFirst() {
        return first;
    }

    public Second getSecond() {
        return second;
    }

    public void setFirst(First first) {
        this.first = first;
    }

    public void setSecond(Second second) {
        this.second = second;
    }

    public boolean isEmpty() {
        return first == null && second == null;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.first);
        hash = 29 * hash + Objects.hashCode(this.second);
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
        final UIPair<?, ?> other = (UIPair<?, ?>) obj;
        if (!Objects.equals(this.first, other.first)) {
            return false;
        }
        return Objects.equals(this.second, other.second);
    }

}
