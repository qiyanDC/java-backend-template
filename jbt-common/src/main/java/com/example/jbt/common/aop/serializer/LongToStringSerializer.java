package com.example.jbt.common.aop.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializerBase;

public class LongToStringSerializer extends ToStringSerializerBase {

    public final static LongToStringSerializer instance = new LongToStringSerializer();

    public LongToStringSerializer() {
        super(Long.class);
    }
    
    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        Long valueLong = (Long) value;
        if(valueLong > Long.MAX_VALUE || valueLong < Long.MIN_VALUE) {
            gen.writeString(valueToString(valueLong));
        } else {
            gen.writeNumber(valueLong);
        }
    }

    @Override
    public String valueToString(Object value) {
        return value.toString();
    }
}