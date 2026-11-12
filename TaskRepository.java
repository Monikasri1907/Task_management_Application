package com.monika.taskmanager.repository;

import com.monika.taskmanager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUserUsernameOrderByIdDesc(String username);
    Optional<Task> findByIdAndUserUsername(Long id, String username);
}
