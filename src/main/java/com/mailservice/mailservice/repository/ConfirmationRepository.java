package com.mailservice.mailservice.repository;

import com.mailservice.mailservice.domain.Confirmation;
import com.mailservice.mailservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ConfirmationRepository extends JpaRepository<Confirmation, Long> {
    Confirmation findByToken(String token);
    Confirmation findByUser(User user);
}