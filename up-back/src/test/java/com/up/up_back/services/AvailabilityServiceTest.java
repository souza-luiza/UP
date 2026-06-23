package com.up.up_back.services;

import com.up.up_back.dto.availability.CreateAvailabilityDto;
import com.up.up_back.entity.Availability;
import com.up.up_back.entity.User;
import com.up.up_back.repository.AvailabilityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AvailabilityServiceTest {

    @Mock
    private AvailabilityRepository availabilityRepository;

    @InjectMocks
    private AvailabilityServiceImpl availabilityService;

    @Test
    void shouldCreateAvailabilitySuccessfully() {

        User user = User.builder()
                .id(1L)
                .name("Joao")
                .email("joao@gmail.com")
                .build();

        CreateAvailabilityDto dto = new CreateAvailabilityDto(DayOfWeek.MONDAY, LocalTime.of(19, 0), LocalTime.of(22, 0));

        when(availabilityRepository.save(any(Availability.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Availability availability = availabilityService.create(dto, user);

        assertNotNull(availability);
        assertEquals(DayOfWeek.MONDAY, availability.getDayOfWeek());
        assertEquals(LocalTime.of(19, 0), availability.getStartTime());
        assertEquals(LocalTime.of(22, 0), availability.getEndTime());
    }

    @Test
    void shouldAssociateAvailabilityWithUser() {

        User user = User.builder()
                .id(1L)
                .name("Joao")
                .email("joao@gmail.com")
                .build();

        CreateAvailabilityDto dto = new CreateAvailabilityDto(DayOfWeek.MONDAY, LocalTime.of(19, 0), LocalTime.of(22, 0));

        when(availabilityRepository.save(any(Availability.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Availability availability = availabilityService.create(dto, user);

        assertNotNull(availability.getUser());
        assertEquals(user.getId(), availability.getUser().getId());
    }

    @Test
    void shouldThrowExceptionWhenEndTimeIsBeforeStartTime() {

        User user = User.builder()
                .id(1L)
                .build();

        CreateAvailabilityDto dto = new CreateAvailabilityDto(
                        DayOfWeek.MONDAY,
                        LocalTime.of(20, 0),
                        LocalTime.of(19, 0)
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> availabilityService.create(dto, user)
        );
    }

    @Test
    void shouldThrowExceptionWhenStartTimeEqualsEndTime() {

        User user = User.builder()
                .id(1L)
                .build();

        CreateAvailabilityDto dto = new CreateAvailabilityDto(
                DayOfWeek.MONDAY,
                LocalTime.of(20, 0),
                LocalTime.of(20, 0)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> availabilityService.create(dto, user)
        );
    }
}
