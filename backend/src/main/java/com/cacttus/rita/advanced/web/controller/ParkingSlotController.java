package com.cacttus.rita.advanced.web.controller;

import com.cacttus.rita.advanced.web.dto.http.ErrorResponse;
import com.cacttus.rita.advanced.web.dto.http.GenericJsonResponse;
import com.cacttus.rita.advanced.web.dto.parkingSlot.EditParkingSlotRequestDto;
import com.cacttus.rita.advanced.web.dto.parkingSlot.ParkingSlotDto;
import com.cacttus.rita.advanced.web.exception.ParkingSlotWithIdDoesNotExistException;
import com.cacttus.rita.advanced.web.exception.ParkingZoneWithIdDoesNotExistException;
import com.cacttus.rita.advanced.web.service.ParkingSlotService;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/slots")
public class ParkingSlotController {
    private final ParkingSlotService parkingSlotService;

    public ParkingSlotController(ParkingSlotService parkingSlotService) {
        this.parkingSlotService = parkingSlotService;
    }

    @GetMapping("/{id}")
    public GenericJsonResponse<?> getParkingSlotWithId(@PathVariable Long id, HttpServletResponse response){
        try{
            return new GenericJsonResponse<ParkingSlotDto>(true, parkingSlotService.getSlotWithId(id));
        }
        catch (ParkingSlotWithIdDoesNotExistException e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new GenericJsonResponse<>(false, new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public GenericJsonResponse<?> editParkingSlotWithId(@PathVariable Long id, HttpServletResponse response, @RequestBody EditParkingSlotRequestDto dto){
        try{
            return new GenericJsonResponse<ParkingSlotDto>(true, parkingSlotService.editSlotWithId(id, dto));
        }
        catch (ParkingSlotWithIdDoesNotExistException | ParkingZoneWithIdDoesNotExistException e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new GenericJsonResponse<>(false, new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public GenericJsonResponse<?> deleteParkingSlotWithId(@PathVariable Long id, HttpServletResponse response){
        try{
            parkingSlotService.softDeleteSlotWithId(id);
            return new GenericJsonResponse<ParkingSlotDto>(true, null);
        }
        catch (ParkingSlotWithIdDoesNotExistException e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new GenericJsonResponse<>(false, new ErrorResponse(e.getMessage()));
        }
    }

}
