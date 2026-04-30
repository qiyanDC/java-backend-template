package com.example.jbt.common.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;

import org.springframework.util.ReflectionUtils;

public class ReflectUtils {

    public void setValue(Object model, String setName, String setValue) {

        // 获取实体类的所有属性，返回Field数组
        Field[] fields = model.getClass().getDeclaredFields();
        try {
            // 遍历所有属性
            for (Field field: fields) {
                // 获取属性的名字
                String name = field.getName();
                if (name.equals(setName)) {
                    // 将属性的首字符大写，方便构造get，set方法
                    name = name.substring(0, 1).toUpperCase() + name.substring(1);
                    // 获取属性的类型
                    String type = field.getGenericType().toString();
                    // 如果type是类类型，则前面包含"class "，后面跟类名
                    if (type.equals("class java.lang.String")) {
                        // 调用getter方法获取属性值
                        Method m = model.getClass().getMethod("get" + name);
                        String value = (String) m.invoke(model);
                        if (value == null) {
                            m = model.getClass().getMethod("set" + name, String.class);
                            m.invoke(model, setValue.toString());
                        }
                    }
                    if (type.equals("class java.lang.Integer")) {
                        Method m = model.getClass().getMethod("get" + name);
                        Integer value = (Integer) m.invoke(model);
                        if (value == null) {
                            m = model.getClass().getMethod("set" + name, Integer.class);
                            m.invoke(model, Integer.parseInt(setValue));
                        }
                    }
                    if (type.equals("class java.lang.Long")) {
                        Method m = model.getClass().getMethod("get" + name);
                        Long value = (Long) m.invoke(model);
                        if (value == null) {
                            m = model.getClass().getMethod("set" + name, Integer.class);
                            m.invoke(model, Long.parseLong(setValue));
                        }
                    }
                    if (type.equals("class java.lang.Boolean")) {
                        Method m = model.getClass().getMethod("get" + name);
                        Boolean value = (Boolean) m.invoke(model);
                        if (value == null) {
                            m = model.getClass().getMethod("set" + name, Boolean.class);
                            m.invoke(model, Boolean.parseBoolean(setValue));
                        }
                    }
                    if (type.equals("class java.util.Date")) {
                        Method m = model.getClass().getMethod("get" + name);
                        LocalDateTime value = (LocalDateTime) m.invoke(model);
                        if (value == null) {
                            m = model.getClass().getMethod("set" + name, LocalDateTime.class);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            m.invoke(model, sdf.parse(setValue));
                        }
                    }// 如果有需要,可以仿照上面继续进行扩充,再增加对其它类型的判断
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("设置实体类的所有属性出现异常", e);
        }
    }

    public HashMap<String, Object> getValue(Object model, boolean isAnnotation) {

        HashMap<String, Object> retMap = new HashMap<>();
        // 获取实体类的所有属性，返回Field数组
        Field[] fields = model.getClass().getDeclaredFields();
        try {
            // 遍历所有属性
            for (Field field: fields) {
                // 获取属性的名字
                String varName = field.getName();
                // 将属性的首字符大写，方便构造get，set方法
                String name = varName.substring(0, 1).toUpperCase() + varName.substring(1);
                // 获取属性的类型
                String type = field.getGenericType().toString();

                Method m = null;
                try {
                    m = model.getClass().getMethod("get" + name);
                } catch (NoSuchMethodException ex){
                    continue;
                }

                // 如果type是类类型，则前面包含"class "，后面跟类名
                if (type.equals("class java.lang.String") || type.equalsIgnoreCase("string")) {
                    // 调用getter方法获取属性值
                    String value = (String) m.invoke(model);
                    if (value == null) {
                        retMap.put(varName, "");
                    }else{
                        retMap.put(varName, value);
                    }
                }else if (type.equals("class java.lang.Integer") || type.equalsIgnoreCase("int")) {
                    Integer value = (Integer) m.invoke(model);
                    if (value == null) {
                        retMap.put(varName, "");
                    }else{
                        retMap.put(varName, String.valueOf(value));
                    }
                }else if (type.equals("class java.lang.Long") || type.equalsIgnoreCase("long")) {
                    Long value = (Long) m.invoke(model);
                    if (value == null) {
                        retMap.put(varName, "");
                    }else{
                        retMap.put(varName, String.valueOf(value));
                    }
                }else if (type.equals("class java.lang.Boolean") || type.equalsIgnoreCase("boolean")) {
                    Boolean value = (Boolean) m.invoke(model);
                    if (value == null) {
                        retMap.put(varName, "");
                    }else{
                        retMap.put(varName, String.valueOf(value));
                    }
                }else if (type.equals("class java.util.Date") || type.equalsIgnoreCase("date")) {
                    LocalDateTime value = (LocalDateTime) m.invoke(model);
                    if (value == null) {
                        retMap.put(varName, "");
                    }else{
                        retMap.put(varName, String.valueOf(DateUtils.format(value, "yyyy-MM-dd HH:mm:ss")));
                    }
                }
                // 如果有需要,可以仿照上面继续进行扩充,再增加对其它类型的判断
            }
        } catch (Exception e) {
            throw new RuntimeException("设置实体类的所有属性出现异常", e);
        }
        return retMap;
    }



    /**
     * 获取指定类的指定field,包括父类
     *
     * @param clazz 字段所属类型
     * @param name  字段名
     * @return
     */
    public static Field getField(Class<?> clazz, String name) {
        return getField(clazz, name, null);
    }

    /**
     * 获取指定类的指定field,包括父类
     *
     * @param clazz 字段所属类型
     * @param name  字段名
     * @param type  field类型
     * @return      Field对象
     */
    public static Field getField(Class<?> clazz, String name, Class<?> type) {
        Objects.requireNonNull(clazz, "clazz must not be null!");
        //嵌套对象
        if(name.contains(".")) {
            String nestedObject = name.substring(0, name.indexOf("."));
            Field nestedField = ReflectUtils.getField(clazz, nestedObject);
            if(null != nestedField) {
                String nestedProperty = name.substring(name.indexOf(".") + 1);
                return ReflectUtils.getField(nestedField.getType(), nestedProperty);
            }
        } else {
            while (clazz != Object.class && clazz != null) {
                for (Field field : clazz.getDeclaredFields()) {
                    if ((name == null || name.equals(field.getName())) &&
                            (type == null || type.equals(field.getType()))) {
                        return field;
                    }
                }
                clazz = clazz.getSuperclass();
            }
        }

        return null;
    }

	public static void makeAccessible(Constructor<?> ctor) {
        ReflectionUtils.makeAccessible(ctor);
	}

	public static void makeAccessible(Method method) {
        ReflectionUtils.makeAccessible(method);
	}

	public static void makeAccessible(Field field) {
        ReflectionUtils.makeAccessible(field);
	}
}