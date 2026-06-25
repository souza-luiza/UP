package com.up.up_back.services;

import com.up.up_back.dto.availability.CreateAvailabilityDto;
import com.up.up_back.entity.Availability;
import com.up.up_back.entity.User;
import com.up.up_back.repository.AvailabilityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {

    private final AvailabilityRepository availabilityRepository;

    @Override
    public Availability create(CreateAvailabilityDto dto, User user) {

        if (!dto.endTime().isAfter(dto.startTime())) throw new IllegalArgumentException("End time must be after start time");

        Availability availability = Availability.builder()
                .dayOfWeek(dto.dayOfWeek())
                .startTime(dto.startTime())
                .endTime(dto.endTime())
                .user(user)
                .build();

        return availabilityRepository.save(availability);
    }

    @Override
    public List<Availability> findAll(User user) {
        return availabilityRepository.findByUser(user);
    }

    @Override
    @Transactional
    public void delete(Long id, User user) {

        Availability availability = availabilityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found."));

        if (!availability.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You can't delete this subject.");
        }

        availabilityRepository.delete(availability);
    }
}
