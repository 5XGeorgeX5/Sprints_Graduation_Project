package com.team5.graduation_project.Repository;

import com.team5.graduation_project.Models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);

    boolean existsByName(String name);
}
