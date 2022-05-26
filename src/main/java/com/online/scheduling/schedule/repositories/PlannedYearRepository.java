package com.online.scheduling.schedule.repositories;

import com.online.scheduling.schedule.models.PlannedYearContainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlannedYearRepository extends JpaRepository<PlannedYearContainer,Long> {
}
