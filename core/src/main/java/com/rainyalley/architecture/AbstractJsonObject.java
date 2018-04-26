package com.rainyalley.architecture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class AbstractJsonObject {

    private ObjectMapper om = new ObjectMapper();

    public String toJson() {
        try {
            return om.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return super.toString() + " : " + e.getMessage();
        }
    }

    public Object toObject(String json) {
        try {
            return om.readValue(json, this.getClass());
        } catch (IOException e) {
            throw new IllegalArgumentException(json);
        }
    }

    @Override
    public String toString() {
        return toJson();
    }
}
