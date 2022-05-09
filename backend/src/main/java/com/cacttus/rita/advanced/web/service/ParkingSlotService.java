package com.cacttus.rita.advanced.web.service;

import com.cacttus.rita.advanced.web.dto.parkingSlot.CreateParkingSlotsRequestDto;
import com.cacttus.rita.advanced.web.dto.parkingSlot.EditParkingSlotRequestDto;
import com.cacttus.rita.advanced.web.dto.parkingSlot.ParkingSlotDto;
import com.cacttus.rita.advanced.web.exception.DateTimeException;
import com.cacttus.rita.advanced.web.exception.ParkingSlotWithIdDoesNotExistException;
import com.cacttus.rita.advanced.web.exception.ParkingZoneWithIdDoesNotExistException;
import com.cacttus.rita.advanced.web.model.ParkingSlot;
import com.cacttus.rita.advanced.web.model.ParkingZone;
import com.cacttus.rita.advanced.web.repository.ParkingSlotRepository;
import com.cacttus.rita.advanced.web.repository.ParkingZoneRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class ParkingSlotService {
    private final ParkingZoneRepository parkingZoneRepository;
    private final ParkingSlotRepository parkingSlotRepository;

    public ParkingSlotService(ParkingZoneRepository parkingZoneRepository, ParkingSlotRepository parkingSlotRepository) {
        this.parkingZoneRepository = parkingZoneRepository;
        this.parkingSlotRepository = parkingSlotRepository;
    }

    public void saveSlots(CreateParkingSlotsRequestDto slots) throws ParkingZoneWithIdDoesNotExistException {
        Optional<ParkingZone> zone = parkingZoneRepository.findById(slots.getParkingZoneId());

        if (zone.isEmpty())
            throw new ParkingZoneWithIdDoesNotExistException(slots.getParkingZoneId());

        for (int i = 0; i < slots.getNumberOfSlots(); i++) {
            parkingSlotRepository.save(new ParkingSlot(zone.get(), slots.isHandicap()));
        }
    }

    public ParkingSlotDto getSlotWithId(Long id) throws ParkingSlotWithIdDoesNotExistException {
        Optional<ParkingSlot> slot = parkingSlotRepository.findById(id);

        if (slot.isEmpty())
            throw new ParkingSlotWithIdDoesNotExistException(id);

        ParkingSlotDto dto = new ParkingSlotDto();
        dto.setFree(slot.get().isFree());
        dto.setHandicap(slot.get().isHandicap());
        dto.setId(slot.get().getId());
        dto.setParkingZoneId(slot.get().getParkingZone().getId());

        return dto;
    }

    public ParkingSlotDto editSlotWithId(Long id, EditParkingSlotRequestDto dto) throws ParkingSlotWithIdDoesNotExistException, ParkingZoneWithIdDoesNotExistException {
        Optional<ParkingSlot> slot = parkingSlotRepository.findById(id);

        if (slot.isEmpty())
            throw new ParkingSlotWithIdDoesNotExistException(id);

//if the user wants to change the parking zone
        if (dto.getParkingZoneId() != null) {
            Optional<ParkingZone> zone = parkingZoneRepository.findById(dto.getParkingZoneId());

            if (zone.isEmpty())
                throw new ParkingZoneWithIdDoesNotExistException(dto.getParkingZoneId());

            slot.get().setParkingZone(zone.get());
        }

        if(dto.isFree() != null)
            slot.get().setFree(dto.isFree());

        if(dto.isHandicap() != null)
            slot.get().setHandicap(dto.isHandicap());

        parkingSlotRepository.save(slot.get());

        ParkingSlotDto responseDto = new ParkingSlotDto();
        responseDto.setParkingZoneId(slot.get().getParkingZone().getId());
        responseDto.setId(slot.get().getId());
        responseDto.setFree(slot.get().isFree());
        responseDto.setHandicap(slot.get().isHandicap());

        return responseDto;
    }
    public void softDeleteSlotWithId(Long id) throws ParkingSlotWithIdDoesNotExistException {
        Optional<ParkingSlot> slot = parkingSlotRepository.findById(id);

        if(slot.isEmpty())
            throw new ParkingSlotWithIdDoesNotExistException(id);

        slot.get().setActive(false);

        parkingSlotRepository.save(slot.get());
    }

    public List<ParkingSlotDto> getSlotsInZone(Long zoneId) throws ParkingZoneWithIdDoesNotExistException {
        Optional<ParkingZone> zone = parkingZoneRepository.findById(zoneId);

        if(zone.isEmpty())
            throw new ParkingZoneWithIdDoesNotExistException(zoneId);

        return parkingSlotRepository.getAllSlotsAtZone(zoneId);
    }
    public List<ParkingSlotDto> getFreeSlots(LocalDateTime fromTime, LocalDateTime toTime, Boolean isHandicap, Long zoneId, Long cityId) throws DateTimeException {
        if(fromTime.isAfter(toTime))
            throw new DateTimeException();

        if(isHandicap == null)
            isHandicap = false;

        if(zoneId != null){
            return parkingSlotRepository.getAllByZoneId(zoneId, isHandicap);
        }
        else if(cityId != null){
            return parkingSlotRepository.getAllByCityId(cityId, isHandicap);
        }
        else{
            return parkingSlotRepository.getAllByHandicap(isHandicap);
        }
    }
}

