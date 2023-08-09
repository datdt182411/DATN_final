package com.example.test.Exception;

public class OrderNotFoundException extends Throwable{
    public OrderNotFoundException(String message) {
        super(message);
    }
}
