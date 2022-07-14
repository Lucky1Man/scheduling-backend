package com.online.scheduling.schedule.repositories;

import com.online.scheduling.schedule.entities.UserAccount;
import com.online.scheduling.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByOwner_Id(Long id);
    Optional<UserAccount> findByOwner_Email(String email);
}
