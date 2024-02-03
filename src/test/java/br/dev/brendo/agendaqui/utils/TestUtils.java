package br.dev.brendo.agendaqui.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtils {
    public static String objectToJson(Object obj){
        try{
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
