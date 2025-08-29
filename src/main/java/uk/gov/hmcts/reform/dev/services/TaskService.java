package uk.gov.hmcts.reform.dev.services;

import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.dev.dtos.CreateTaskDTO;
import uk.gov.hmcts.reform.dev.dtos.TaskResponseDTO;
import uk.gov.hmcts.reform.dev.exception.TaskNotFoundException;
import uk.gov.hmcts.reform.dev.mappers.TaskMapperImpl;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.repositories.TaskRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private TaskRepository taskRepository;
    private TaskMapperImpl taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapperImpl taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    public TaskResponseDTO getTaskById(Long id) {
        Task task = safelyGetTaskById(id);
        return taskMapper.toResponseDTO(task);
    }

    public List<TaskResponseDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
            .map(taskMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    public TaskResponseDTO createTask(CreateTaskDTO dto) {
        return taskMapper.toResponseDTO(taskRepository.save(taskMapper.toEntity(dto)));
    }

    public void deleteTask(Long id) {
        Task task = safelyGetTaskById(id);
        taskRepository.delete(task);
    }

    public TaskResponseDTO updateTaskStatusById(Long id, String status) {
        Task task = safelyGetTaskById(id);
        task.setStatus(status);
        taskRepository.save(task);
        return taskMapper.toResponseDTO(task);
    }

    private Task safelyGetTaskById(Long id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isEmpty()) {
            throw new TaskNotFoundException(id);
        }
        return taskOptional.get();
    }
}
