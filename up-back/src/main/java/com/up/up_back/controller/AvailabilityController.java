package com.up.up_back.controller;

import com.up.up_back.dto.availability.AvailabilityResponseDto;
import com.up.up_back.dto.availability.CreateAvailabilityDto;
import com.up.up_back.entity.Availability;
import com.up.up_back.security.UserDetailsImpl;
import com.up.up_back.services.AvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/availabilities")
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    @PostMapping
    public AvailabilityResponseDto create(@RequestBody @Valid CreateAvailabilityDto dto, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Availability availability = availabilityService.create(dto, userDetails.getUser());

        return new AvailabilityResponseDto(availability.getId(), availability.getDayOfWeek(), availability.getStartTime(), availability.getEndTime());
    }

    @GetMapping
    public List<AvailabilityResponseDto> findAll(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {

        return availabilityService.findAll(userDetails.getUser())
                .stream()
                .map(availability ->
                        new AvailabilityResponseDto(
                                availability.getId(),
                                availability.getDayOfWeek(),
                                availability.getStartTime(),
                                availability.getEndTime()
                        )
                )
                .toList();
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        availabilityService.delete(id, userDetails.getUser());
    }
}
