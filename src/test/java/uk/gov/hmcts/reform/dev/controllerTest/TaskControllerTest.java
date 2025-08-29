package uk.gov.hmcts.reform.dev.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.reform.dev.controllers.TaskController;
import uk.gov.hmcts.reform.dev.dtos.CreateTaskDTO;
import uk.gov.hmcts.reform.dev.dtos.TaskResponseDTO;
import uk.gov.hmcts.reform.dev.dtos.TaskStatusUpdateDTO;
import uk.gov.hmcts.reform.dev.exception.TaskNotFoundException;
import uk.gov.hmcts.reform.dev.services.TaskService;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@TestPropertySource(properties = {"SERVER_PORT=4000"})
@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskService taskService;

    @Test
    void createTaskShouldReturnBadRequestWhenRequestBodyIsNotValid() throws Exception {
        CreateTaskDTO invalidDto = new CreateTaskDTO();
        invalidDto.setDescription("I have a description, but no case number, title, or dueDate!");

        MockHttpServletResponse response = mockMvc.perform(post("/task")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidDto)))
            .andReturn().getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void createTaskShouldReturn200AndTaskDTOWhenCreationSuccessful() throws Exception {
        LocalDateTime dueDate = LocalDateTime.now();
        CreateTaskDTO validDto = new CreateTaskDTO("CASE17828", "Title", "Description", "Status", LocalDateTime.now());
        when(taskService.createTask(any())).thenReturn(
            new TaskResponseDTO(
                1L,"CASE17828", "Title", "Description", "Status", dueDate));

        MockHttpServletResponse response = mockMvc.perform(post("/task")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validDto)))
            .andReturn().getResponse();

        TaskResponseDTO responseDto = objectMapper.readValue(response.getContentAsString(), TaskResponseDTO.class);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(1, responseDto.getId());
        assertEquals("CASE17828", responseDto.getCaseNumber());
        assertEquals("Title", responseDto.getTitle());
        assertEquals("Description", responseDto.getDescription());
        assertEquals("Status", responseDto.getStatus());
        assertEquals(dueDate, responseDto.getDueDate());
    }

    @Test
    void getTaskByIdShouldReturn200AndTaskResponseDTOWhenTaskFound() throws Exception {

        when(taskService.getTaskById(anyLong())).thenReturn(new TaskResponseDTO());

        MockHttpServletResponse response = mockMvc.perform(get("/task/1")
                            .contentType(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void getTaskByIdShouldReturn404WhenTaskNotFound() throws Exception {

        when(taskService.getTaskById(anyLong())).thenThrow(new TaskNotFoundException(1L));

        MockHttpServletResponse response = mockMvc.perform(get("/task/1")
                        .contentType(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals("Task with ID 1 could not be found!", response.getContentAsString());
    }

    @Test
    void getAllTasksShouldReturnListOfTaskResponseDTOsWhenZeroOrMoreTasksFound() throws Exception {

        when(taskService.getAllTasks()).thenReturn(new ArrayList<TaskResponseDTO>());

        MockHttpServletResponse response = mockMvc.perform(get("/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void deleteTaskByIdShouldReturn204WhenTaskSuccessfullyDeleted() throws Exception {
        doNothing().when(taskService).deleteTask(anyLong());

        MockHttpServletResponse response = mockMvc.perform(delete("/task/1")
                        .contentType(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    void deleteTaskByIdShouldReturn404WhenTaskNotFound() throws Exception {
        doThrow(new TaskNotFoundException(1L)).when(taskService).deleteTask(anyLong());

        MockHttpServletResponse response = mockMvc.perform(delete("/task/1")
                        .contentType(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }


    @Test
    void updateTaskStatusShouldReturn200WhenValidBodyPassedAndTaskFound() throws Exception {

        when(taskService.updateTaskStatusById(1L, "ExampleStatus")).thenReturn(new TaskResponseDTO());

        MockHttpServletResponse response = mockMvc.perform(put("/task/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TaskStatusUpdateDTO("ExampleStatus"))))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void updateTaskStatusShouldReturn404WhenTaskNotFound() throws Exception {

        when(taskService.updateTaskStatusById(1L, "ExampleStatus")).thenThrow(new TaskNotFoundException(1L));

        MockHttpServletResponse response = mockMvc.perform(put("/task/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TaskStatusUpdateDTO("ExampleStatus"))))
            .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    void updateTaskStatusShouldReturn400WhenBlankStatusPassedInBody() throws Exception {

        MockHttpServletResponse response = mockMvc.perform(put("/task/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TaskStatusUpdateDTO(""))))
            .andReturn().getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }
}
