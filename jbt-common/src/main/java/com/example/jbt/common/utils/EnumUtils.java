package com.example.jbt.common.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumSet;

public class EnumUtils {

	private EnumUtils() {}

	@SuppressWarnings("unchecked")
    public static <E extends Enum<E>> E valueOf(int ordinal, Class<E> enumClass) {
		EnumSet<E> es = EnumSet.allOf(enumClass);
		
		try {
			//检查是否有getValue方法，有则以此方法比较值并返回
			Method method = enumClass.getDeclaredMethod("getCode");
			for(Enum<E> enumObj: es) {
				String result = (String) method.invoke(enumObj);
				if(result.equals(String.valueOf(ordinal))) {
					return (E) enumObj;
				}
			}
		} catch (NoSuchMethodException | ClassCastException | SecurityException | IllegalAccessException | InvocationTargetException e) {
			//出错则进一步比较ordinal是否可以匹配
			for(Enum<E> enumObj: es) {
				if(enumObj.ordinal() == ordinal) {
					return (E) enumObj;
				}
			}
		}
		//getValue没有出错，但也没有对应值，返回第一个，因为第一个是NONE或者会强制保存一个值
		return es.iterator().next();
	}
	
	public static int getValue(Enum<?> enumObj, Class<?> enumClass) {
		try {
			//强制使用getValue返回
			Method method = enumClass.getDeclaredMethod("getValue");
			return (int)method.invoke(enumObj);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
		}
		//getValue没有返回，使用ordinal
		return enumObj.ordinal();
	}

	public static String getCode(Enum<?> enumObj, Class<?> enumClass) {
		try {
			//强制使用getValue返回
			Method method = enumClass.getDeclaredMethod("getCode");
			Object obj = method.invoke(enumObj);
			if(obj instanceof String) {
				return (String) obj;
			} else if(obj instanceof Integer) {
				return String.valueOf((int) obj);
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
		}
		//getValue没有返回，使用ordinal
		return String.valueOf(enumObj.ordinal());
	}
}