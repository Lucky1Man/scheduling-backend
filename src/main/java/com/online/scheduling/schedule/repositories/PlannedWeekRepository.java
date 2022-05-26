package com.online.scheduling.schedule.repositories;

import com.online.scheduling.schedule.models.PlannedWeek;
import com.online.scheduling.schedule.models.PlannedWeekContainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlannedWeekRepository extends JpaRepository<PlannedWeekContainer, Long> {
}
