package com.example.jbt.common.utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jbt.common.codec.deserializer.LocalDateTimeDeserializer;
import com.example.jbt.common.codec.serializer.LocalDateTimeSerializer;
import com.example.jbt.common.codec.serializer.LongToStringSerializer;
import com.example.jbt.common.constants.DatePattern;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

/**
 * 简化Json输出，将Java对象转换为Json字符串。若需要更换解析库，只需要修改这个文件
 * @author qiyan
 * @version 1.0
 * @since 2024-04-28
 */
public class JsonUtils {

    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);

    private JsonUtils() {
    }

    private static final String DYNC_INCLUDE = "DYNC_INCLUDE";
    private static final String DYNC_FILTER = "DYNC_FILTER";

    @JsonFilter(DYNC_FILTER)
    interface DynamicFilter {
    }

    @JsonFilter(DYNC_INCLUDE)
    interface DynamicInclude {
    }

    public static final ThreadLocal<ObjectMapper> om =  new ThreadLocal<ObjectMapper>() {
        @Override
	    @SuppressWarnings({ "unchecked", "rawtypes" })
        protected ObjectMapper initialValue() {
            ObjectMapper objectMapper = new ObjectMapper();

            objectMapper.setDateFormat(new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN));
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addSerializer(Long.class, LongToStringSerializer.instance);
            simpleModule.addSerializer(Long.TYPE, LongToStringSerializer.instance);

            // 枚举转换;
            simpleModule.addSerializer(Enum.class, new JsonSerializer<Enum>() {
                @Override
                public void serialize(Enum value, JsonGenerator gen, SerializerProvider serializers)
                        throws IOException {
                    gen.writeString(EnumUtils.getCode(value, value.getClass()));
                }
            });
            simpleModule.addDeserializer(Enum.class, new JsonDeserializer<Enum>() {
                @Override
                public Enum deserialize(JsonParser p, DeserializationContext ctxt)
                        throws IOException {
                    JsonStreamContext parsingContext = p.getParsingContext();
                    Object rootObj = parsingContext.getCurrentValue();
                    String fieldName = parsingContext.getCurrentName();
                    Field field = ReflectUtils.getField(rootObj.getClass(), fieldName);
                    return EnumUtils.valueOf(p.getValueAsInt(), (Class) field.getType());
                }
            });

            JavaTimeModule javaTimeModule = new JavaTimeModule();
            // 解决日期格式化不要nano问题
            javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
            javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());

            javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)));
            javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)));

            objectMapper.registerModule(simpleModule);
            objectMapper.registerModule(javaTimeModule);

            // json字符串中多余的属性，不解析
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            // 空属性对象时，不处理
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            // 是否允许最后一个多余逗号
            objectMapper.configure(JsonReadFeature.ALLOW_TRAILING_COMMA.mappedFeature(), true);

            // 这个特性，决定了解析器是否将自动关闭那些不属于parser自己的输入源。
            // 如果禁止，则调用应用不得不分别去关闭那些被用来创建parser的基础输入流InputStream和reader；
            // 默认是true
            objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
            // 是否允许'/*'、'*/'或者'//'这样的注释出现
            objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);

            // 反序列化是否允许属性名称不带双引号
            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

            //是否允许单引号来包住属性名称和字符串值
            objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

	            //是否允许JSON字符串包含非引号控制字符（值小于32的ASCII字符，包含制表符和换行符）
            objectMapper.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);

            //是否允许JSON整数以多个0开始
            objectMapper.configure(JsonReadFeature.ALLOW_LEADING_ZEROS_FOR_NUMBERS.mappedFeature(), true);

            //是否缩进排列输出,默认false，不用美化了
            objectMapper.configure(SerializationFeature.INDENT_OUTPUT, false);

            //序列化枚举是否以toString()来输出，默认false，即默认以name()来输出
            objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);

            //序列化枚举是否以ordinal()来输出，默认false
            objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, true);

            //序列化单元素数组时不以数组来输出，默认false
            objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);

            //序列化Map时对key进行排序操作，默认false
            objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);

            //序列化char[]时以json数组输出，默认false
            objectMapper.configure(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS, true);

            return objectMapper;
        }
    };

    public static ObjectMapper getObjectMapper() {
        return om.get();
    }

    /**
     * 排除或指定包含字段，一旦使用，Bean将不是全部属性输出
     * @param entity 类型
     * @param includes 包含字段
     * @param excludes 排除字段
     * @return String
     */
    public static String toJson(Object entity, Set<String> includes, Set<String> excludes) {
        ObjectMapper objectMapper = JsonUtils.getObjectMapper().copy();//复制一个，用于改造属性
        if(null != includes || null != excludes) {
            SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider();
            //只有一个会生效
            if(null != includes) {
                simpleFilterProvider.addFilter(DYNC_INCLUDE, SimpleBeanPropertyFilter.filterOutAllExcept(includes));
                objectMapper.addMixIn(entity.getClass(), DynamicInclude.class);//必须指定混入接口，否则不采取包含操作
            } else if(null != excludes) {
                simpleFilterProvider.addFilter(DYNC_FILTER, SimpleBeanPropertyFilter.serializeAllExcept(excludes));
                objectMapper.addMixIn(entity.getClass(), DynamicFilter.class);//同上
            }
            objectMapper.setFilterProvider(simpleFilterProvider);
        }
        try {
            return objectMapper.writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            log.error("json转换异常", e);
        }
        return "{}";
    }

    public static <T> String toJson(T obj) {
        try {
            return JsonUtils.getObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("json转换异常", e);
        }
        return "{}";
    }

    public static <T> T toObject(Object obj, Class<T> objClass) {
        return JsonUtils.toObject(JsonUtils.toJson(obj), objClass);
    }

    public static <T> T toObject(String json, Class<T> objClass) {
        T objT = null;
        try {
            objT = JsonUtils.getObjectMapper().readValue(json, objClass);
        } catch (JsonProcessingException e) {
            log.error("json转换异常", e);
        }
        return objT;
    }

    public static <T> T toObject(String json, TypeReference<T> typeReference) {
        T objT = null;
        try {
            objT = JsonUtils.getObjectMapper().readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            log.error("json转换异常", e);
        }
        return objT;
    }
}