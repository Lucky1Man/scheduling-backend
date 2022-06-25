package com.online.scheduling.schedule.repositories.excluded;

import com.online.scheduling.schedule.entities.todo.PlannedWeekContainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlannedWeekRepository extends JpaRepository<PlannedWeekContainer, Long> {
}
