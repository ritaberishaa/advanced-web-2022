package com.cacttus.rita.advanced.web.controller;

import com.cacttus.rita.advanced.web.dto.http.ErrorResponse;
import com.cacttus.rita.advanced.web.dto.http.GenericJsonResponse;
import com.cacttus.rita.advanced.web.dto.reservation.CreateReservationRequestDto;
import com.cacttus.rita.advanced.web.dto.reservation.EditReservationRequestDto;
import com.cacttus.rita.advanced.web.exception.*;
import com.cacttus.rita.advanced.web.service.ReservationService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService){
        this.reservationService = reservationService;
    }

    @PostMapping
    public GenericJsonResponse<?> createNewReservation(@RequestBody CreateReservationRequestDto dto, Principal principal, HttpServletResponse response){
        try{
            return new GenericJsonResponse<>(true, reservationService.createNewReservation(dto, principal.getName()));
        }
        catch (ParkingSlotWithIdDoesNotExistException | DateTimeException | ParkingSlotNotFreeBetweenTimeException e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new GenericJsonResponse<>(false, new ErrorResponse(e.getMessage()));
        }
    }
    @GetMapping
    public GenericJsonResponse<?> getAllReservations(Principal principal, HttpServletResponse response,
                                                     @RequestParam(required = false) Long cityId, @RequestParam(required = false) Long zoneId,
                                                     @Parameter(schema = @Schema(pattern = "^(\\d{2})-(\\d{2})-(\\d{4}) (\\d{2}):(\\d{2})$")) @RequestParam(required = true) @DateTimeFormat(pattern="dd-MM-yyyy HH:mm") LocalDateTime fromTime,
                                                     @Parameter(schema = @Schema(pattern = "^(\\d{2})-(\\d{2})-(\\d{4}) (\\d{2}):(\\d{2})$")) @RequestParam(required = true) @DateTimeFormat(pattern="dd-MM-yyyy HH:mm") LocalDateTime toTime,
                                                     @RequestParam(required = false) Long userId, @RequestParam(required = false) Long slotId) {
        try {
            return new GenericJsonResponse<>(true, reservationService.getReservations(principal.getName(), userId, fromTime, toTime, slotId, zoneId, cityId));
        } catch (DateTimeException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new GenericJsonResponse<>(false, new ErrorResponse(e.getMessage()));
        } catch (UnauthorizedException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return new GenericJsonResponse<>(false, new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public GenericJsonResponse<?> getReservationWithId(@PathVariable Long id, Principal principal, HttpServletResponse response){
        try{
            return new GenericJsonResponse<>(true, reservationService.getReservationDtoWithId(id, principal.getName()));
        }
        catch (ReservationWithIdDoesNotExistException e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new GenericJsonResponse<>(false, new ErrorResponse(e.getMessage()));
        }
        catch (UnauthorizedException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return new GenericJsonResponse<>(false, new ErrorResponse(e.getMessage()));
        }
    }
    @PutMapping("/{id}")
    public GenericJsonResponse<?> editReservationWithId(@PathVariable Long id, @RequestBody EditReservationRequestDto dto, Principal principal, HttpServletResponse response){
        try{
            return new GenericJsonResponse<>(true, reservationService.editReservationWithId(id, dto, principal.getName()));
        }
        catch (ReservationWithIdDoesNotExistException | DateTimeException | ParkingSlotNotFreeBetweenTimeException e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new GenericJsonResponse<>(false, new ErrorResponse(e.getMessage()));
        }
        catch (UnauthorizedException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return new GenericJsonResponse<>(false, new ErrorResponse(e.getMessage()));
        }
    }
    @DeleteMapping("/{id}")
    public GenericJsonResponse<?> deleteReservationWithId(@PathVariable Long id, Principal principal, HttpServletResponse response){
        try{
            reservationService.deleteReservationWithId(id, principal.getName());
            return new GenericJsonResponse<>(true, null);
        }
        catch (ReservationWithIdDoesNotExistException e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new GenericJsonResponse<>(false, new ErrorResponse(e.getMessage()));
        }
        catch (UnauthorizedException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return new GenericJsonResponse<>(false, new ErrorResponse(e.getMessage()));
        }
    }

}
