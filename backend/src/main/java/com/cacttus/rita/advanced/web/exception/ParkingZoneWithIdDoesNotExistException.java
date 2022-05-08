package com.cacttus.rita.advanced.web.exception;

public class ParkingZoneWithIdDoesNotExistException extends Exception {
    public ParkingZoneWithIdDoesNotExistException(Long id){
        super("Parking Zone With id " + id + " does not exist!");
    }
}
