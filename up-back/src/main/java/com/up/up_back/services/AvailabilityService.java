package com.up.up_back.services;

import com.up.up_back.dto.availability.CreateAvailabilityDto;
import com.up.up_back.entity.Availability;
import com.up.up_back.entity.User;

import java.util.List;

public interface AvailabilityService {

    Availability create(CreateAvailabilityDto dto, User user);
    List<Availability> findAll(User user);
}
