package com.example.test.Converter;

import org.springframework.stereotype.Component;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Component
public class ConvertDate {
        public String convertDate(String date) {
            Long timeStamp = Long.parseLong(date);
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date convert = new Date(timeStamp);
            return f.format(convert);
        }
    }

