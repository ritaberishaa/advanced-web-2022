package com.cacttus.rita.advanced.web.exception;

public class UserWithEmailAlreadyExistException extends Exception {
    public UserWithEmailAlreadyExistException(){ super("User with the provided email already exists!");}
}
