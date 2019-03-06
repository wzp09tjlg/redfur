package com.wuzp.commonlib.Reflection;

import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 反射相关的工具类
 * <p>
 * Field相关
 *
 * @author wuzhenpeng03
 */
public class FieldUtils {

    private static final Map<String, Field> sFieldCache = new HashMap();

    private FieldUtils() {
    }

    private static String getKey(Class<?> cls, String fieldName) {
        StringBuilder sb = new StringBuilder();
        sb.append(cls.toString()).append("@").append(cls.getClassLoader()).append("#").append(fieldName);
        return sb.toString();
    }

    private static Field getField(Class<?> cls, String fieldName, boolean forceAccess) {
        assertTrue(cls != null, "The class must not be null", new Object[0]);
        assertTrue(!TextUtils.isEmpty(fieldName), "The field name must not be blank/empty", new Object[0]);
        String key = getKey(cls, fieldName);
        Map var5 = sFieldCache;
        Field cachedField;
        synchronized (sFieldCache) {
            cachedField = (Field) sFieldCache.get(key);
        }

        if (cachedField != null) {
            if (forceAccess && !cachedField.isAccessible()) {
                cachedField.setAccessible(true);
            }

            return cachedField;
        } else {
            for (Class acls = cls; acls != null; acls = acls.getSuperclass()) {
                try {
                    Field field = acls.getDeclaredField(fieldName);
                    if (!Modifier.isPublic(field.getModifiers())) {
                        if (!forceAccess) {
                            continue;
                        }

                        field.setAccessible(true);
                    }

                    Map var7 = sFieldCache;
                    synchronized (sFieldCache) {
                        sFieldCache.put(key, field);
                    }

                    return field;
                } catch (NoSuchFieldException var14) {
                    ;
                }
            }

            Field match = null;
            Iterator var17 = ReflectUtils.getAllInterfaces(cls).iterator();

            while (var17.hasNext()) {
                Class class1 = (Class) var17.next();

                try {
                    Field test = class1.getField(fieldName);
                    assertTrue(match == null,
                        "Reference to field %s is ambiguous relative to %s; a matching field exists on two or more implemented interfaces.",
                        new Object[]{fieldName, cls});
                    match = test;
                } catch (NoSuchFieldException var11) {
                    ;
                }
            }

            Map var18 = sFieldCache;
            synchronized (sFieldCache) {
                sFieldCache.put(key, match);
                return match;
            }
        }
    }

    public static Object readField(Field field, Object target, boolean forceAccess) throws IllegalAccessException {
        assertTrue(field != null, "The field must not be null", new Object[0]);
        if (forceAccess && !field.isAccessible()) {
            field.setAccessible(true);
        } else {
            MemberUtils.setAccessibleWorkaround(field);
        }

        return field.get(target);
    }

    public static void writeField(Field field, Object target, Object value, boolean forceAccess)
        throws IllegalAccessException {
        assertTrue(field != null, "The field must not be null", new Object[0]);
        if (forceAccess && !field.isAccessible()) {
            field.setAccessible(true);
        } else {
            MemberUtils.setAccessibleWorkaround(field);
        }

        field.set(target, value);
    }

    public static Object readField(Field field, Object target) throws IllegalAccessException {
        return readField(field, target, true);
    }

    public static Field getField(Class<?> cls, String fieldName) {
        return getField(cls, fieldName, true);
    }

    public static Object readField(Object target, String fieldName) throws IllegalAccessException {
        assertTrue(target != null, "target object must not be null", new Object[0]);
        Class<?> cls = target.getClass();
        Field field = getField(cls, fieldName, true);
        assertTrue(field != null, "Cannot locate field %s on %s", new Object[]{fieldName, cls});
        return readField(field, target, false);
    }

    public static Object readField(Object target, String fieldName, boolean forceAccess) throws IllegalAccessException {
        assertTrue(target != null, "target object must not be null", new Object[0]);
        Class<?> cls = target.getClass();
        Field field = getField(cls, fieldName, forceAccess);
        assertTrue(field != null, "Cannot locate field %s on %s", new Object[]{fieldName, cls});
        return readField(field, target, forceAccess);
    }

    public static void writeField(Object target, String fieldName, Object value) throws IllegalAccessException {
        writeField(target, fieldName, value, true);
    }

    public static void writeField(Object target, String fieldName, Object value, boolean forceAccess)
        throws IllegalAccessException {
        assertTrue(target != null, "target object must not be null", new Object[0]);
        Class<?> cls = target.getClass();
        Field field = getField(cls, fieldName, true);
        assertTrue(field != null, "Cannot locate declared field %s.%s", new Object[]{cls.getName(), fieldName});
        writeField(field, target, value, forceAccess);
    }

    public static void writeField(Field field, Object target, Object value) throws IllegalAccessException {
        writeField(field, target, value, true);
    }

    public static Object readStaticField(Field field, boolean forceAccess) throws IllegalAccessException {
        assertTrue(field != null, "The field must not be null", new Object[0]);
        assertTrue(Modifier.isStatic(field.getModifiers()), "The field '%s' is not static",
            new Object[]{field.getName()});
        return readField((Field) field, (Object) null, forceAccess);
    }

    public static Object readStaticField(Class<?> cls, String fieldName) throws IllegalAccessException {
        Field field = getField(cls, fieldName, true);
        assertTrue(field != null, "Cannot locate field '%s' on %s", new Object[]{fieldName, cls});
        return readStaticField(field, true);
    }

    public static void writeStaticField(Field field, Object value, boolean forceAccess) throws IllegalAccessException {
        assertTrue(field != null, "The field must not be null", new Object[0]);
        assertTrue(Modifier.isStatic(field.getModifiers()), "The field %s.%s is not static",
            new Object[]{field.getDeclaringClass().getName(), field.getName()});
        writeField((Field) field, (Object) null, value, forceAccess);
    }

    public static void writeStaticField(Class<?> cls, String fieldName, Object value) throws IllegalAccessException {
        Field field = getField(cls, fieldName, true);
        assertTrue(field != null, "Cannot locate field %s on %s", new Object[]{fieldName, cls});
        writeStaticField(field, value, true);
    }

    public static Field getDeclaredField(Class<?> cls, String fieldName, boolean forceAccess) {
        assertTrue(cls != null, "The class must not be null", new Object[0]);
        assertTrue(!TextUtils.isEmpty(fieldName), "The field name must not be blank/empty", new Object[0]);

        try {
            Field field = cls.getDeclaredField(fieldName);
            if (!MemberUtils.isAccessible(field)) {
                if (!forceAccess) {
                    return null;
                }

                field.setAccessible(true);
            }

            return field;
        } catch (NoSuchFieldException var4) {
            return null;
        }
    }

    public static void writeDeclaredField(Object target, String fieldName, Object value) throws IllegalAccessException {
        Class<?> cls = target.getClass();
        Field field = getDeclaredField(cls, fieldName, true);
        assertTrue(field != null, "Cannot locate declared field %s.%s", new Object[]{cls.getName(), fieldName});
        writeField(field, target, value, false);
    }

    static void assertTrue(boolean expression, String message, Object... values) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }
}