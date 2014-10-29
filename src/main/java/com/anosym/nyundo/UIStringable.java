package com.anosym.nyundo;

import com.google.common.base.MoreObjects;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mochieng
 */
@XmlTransient
public abstract class UIStringable {

    @XmlTransient
    private transient final Iterable<Field> fields;

    protected UIStringable() {
        this.fields = getfields();
    }

    private void getFields(@Nonnull final List<Field> fields, @Nonnull Class<?> clazz) {
        if (clazz.isAssignableFrom(UIStringable.class)) {
            return;
        }
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        getFields(fields, clazz.getSuperclass());
    }

    private Iterable<Field> getfields() {
        List<Field> objFields = Lists.newArrayList();
        getFields(objFields, getClass());
        Collections.sort(objFields, new Comparator<Field>() {

            @Override
            public int compare(Field o1, Field o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return Collections2.filter(objFields, new Predicate<Field>() {

            @Override
            public boolean apply(Field input) {
                final int modifiers = input.getModifiers();
                return !Modifier.isStatic(modifiers);
            }
        });
    }

    @Override
    public final String toString() {
        MoreObjects.ToStringHelper toStringHelper = MoreObjects.toStringHelper(getClass());
        for (Field f : this.fields) {
            try {
                f.setAccessible(true);
                Class<?> type = f.getType();
                //special handling for calendars.
                Object value = f.get(this);
                if (value != null && Calendar.class.isAssignableFrom(type)) {
                    value = convertFrom((Calendar) value, "yyyy-MM-dd HH:mm:ss");
                }
                toStringHelper.add(f.getName(), value);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
        return toStringHelper.toString();
    }

    public String convertFrom(Calendar value, String format) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.format(value.getTime());
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
