package com.cacttus.rita.advanced.web.exception;

import java.time.LocalDateTime;

public class ParkingSlotNotFreeBetweenTimeException extends Exception{
    public ParkingSlotNotFreeBetweenTimeException (Long id, LocalDateTime timeFrom, LocalDateTime timeTo){
        super("The parking slot with id " + id + " is not free in the interval between (" + timeFrom + ")-(" + timeTo + ").");
    }
}
