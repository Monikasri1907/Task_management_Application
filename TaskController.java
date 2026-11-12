package com.monika.taskmanager.controller;

import com.monika.taskmanager.model.Task;
import com.monika.taskmanager.repository.TaskRepository;
import com.monika.taskmanager.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskRepository tasks;
    private final UserRepository users;

    public TaskController(TaskRepository tasks, UserRepository users) {
        this.tasks = tasks;
        this.users = users;
    }

    public record TaskRequest(@NotBlank String title, String description,
                              String status, LocalDate dueDate) {}

    public record TaskResponse(Long id, String title, String description,
                               String status, LocalDate dueDate) {
        static TaskResponse from(Task task) {
            return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(),
                    task.getStatus(), task.getDueDate());
        }
    }

    @GetMapping
    public List<TaskResponse> all(Authentication auth) {
        return tasks.findAllByUserUsernameOrderByIdDesc(auth.getName())
                .stream().map(TaskResponse::from).toList();
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody TaskRequest request, Authentication auth) {
        Task task = new Task();
        apply(task, request);
        task.setUser(users.findByUsername(auth.getName()).orElseThrow());
        return ResponseEntity.status(HttpStatus.CREATED).body(TaskResponse.from(tasks.save(task)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody TaskRequest request,
                                    Authentication auth) {
        return tasks.findByIdAndUserUsername(id, auth.getName())
                .map(task -> {
                    apply(task, request);
                    return ResponseEntity.ok(TaskResponse.from(tasks.save(task)));
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication auth) {
        return tasks.findByIdAndUserUsername(id, auth.getName())
                .map(task -> {
                    tasks.delete(task);
                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    private void apply(Task task, TaskRequest request) {
        task.setTitle(request.title().trim());
        task.setDescription(request.description() == null ? "" : request.description().trim());
        String status = request.status() == null ? "PENDING" : request.status().toUpperCase();
        if (!List.of("PENDING", "IN_PROGRESS", "COMPLETED").contains(status)) {
            throw new IllegalArgumentException("Status must be PENDING, IN_PROGRESS, or COMPLETED");
        }
        task.setStatus(status);
        task.setDueDate(request.dueDate());
    }
}
