package com.cacttus.rita.advanced.web.exception;

public class UnauthorizedException extends Exception{
    public UnauthorizedException(){
        super("You are not authorized to see this data!");
    }
}
