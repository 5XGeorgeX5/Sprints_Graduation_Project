package com.team5.graduation_project.Repository;

import com.team5.graduation_project.Models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
