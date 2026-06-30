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
import org.springframework.security.access.AccessDeniedException;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Test
    void shouldNotAllowOverlappingAvailabilities() {

        User user = User.builder()
                .id(1L)
                .build();

        CreateAvailabilityDto dto = new CreateAvailabilityDto(
                DayOfWeek.MONDAY,
                LocalTime.of(15, 0),
                LocalTime.of(17, 0)
        );

        Availability existing = Availability.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(16, 0))
                .user(user)
                .build();

        when(availabilityRepository.findByUser(user))
                .thenReturn(List.of(existing));

        assertThrows(
                IllegalArgumentException.class,
                () -> availabilityService.create(dto, user)
        );

        verify(availabilityRepository, never()).save(any());
    }

    @Test
    void shouldAllowAdjacentAvailabilities() {

        User user = User.builder()
                .id(1L)
                .build();

        Availability existing = Availability.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(16, 0))
                .user(user)
                .build();

        CreateAvailabilityDto dto = new CreateAvailabilityDto(
                DayOfWeek.MONDAY,
                LocalTime.of(16, 0),
                LocalTime.of(18, 0)
        );

        when(availabilityRepository.findByUser(user))
                .thenReturn(List.of(existing));

        when(availabilityRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Availability availability = availabilityService.create(dto, user);

        assertNotNull(availability);

        verify(availabilityRepository).save(any());
    }

    @Test
    void shouldNotAllowEqualAvailabilities() {

        User user = User.builder()
                .id(1L)
                .build();

        CreateAvailabilityDto dto = new CreateAvailabilityDto(
                DayOfWeek.MONDAY,
                LocalTime.of(14, 0),
                LocalTime.of(16, 0)
        );

        Availability existing = Availability.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(16, 0))
                .user(user)
                .build();

        when(availabilityRepository.findByUser(user))
                .thenReturn(List.of(existing));

        assertThrows(
                IllegalArgumentException.class,
                () -> availabilityService.create(dto, user)
        );

        verify(availabilityRepository, never()).save(any());
    }

    @Test
    void shouldReturnAllAvailabilitiesFromUser() {

        User user = User.builder()
                .id(1L)
                .build();

        List<Availability> availabilities = List.of(
                Availability.builder().user(user).build(),
                Availability.builder().user(user).build()
        );

        when(availabilityRepository.findByUser(user))
                .thenReturn(availabilities);

        List<Availability> result = availabilityService.findAll(user);

        assertEquals(2, result.size());
        assertEquals(availabilities, result);

        verify(availabilityRepository).findByUser(user);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingAvailability() {

        User user = User.builder()
                .id(1L)
                .build();

        when(availabilityRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> availabilityService.delete(1L, user)
        );

        verify(availabilityRepository).findById(1L);
    }

    @Test
    void shouldNotAllowDeletingAnotherUsersAvailability() {

        User owner = User.builder()
                .id(1L)
                .build();

        User anotherUser = User.builder()
                .id(2L)
                .build();

        Availability availability = Availability.builder()
                .user(owner)
                .build();

        when(availabilityRepository.findById(1L))
                .thenReturn(Optional.of(availability));

        assertThrows(
                AccessDeniedException.class,
                () -> availabilityService.delete(1L, anotherUser)
        );

        verify(availabilityRepository, never()).delete(any());
    }

    @Test
    void shouldDeleteAvailabilitySuccessfully() {

        User user = User.builder()
                .id(1L)
                .build();

        Availability availability = Availability.builder()
                .id(1L)
                .user(user)
                .build();

        when(availabilityRepository.findById(1L))
                .thenReturn(Optional.of(availability));

        availabilityService.delete(1L, user);

        verify(availabilityRepository).delete(availability);
    }
}
