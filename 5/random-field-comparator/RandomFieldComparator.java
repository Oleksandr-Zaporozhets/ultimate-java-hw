package com.bobocode;

import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A generic comparator that is comparing a random field of the given class. The field is either primitive or
 * {@link Comparable}. It is chosen during comparator instance creation and is used for all comparisons.
 * <p>
 * By default it compares only accessible fields, but this can be configured via a constructor property. If no field is
 * available to compare, the constructor throws {@link IllegalArgumentException}
 *
 * @param <T> the type of the objects that may be compared by this comparator
 */
public class RandomFieldComparator<T> implements Comparator<T> {

    private List<Field> comparableFields = new ArrayList<>();
    private Field comparableField;
    Class<T> targetType;
    boolean compareOnlyAccessibleFields;

    public RandomFieldComparator(Class<T> targetType) {
        this(targetType, true);
    }

    /**
     * A constructor that accepts a class and a property indicating which fields can be used for comparison. If property
     * value is true, then only public fields or fields with public getters can be used.
     *
     * @param targetType                  a type of objects that may be compared
     * @param compareOnlyAccessibleFields config property indicating if only publicly accessible fields can be used
     */
    public RandomFieldComparator(Class<T> targetType, boolean compareOnlyAccessibleFields) {
        this.targetType = targetType;
        this.compareOnlyAccessibleFields = compareOnlyAccessibleFields;
        init();
    }

    private void init() {
        Field[] fields;
        if (compareOnlyAccessibleFields) {
            fields = targetType.getFields();
        } else {
            fields = targetType.getDeclaredFields();
        }

        for (var field : fields) {
            Class<?> fieldType = field.getType();
            if (fieldType.isPrimitive() || Comparable.class.isAssignableFrom(fieldType)) {
                comparableFields.add(field);
            }
        }
        if (comparableFields.isEmpty()){
            throw new IllegalArgumentException("There is no field available to compare");
        } else {
            comparableField = comparableFields.get(ThreadLocalRandom.current().nextInt(comparableFields.size()));
        }
    }

    /**
     * Compares two objects of the class T by the value of the field that was randomly chosen. It allows null values
     * for the fields, and it treats null value grater than a non-null value (nulls last).
     */
    @SneakyThrows
    @Override
    public int compare(T o1, T o2) {
        if (!compareOnlyAccessibleFields){
            comparableField.setAccessible(true);
        }
        Comparable first = (Comparable) comparableField.get(o1);
        Comparable second = (Comparable) comparableField.get(o2);
        if (first == null && second == null) {
            return 0;
        }
        if (first == null) {
            return 1;
        }
        if (second == null) {
            return -1;
        }


        return first.compareTo(second);
    }

    /**
     * Returns a statement "Random field comparator of class '%s' is comparing '%s'" where the first param is the name
     * of the type T, and the second parameter is the comparing field name.
     *
     * @return a predefined statement
     */
    @Override
    public String toString() {
        return String.format("Random field comparator of class '%s' is comparing '%s'", targetType.getSimpleName(), comparableField.getName());
    }
}
