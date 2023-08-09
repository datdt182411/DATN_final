package com.example.test.Exception;

public class OrderLineNotFoundException extends Throwable{
    public OrderLineNotFoundException(String message) {
        super(message);
    }
}
