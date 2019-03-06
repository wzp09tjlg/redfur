package com.wuzp.commonlib.Reflection;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author wuzhenpeng03
 */

class MemberUtils {
    private static final int ACCESS_TEST = 7;
    private static final Class<?>[] ORDERED_PRIMITIVE_TYPES;
    private static final Map<Class<?>, Class<?>> primitiveWrapperMap;
    private static final Map<Class<?>, Class<?>> wrapperPrimitiveMap;

    MemberUtils() {
    }

    private static boolean isPackageAccess(int modifiers) {
        return (modifiers & 7) == 0;
    }

    static boolean isAccessible(Member m) {
        return m != null && Modifier.isPublic(m.getModifiers()) && !m.isSynthetic();
    }

    static boolean setAccessibleWorkaround(AccessibleObject o) {
        if(o != null && !o.isAccessible()) {
            Member m = (Member)o;
            if(!o.isAccessible() && Modifier.isPublic(m.getModifiers()) && isPackageAccess(m.getDeclaringClass().getModifiers())) {
                try {
                    o.setAccessible(true);
                    return true;
                } catch (SecurityException var3) {
                    ;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    static boolean isAssignable(Class<?> cls, Class<?> toClass) {
        return isAssignable(cls, toClass, true);
    }

    static boolean isAssignable(Class<?>[] classArray, Class<?>[] toClassArray, boolean autoboxing) {
        if(!ReflectUtils.isSameLength(classArray, toClassArray)) {
            return false;
        } else {
            if(classArray == null) {
                classArray = ReflectUtils.EMPTY_CLASS_ARRAY;
            }

            if(toClassArray == null) {
                toClassArray = ReflectUtils.EMPTY_CLASS_ARRAY;
            }

            for(int i = 0; i < classArray.length; ++i) {
                if(!isAssignable(classArray[i], toClassArray[i], autoboxing)) {
                    return false;
                }
            }

            return true;
        }
    }

    static boolean isAssignable(Class<?> cls, Class<?> toClass, boolean autoboxing) {
        if(toClass == null) {
            return false;
        } else if(cls == null) {
            return !toClass.isPrimitive();
        } else {
            if(autoboxing) {
                if(cls.isPrimitive() && !toClass.isPrimitive()) {
                    cls = primitiveToWrapper(cls);
                    if(cls == null) {
                        return false;
                    }
                }

                if(toClass.isPrimitive() && !cls.isPrimitive()) {
                    cls = wrapperToPrimitive(cls);
                    if(cls == null) {
                        return false;
                    }
                }
            }

            return cls.equals(toClass)?true:(cls.isPrimitive()?(!toClass.isPrimitive()?false:(Integer.TYPE.equals(cls)?Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass):(Long.TYPE.equals(cls)?Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass):(Boolean.TYPE.equals(cls)?false:(Double.TYPE.equals(cls)?false:(Float.TYPE.equals(cls)?Double.TYPE.equals(toClass):(Character.TYPE.equals(cls)?Integer.TYPE.equals(toClass) || Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass):(Short.TYPE.equals(cls)?Integer.TYPE.equals(toClass) || Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass):(!Byte.TYPE.equals(cls)?false:Short.TYPE.equals(toClass) || Integer.TYPE.equals(toClass) || Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass)))))))))):toClass.isAssignableFrom(cls));
        }
    }

    static Class<?> primitiveToWrapper(Class<?> cls) {
        Class<?> convertedClass = cls;
        if(cls != null && cls.isPrimitive()) {
            convertedClass = (Class)primitiveWrapperMap.get(cls);
        }

        return convertedClass;
    }

    static Class<?> wrapperToPrimitive(Class<?> cls) {
        return (Class)wrapperPrimitiveMap.get(cls);
    }

    static int compareParameterTypes(Class<?>[] left, Class<?>[] right, Class<?>[] actual) {
        float leftCost = getTotalTransformationCost(actual, left);
        float rightCost = getTotalTransformationCost(actual, right);
        return leftCost < rightCost?-1:(rightCost < leftCost?1:0);
    }

    private static float getTotalTransformationCost(Class<?>[] srcArgs, Class<?>[] destArgs) {
        float totalCost = 0.0F;

        for(int i = 0; i < srcArgs.length; ++i) {
            Class<?> srcClass = srcArgs[i];
            Class<?> destClass = destArgs[i];
            totalCost += getObjectTransformationCost(srcClass, destClass);
        }

        return totalCost;
    }

    private static float getObjectTransformationCost(Class<?> srcClass, Class<?> destClass) {
        if(destClass.isPrimitive()) {
            return getPrimitivePromotionCost(srcClass, destClass);
        } else {
            float cost;
            for(cost = 0.0F; srcClass != null && !destClass.equals(srcClass); srcClass = srcClass.getSuperclass()) {
                if(destClass.isInterface() && isAssignable(srcClass, destClass)) {
                    cost += 0.25F;
                    break;
                }

                ++cost;
            }

            if(srcClass == null) {
                ++cost;
            }

            return cost;
        }
    }

    private static float getPrimitivePromotionCost(Class<?> srcClass, Class<?> destClass) {
        float cost = 0.0F;
        Class<?> cls = srcClass;
        if(!srcClass.isPrimitive()) {
            cost += 0.1F;
            cls = wrapperToPrimitive(srcClass);
        }

        for(int i = 0; cls != destClass && i < ORDERED_PRIMITIVE_TYPES.length; ++i) {
            if(cls == ORDERED_PRIMITIVE_TYPES[i]) {
                cost += 0.1F;
                if(i < ORDERED_PRIMITIVE_TYPES.length - 1) {
                    cls = ORDERED_PRIMITIVE_TYPES[i + 1];
                }
            }
        }

        return cost;
    }

    static {
        ORDERED_PRIMITIVE_TYPES = new Class[]{Byte.TYPE, Short.TYPE, Character.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE};
        primitiveWrapperMap = new HashMap();
        primitiveWrapperMap.put(Boolean.TYPE, Boolean.class);
        primitiveWrapperMap.put(Byte.TYPE, Byte.class);
        primitiveWrapperMap.put(Character.TYPE, Character.class);
        primitiveWrapperMap.put(Short.TYPE, Short.class);
        primitiveWrapperMap.put(Integer.TYPE, Integer.class);
        primitiveWrapperMap.put(Long.TYPE, Long.class);
        primitiveWrapperMap.put(Double.TYPE, Double.class);
        primitiveWrapperMap.put(Float.TYPE, Float.class);
        primitiveWrapperMap.put(Void.TYPE, Void.TYPE);
        wrapperPrimitiveMap = new HashMap();
        Iterator var0 = primitiveWrapperMap.keySet().iterator();

        while(var0.hasNext()) {
            Class<?> primitiveClass = (Class)var0.next();
            Class<?> wrapperClass = (Class)primitiveWrapperMap.get(primitiveClass);
            if(!primitiveClass.equals(wrapperClass)) {
                wrapperPrimitiveMap.put(wrapperClass, primitiveClass);
            }
        }

    }
}
