package com.online.scheduling.schedule.repositories.excluded;

import com.online.scheduling.schedule.entities.todo.PlansContainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlansRepository extends JpaRepository<PlansContainer, Long> {
}
