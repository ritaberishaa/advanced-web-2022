package com.cacttus.rita.advanced.web.exception;

public class CityWithIdDoesNotExistException  extends Exception{
    public CityWithIdDoesNotExistException(Long id) { super("City with id " + " doesn't exist");}
}
