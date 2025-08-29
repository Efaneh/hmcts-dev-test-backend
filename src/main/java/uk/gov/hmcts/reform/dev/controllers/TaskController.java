package uk.gov.hmcts.reform.dev.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.dev.dtos.CreateTaskDTO;
import uk.gov.hmcts.reform.dev.dtos.TaskResponseDTO;
import uk.gov.hmcts.reform.dev.dtos.TaskStatusUpdateDTO;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.services.TaskService;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
public class TaskController {
    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping(value = "/task/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @GetMapping(value = "/tasks")
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());

    }

    @PostMapping(value = "/task")
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody @Valid CreateTaskDTO createDto) {
        return ResponseEntity.ok(taskService.createTask(createDto));
    }

    @DeleteMapping(value = "task/{id}")
    public ResponseEntity deleteTaskById(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    //This should be PATCH as a partial update, but I didn't want to delve into Spring Security for a simple app (PATCH disabled by default)
    @PutMapping(value = "task/{id}")
    public ResponseEntity<TaskResponseDTO> updateTaskStatus (@RequestBody @Valid TaskStatusUpdateDTO updateDto,
                                                             @PathVariable Long id) {
        return ResponseEntity.ok(taskService.updateTaskStatusById(id, updateDto.getStatus()));

    }
}
