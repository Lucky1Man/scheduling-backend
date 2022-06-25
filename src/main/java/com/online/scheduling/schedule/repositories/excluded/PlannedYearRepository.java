package com.online.scheduling.schedule.repositories.excluded;

import com.online.scheduling.schedule.entities.todo.PlannedYearContainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlannedYearRepository extends JpaRepository<PlannedYearContainer,Long> {
}
