package com.example.jbt.common.utils;

import java.util.Random;

import com.example.jbt.common.constants.CommonConstants;

/**
 * 需要自己定义一个String的工具类，用于隔离String操作的包依赖
 * CharSequence 接口被String,StringBuffer,StringBuilder实现了，因此可以通用
 * @author qiyan
 * @since 2024年04月28日
 * @version 1.0
 */
public class StringUtils {
	private StringUtils() {}
	
	public static final String EMPTY = "";
	private static final char COLUMN_SEPARATOR = '_';
	
	public static boolean contains(final CharSequence seq, final CharSequence searchSeq) {
		return org.apache.commons.lang3.StringUtils.contains(seq, searchSeq);
	}
	public static boolean containsAny(final CharSequence cs, final CharSequence... searchCharSequences) {
		return org.apache.commons.lang3.StringUtils.containsAny(cs, searchCharSequences);
	}
	
	public static boolean isBlank(CharSequence... css) {
		return org.apache.commons.lang3.StringUtils.isAnyBlank(css);
	}
	public static boolean isNoneBlank(final CharSequence... css) {
		return org.apache.commons.lang3.StringUtils.isNoneBlank(css);
	}
	
	public static String leftPad(final String text, final int size, String padStr) {
		return org.apache.commons.lang3.StringUtils.leftPad(text, size, padStr);
	}
	
	public static boolean hasText(final CharSequence str) {
		return org.springframework.util.StringUtils.hasText(str);
	}
	
	public static boolean endsWith(final CharSequence str, final CharSequence suffix) {
        return org.apache.commons.lang3.StringUtils.endsWithIgnoreCase(str, suffix);
    }
	public static boolean endsWithAny(final CharSequence cs, final CharSequence... searchCharSequences) {
		return org.apache.commons.lang3.StringUtils.endsWithAny(cs, searchCharSequences);
	}
	
	public static boolean startsWith(final CharSequence str, final CharSequence prefix) {
		return org.apache.commons.lang3.StringUtils.startsWithIgnoreCase(str, prefix);
	}
	public static boolean startsWithAny(final CharSequence cs, final CharSequence... searchCharSequences) {
		return org.apache.commons.lang3.StringUtils.startsWithAny(cs, searchCharSequences);
	}
	
	public static String[] split(final String text, final String separatorChars) {
		return org.apache.commons.lang3.StringUtils.split(text, separatorChars);
    }
	public static String join(final Object[] array, final String delimiter) {
		return org.apache.commons.lang3.StringUtils.join(array, delimiter);
	}
	public static String trim(final String text) {
        return text == null ? null : text.trim();
    }
	/**
	 * 即然要用到equwals，而不是字符串本身的equals，那么直接忽略大小写才是用它的本意
	 * @param str
	 * @param args
	 * @return
	 */
	public static boolean equals(final CharSequence str, CharSequence args) {
		return org.apache.commons.lang3.StringUtils.equalsAnyIgnoreCase(str, args);
	}
	public static boolean equalsAny(final CharSequence str, CharSequence... args) {
		return org.apache.commons.lang3.StringUtils.equalsAny(str, args);
	}
	//效果就是重复replace，或replaceAll，这样是对的，不需要单独的replace
	public static String replace(final String text, final String searchString, final String replacement) {
		return org.apache.commons.lang3.StringUtils.replace(text, searchString, replacement);
	}
	public static String replaceIgnoreCase(final String text, final String searchString, final String replacement) {
		return org.apache.commons.lang3.StringUtils.replaceIgnoreCase(text, searchString, replacement);
	}
	public static String replaceEach(final String text, final String[] searchList, final String[] replacementList) {
		return org.apache.commons.lang3.StringUtils.replaceEach(text, searchList, replacementList);
	}
	
	public static String substringBeforeLast(final String text, final String separator) {
		return org.apache.commons.lang3.StringUtils.substringBeforeLast(text, separator);
	}
	
	public static String zeros(int n) {
        return repeat('0', n);
    }

    public static String repeat(char value, int n) {
        return new String(new char[n]).replace("\0", String.valueOf(value));
    }

	public static String valueOf(Object obj) {
	   return obj == null ? "" : obj.toString();
	}

	public static String valueOfOrZero(Object obj) {
	   return obj == null ? CommonConstants.VALUE_0 : obj.toString();
	}
    
	/**
	 * 驼峰命名法工具
	 * @return camelCase("hello_world") == "helloWorld"
	 */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == COLUMN_SEPARATOR) {
                upperCase = i != 1; // 不允许第二个字符是大写
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    
    /**
	 * 驼峰命名法工具
	 * @return capCamelCase("hello_world") == "HelloWorld"
	 */
    public static String toCapitalizeCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
    
    /**
	 * 驼峰命名法工具
	 * @return uncamelCase("helloWorld") = "hello_world"
	 */
    public static String toUnderlineName(String s) {
        if (s == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            boolean nextUpperCase = true;
            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }
            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append(COLUMN_SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }

	/**
	 * 获取指定长度随机数字
	 * */
	public static String getRandomNumberStr(int num){
		StringBuffer sb=new StringBuffer();
		for(int j=1;j<=num;j++){
			sb.append(getNum());
		}
		return sb.toString();
	}

	/**
	 * 随机获取一个0-9的数字
	 * @return
	 */
	public static int getNum(){
		return getRadomInt(0, 9);
	}

	/**
	 * 获取一个范围内的随机数字
	 * @return
	 */
	public static int getRadomInt(int min,int max){
		return new Random().nextInt(max-min+1)+min;
	}

	public static String checkDelimiter(String delimiter) {
		if(StringUtils.isBlank(delimiter)) {
			return delimiter;
		}

		String[] delimiterArr = {"\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "{", "}", "|"};
		for(String d : delimiterArr) {
			if(delimiter.contains(d)) {
				return delimiter.replace(d, "\\" + d);
			}
		}
		return delimiter;
	}
}