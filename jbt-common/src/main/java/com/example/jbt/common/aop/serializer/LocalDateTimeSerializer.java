package com.example.jbt.common.aop.serializer;

import java.io.IOException;
import java.time.LocalDateTime;

import com.example.jbt.common.constants.DatePattern;
import com.example.jbt.common.utils.DateUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

    private String pattern;

    public LocalDateTimeSerializer(String pattern) {
        this.pattern = pattern;
    }

    public LocalDateTimeSerializer() {
        this.pattern = DatePattern.NORM_DATETIME_PATTERN;
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException
    {
        gen.writeString(DateUtils.format(value, this.pattern));
    }
}