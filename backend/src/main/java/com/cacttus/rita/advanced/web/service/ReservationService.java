package com.cacttus.rita.advanced.web.service;

import com.cacttus.rita.advanced.web.dto.reservation.CreateReservationRequestDto;
import com.cacttus.rita.advanced.web.dto.reservation.EditReservationRequestDto;
import com.cacttus.rita.advanced.web.dto.reservation.InvoiceDto;
import com.cacttus.rita.advanced.web.dto.reservation.ReservationDto;
import com.cacttus.rita.advanced.web.exception.ParkingSlotNotFreeBetweenTimeException;
import com.cacttus.rita.advanced.web.exception.ParkingSlotWithIdDoesNotExistException;
import com.cacttus.rita.advanced.web.exception.ReservationWithIdDoesNotExistException;
import com.cacttus.rita.advanced.web.exception.UnauthorizedException;
import com.cacttus.rita.advanced.web.exception.DateTimeException;
import com.cacttus.rita.advanced.web.model.ParkingSlot;
import com.cacttus.rita.advanced.web.model.Reservation;
import com.cacttus.rita.advanced.web.model.User;
import com.cacttus.rita.advanced.web.repository.ParkingSlotRepository;
import com.cacttus.rita.advanced.web.repository.ReservationRepository;
import com.cacttus.rita.advanced.web.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ParkingSlotRepository parkingSlotRepository;

    public ReservationService(ReservationRepository reservationRepository, UserRepository userRepository, ParkingSlotRepository parkingSlotRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.parkingSlotRepository = parkingSlotRepository;
    }

    public boolean parkingSlotIsFree(Long parkingSlotId, LocalDateTime fromTime, LocalDateTime toTime){
        return reservationRepository.getTakenReservations(parkingSlotId, fromTime, toTime).isEmpty();
    }

    public boolean parkingSlotIsFree(Long parkingSlotId, LocalDateTime fromTime, LocalDateTime toTime, Long reservationId){
        List<Reservation> reservations = reservationRepository.getTakenReservations(parkingSlotId, fromTime, toTime);
        if(reservations.size() == 1 && reservations.get(0).getId() == reservationId){
            return true;
        }
        return false;
    }

    public ReservationDto createNewReservation(CreateReservationRequestDto dto, String username) throws ParkingSlotWithIdDoesNotExistException, DateTimeException, ParkingSlotNotFreeBetweenTimeException {
        Reservation reservation = new Reservation();
        reservation.setCreatedTime(LocalDateTime.now());
        reservation.setUser(userRepository.getUserByUsername(username));
        Optional<ParkingSlot> slot = parkingSlotRepository.findById(dto.getParkingSlotId());

        if(slot.isPresent())
            reservation.setParkingSlot(slot.get());
        else{
            throw new ParkingSlotWithIdDoesNotExistException(dto.getParkingSlotId());
        }

        if(dto.getFromTime().isBefore(dto.getToTime())){
            reservation.setFromTime(dto.getFromTime());
            reservation.setToTime(dto.getToTime());
        }
        else{
            throw new DateTimeException();
        }

        if(!parkingSlotIsFree(slot.get().getId(), dto.getFromTime(), dto.getToTime())){
            throw new ParkingSlotNotFreeBetweenTimeException(slot.get().getId(), dto.getFromTime(), dto.getToTime());
        }

        double price = (double) ChronoUnit.HOURS.between(dto.getFromTime(), dto.getToTime()) * 1.5;
        reservation.setPrice(price);

        reservationRepository.save(reservation);

        ReservationDto responseDto = new ReservationDto();
        responseDto.setId(reservation.getId());
        responseDto.setCreatedTime(reservation.getCreatedTime());
        responseDto.setFromTime(reservation.getFromTime());
        responseDto.setToTime(reservation.getToTime());
        responseDto.setUserId(reservation.getUser().getId());
        responseDto.setParkingSlotId(reservation.getParkingSlot().getId());
        return responseDto;
    }
    public ReservationDto getReservationDtoWithId(Long id, String username) throws ReservationWithIdDoesNotExistException, UnauthorizedException {
        Optional<ReservationDto> reservation = reservationRepository.getReservationDtoById(id);

        if(reservation.isEmpty())
            throw new ReservationWithIdDoesNotExistException(id);

        if(!userRepository.findById(reservation.get().getUserId()).get().getUsername().equals(username))
            throw new UnauthorizedException();

        return reservation.get();
    }
    public ReservationDto editReservationWithId(Long id, EditReservationRequestDto dto, String username) throws ReservationWithIdDoesNotExistException, DateTimeException, ParkingSlotNotFreeBetweenTimeException, UnauthorizedException {
        Optional<Reservation> reservation = reservationRepository.findById(id);

        if(reservation.isEmpty())
            throw new ReservationWithIdDoesNotExistException(id);

        if(!reservation.get().getUser().getUsername().equals(username))
            throw new UnauthorizedException();

        if(dto.getFromTime().isBefore(LocalDateTime.now()))
            throw new DateTimeException();

        if(dto.getFromTime().isAfter(dto.getToTime()))
            throw new DateTimeException();

        if(!parkingSlotIsFree(reservation.get().getParkingSlot().getId(), dto.getFromTime(), dto.getToTime(), reservation.get().getId()))
            throw new ParkingSlotNotFreeBetweenTimeException(reservation.get().getParkingSlot().getId(), dto.getFromTime(), dto.getToTime());

        reservation.get().setFromTime(dto.getFromTime());
        reservation.get().setToTime(dto.getToTime());

        reservationRepository.save(reservation.get());

        Optional<ReservationDto> response = reservationRepository.getReservationDtoById(id);

        return response.get();
    }
    public void deleteReservationWithId(Long id, String username) throws ReservationWithIdDoesNotExistException, UnauthorizedException {
        Optional<Reservation> reservation = reservationRepository.findById(id);

        if(reservation.isEmpty())
            throw new ReservationWithIdDoesNotExistException(id);

        if(!reservation.get().getUser().getUsername().equals(username))
            throw new UnauthorizedException();

        reservationRepository.delete(reservation.get());
    } public List<ReservationDto> getReservations(String name, Long userId, LocalDateTime fromTime, LocalDateTime toTime, Long slotId, Long zoneId, Long cityId) throws UnauthorizedException, DateTimeException {
        User requestingUser = userRepository.getUserByUsername(name);

        //if a user wants another users reservations, we don't allow it
        if(requestingUser.getRole().equals("USER") && (userId != null || userId != requestingUser.getId()))
            throw new UnauthorizedException();

        if(requestingUser.getRole().equals("USER"))
            userId = requestingUser.getId();

        if(fromTime.isAfter(toTime))
            throw new DateTimeException();

        if(userId != null){
            if(slotId != null)
                return reservationRepository.getAllBySlotIdAndUser(slotId, userId, fromTime, toTime);
            else if(zoneId != null)
                return reservationRepository.getAllByZoneIdAndUser(zoneId, userId, fromTime, toTime);
            else if(cityId != null)
                return reservationRepository.getAllByCityIdAndUser(cityId, userId, fromTime, toTime);
            else
                return reservationRepository.getAllByUser(userId, fromTime, toTime);
        }
        else{
            if(slotId != null)
                return reservationRepository.getAllBySlotId(slotId, fromTime, toTime);
            else if(zoneId != null)
                return reservationRepository.getAllByZoneId(zoneId, fromTime, toTime);
            else if(cityId != null)
                return reservationRepository.getAllByCityId(cityId, fromTime, toTime);
            else
                return reservationRepository.getAll(fromTime, toTime);
        }
    }

    public InvoiceDto getInvoice(Long id, String username) throws ReservationWithIdDoesNotExistException, UnauthorizedException {
        Optional<Reservation> reservation = reservationRepository.findById(id);

        if(reservation.isEmpty())
            throw new ReservationWithIdDoesNotExistException(id);

        if(!reservation.get().getUser().getUsername().equals(username))
            throw new UnauthorizedException();

        InvoiceDto response = new InvoiceDto();
        response.setFromTime(reservation.get().getFromTime());
        response.setToTime(reservation.get().getToTime());
        response.setCityId(reservation.get().getParkingSlot().getParkingZone().getCity().getId());
        response.setParkingZoneId(reservation.get().getParkingSlot().getParkingZone().getId());
        response.setParkingSlotId(reservation.get().getParkingSlot().getId());
        response.setPrice(reservation.get().getPrice());
        response.setTax(0.18);
        response.setTotalPrice(0.18 * reservation.get().getPrice());

        return response;
    }
}
