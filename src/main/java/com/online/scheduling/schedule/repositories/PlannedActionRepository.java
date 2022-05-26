package com.online.scheduling.schedule.repositories;

import com.online.scheduling.schedule.models.PlannedActionContainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlannedActionRepository extends JpaRepository<PlannedActionContainer, Long> {
}
