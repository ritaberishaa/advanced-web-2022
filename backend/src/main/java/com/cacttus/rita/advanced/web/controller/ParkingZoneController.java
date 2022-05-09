package com.cacttus.rita.advanced.web.controller;

import com.cacttus.rita.advanced.web.dto.http.ErrorResponse;
import com.cacttus.rita.advanced.web.dto.http.GenericJsonResponse;
import com.cacttus.rita.advanced.web.dto.parkingZone.ParkingZoneDto;
import com.cacttus.rita.advanced.web.exception.CityWithIdDoesNotExistException;
import com.cacttus.rita.advanced.web.exception.ParkingZoneWithIdDoesNotExistException;
import com.cacttus.rita.advanced.web.service.ParkingSlotService;
import com.cacttus.rita.advanced.web.service.ParkingZoneService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/zone")
public class ParkingZoneController {
    private final ParkingZoneService parkingZoneService;
    private final ParkingSlotService parkingSlotService;

    public ParkingZoneController(ParkingZoneService parkingZoneService, ParkingSlotService parkingSlotService) {
        this.parkingZoneService = parkingZoneService;
        this.parkingSlotService = parkingSlotService;
    }

    @PostMapping
    public GenericJsonResponse<?> createParkingZone(@RequestBody ParkingZoneDto data, HttpServletResponse response) {
        try {
            return new GenericJsonResponse<>(true, parkingZoneService.saveParkingZone(data));
        } catch (CityWithIdDoesNotExistException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new GenericJsonResponse<ErrorResponse>(false, new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping
    public GenericJsonResponse<?> getAllParkingZones() {
        return new GenericJsonResponse<>(true, parkingZoneService.getAllParkingZones());
    }

    @GetMapping("/{id}")
    public GenericJsonResponse<?> getParkingZone(@PathVariable Long id, HttpServletResponse response) {
        try {
            return new GenericJsonResponse<>(true, parkingZoneService.getParkingZoneWithId(id));
        }
        catch (ParkingZoneWithIdDoesNotExistException e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new GenericJsonResponse<>(false, new ErrorResponse(e.getLocalizedMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public GenericJsonResponse<?> softDeleteParkingZone(@PathVariable Long id, HttpServletResponse response) {
        try {
            parkingZoneService.deleteParkingZoneWithId(id);
            return new GenericJsonResponse<>(true, null);
        }
        catch (ParkingZoneWithIdDoesNotExistException e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new GenericJsonResponse<>(false, new ErrorResponse(e.getLocalizedMessage()));
        }
    }

    @GetMapping("/{id}/slots")
    public GenericJsonResponse<?> getParkingZoneSlots(@PathVariable Long id, HttpServletResponse response) {
        try {
            return new GenericJsonResponse<>(true, parkingSlotService.getSlotsInZone(id));
        }
        catch (ParkingZoneWithIdDoesNotExistException e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new GenericJsonResponse<>(false, new ErrorResponse(e.getLocalizedMessage()));
        }
    }

}
