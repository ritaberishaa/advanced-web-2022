package com.cacttus.rita.advanced.web.dto.parkingSlot;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateParkingSlotsRequestDto {
    private Long parkingZoneId;
    private Integer numberOfSlots;
    private Boolean isHandicap;

    public Long getParkingZoneId() { return parkingZoneId;
    }

    public void setParkingZoneId(Long parkingZoneId) { this.parkingZoneId = parkingZoneId;
    }

    public Integer getNumberOfSlots() { return numberOfSlots;
    }

    public void setNumberOfSlots(Integer numberOfSlots) { this.numberOfSlots = numberOfSlots;
    }

}
