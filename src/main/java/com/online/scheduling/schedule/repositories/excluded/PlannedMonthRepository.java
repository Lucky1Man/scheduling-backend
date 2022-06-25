package com.online.scheduling.schedule.repositories.excluded;

import com.online.scheduling.schedule.entities.todo.PlannedMonthContainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlannedMonthRepository extends JpaRepository<PlannedMonthContainer,Long> {
}
