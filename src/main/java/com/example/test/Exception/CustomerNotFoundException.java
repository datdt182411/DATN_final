package com.example.test.Exception;

public class CustomerNotFoundException extends Throwable{
    public CustomerNotFoundException(String message){
        super(message);
    }
}
