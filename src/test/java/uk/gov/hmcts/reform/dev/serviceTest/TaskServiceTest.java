package uk.gov.hmcts.reform.dev.serviceTest;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;
import uk.gov.hmcts.reform.dev.dtos.CreateTaskDTO;
import uk.gov.hmcts.reform.dev.dtos.TaskResponseDTO;
import uk.gov.hmcts.reform.dev.exception.TaskNotFoundException;
import uk.gov.hmcts.reform.dev.mappers.TaskMapperImpl;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.repositories.TaskRepository;
import uk.gov.hmcts.reform.dev.services.TaskService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;

@TestPropertySource(properties = {"SERVER_PORT=4000"})
@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapperImpl taskMapper;

    @InjectMocks
    private TaskService taskService;

    @Test
    void getTaskByIdShouldReturnDTOWhenTaskFound() {
        Task task = new Task();
        TaskResponseDTO dto = new TaskResponseDTO();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.toResponseDTO(task)).thenReturn(dto);

        TaskResponseDTO result = taskService.getTaskById(1L);

        assertEquals(dto, result);
    }

    @Test
    void getTaskByIdShouldThrowExceptionWhenTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(1L));
    }

    @Test
    void getAllTasksShouldReturnMappedDTOList() {
        Task task = new Task();
        TaskResponseDTO dto = new TaskResponseDTO();
        when(taskRepository.findAll()).thenReturn(List.of(task));
        when(taskMapper.toResponseDTO(task)).thenReturn(dto);

        List<TaskResponseDTO> result = taskService.getAllTasks();

        assertEquals(1, result.size());
        assertEquals(dto, result.getFirst());
    }

    @Test
    void createTaskShouldSaveMappedEntityAndReturnDTO() {
        CreateTaskDTO createDto = new CreateTaskDTO("CASE123", "Title", "Desc", "Open", LocalDateTime.now());
        Task task = new Task();
        TaskResponseDTO dto = new TaskResponseDTO();

        when(taskMapper.toEntity(createDto)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponseDTO(task)).thenReturn(dto);

        TaskResponseDTO result = taskService.createTask(createDto);

        assertEquals(dto, result);
    }

    @Test
    void deleteTaskShouldCallDeleteWhenTaskExists() {
        Task task = new Task();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.deleteTask(1L);

        verify(taskRepository).delete(task);
    }

    @Test
    void deleteTaskShouldThrowExceptionWhenTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(1L));
    }

    @Test
    void updateTaskStatusShouldSaveAndReturnDTOWhenTaskFound() {
        Task task = spy(new Task());
        TaskResponseDTO dto = new TaskResponseDTO();
        String newStatus = "newStatus";

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponseDTO(task)).thenReturn(dto);

        TaskResponseDTO result = taskService.updateTaskStatusById(1L, newStatus);

        verify(task).setStatus(newStatus);
        assertEquals(dto, result);
    }

    @Test
    void updateTaskStatusShouldThrowExceptionWhenTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.updateTaskStatusById(1L, "InProgress"));
    }
}
