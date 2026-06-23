package com.up.up_back.repository;

import com.up.up_back.entity.Availability;
import com.up.up_back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

    List<Availability> findByUser(User user);
}
