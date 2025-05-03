package com.finago.interview.task.helper;

import java.lang.reflect.Field;

public final class TestHelper {

    private TestHelper() {}

    /**
     * Injects a value into a private field of a given object using reflection.
     *
     * @param target    The target object whose field is to be modified.
     * @param fieldName The name of the private field.
     * @param value     The value to inject.
     * @throws ReflectiveOperationException if field not found or not accessible.
     */
    public static void injectMock(final Object target, final String fieldName, final Object value) throws ReflectiveOperationException {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
