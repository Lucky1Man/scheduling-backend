package com.online.scheduling.schedule.repositories;

import com.online.scheduling.schedule.models.PlannedDayContainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlannedDayRepository extends JpaRepository<PlannedDayContainer, Long> {
}
