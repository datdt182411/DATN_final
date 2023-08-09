package com.example.test.Converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
@Component
public class ConvertObject implements Converter<Object, String[]> {

    @Override
    public String[] convert(Object source) {
        try {
            String json = new ObjectMapper().writeValueAsString(source);
            System.out.println(json);
            json = json.substring(1,json.length()-1);
            String[] data = json.split(",");

            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
