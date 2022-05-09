package com.cacttus.rita.advanced.web.controller;

import com.cacttus.rita.advanced.web.dto.http.ErrorResponse;
import com.cacttus.rita.advanced.web.dto.http.GenericJsonResponse;
import com.cacttus.rita.advanced.web.dto.parkingSlot.CreateParkingSlotsRequestDto;
import com.cacttus.rita.advanced.web.dto.parkingSlot.EditParkingSlotRequestDto;
import com.cacttus.rita.advanced.web.dto.parkingSlot.ParkingSlotDto;
import com.cacttus.rita.advanced.web.exception.DateTimeException;
import com.cacttus.rita.advanced.web.exception.ParkingSlotWithIdDoesNotExistException;
import com.cacttus.rita.advanced.web.exception.ParkingZoneWithIdDoesNotExistException;
import com.cacttus.rita.advanced.web.service.ParkingSlotService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/slot")
public class ParkingSlotController {
    private final ParkingSlotService parkingSlotService;

    public ParkingSlotController(ParkingSlotService parkingSlotService) {
        this.parkingSlotService = parkingSlotService;
    }

    @GetMapping
    public GenericJsonResponse<?> getFreeParkingSlots(HttpServletResponse response,
                                                      @RequestParam(required = false) Long cityId, @RequestParam(required = false) Long zoneId,
                                                      @RequestParam(required = false) Boolean isHandicap,
                                                      @Parameter(schema = @Schema(pattern = "^(\\d{2})-(\\d{2})-(\\d{4}) (\\d{2}):(\\d{2})$")) @RequestParam(required = true) @DateTimeFormat(pattern="dd-MM-yyyy HH:mm") LocalDateTime fromTime,
                                                      @Parameter(schema = @Schema(pattern = "^(\\d{2})-(\\d{2})-(\\d{4}) (\\d{2}):(\\d{2})$")) @RequestParam(required = true) @DateTimeFormat(pattern="dd-MM-yyyy HH:mm") LocalDateTime toTime){

        try{
            return new GenericJsonResponse<>(true, parkingSlotService.getFreeSlots(fromTime, toTime, isHandicap, zoneId, cityId));
        }
        catch (DateTimeException e){
            return new GenericJsonResponse<>(false, new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping
    public GenericJsonResponse<?> createParkingSlots(@RequestBody CreateParkingSlotsRequestDto slots){
        try{
            parkingSlotService.saveSlots(slots);
            return new GenericJsonResponse<>(true, null);
        }
        catch (ParkingZoneWithIdDoesNotExistException e){
            return new GenericJsonResponse<>(false, new ErrorResponse(e.getMessage()));
        }
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
