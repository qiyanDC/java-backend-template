package com.example.jbt.common.aop.deserializer;

import java.io.IOException;
import java.time.LocalDateTime;

import com.example.jbt.common.utils.DateUtils;
import com.example.jbt.common.utils.ObjectUtils;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    private String pattern;

    public LocalDateTimeDeserializer(String pattern) {
        this.pattern = pattern;
    }

    public LocalDateTimeDeserializer() {
    }

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException
    {
        String value = p.getValueAsString();
        value = value.split("Z")[0];
        if(ObjectUtils.isNotEmpty(this.pattern)) {
            return DateUtils.parse(value, this.pattern);
        }
        
        return DateUtils.parse(value);
    }
}