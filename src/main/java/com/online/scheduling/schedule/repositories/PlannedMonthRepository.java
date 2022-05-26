package com.online.scheduling.schedule.repositories;

import com.online.scheduling.schedule.models.PlannedMonthContainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlannedMonthRepository extends JpaRepository<PlannedMonthContainer,Long> {
}
