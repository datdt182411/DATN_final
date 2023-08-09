package com.example.test.Exception;

public class ProductNotFoundException extends Throwable{
    public ProductNotFoundException(String message){
        super(message);
    }
}
