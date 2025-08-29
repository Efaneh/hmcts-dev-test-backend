package uk.gov.hmcts.reform.dev.mappers;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.dev.dtos.CreateTaskDTO;
import uk.gov.hmcts.reform.dev.dtos.TaskResponseDTO;
import uk.gov.hmcts.reform.dev.models.Task;

@Component
public class TaskMapperImpl implements TaskMapper {

    @Override
    public Task toEntity(CreateTaskDTO dto) {
        Task taskEntity = new Task();
        taskEntity.setCaseNumber(dto.getCaseNumber());
        taskEntity.setTitle(dto.getTitle());
        taskEntity.setDescription(dto.getDescription());
        taskEntity.setDueDate(dto.getDueDate());
        return taskEntity;
    }

    @Override
    public TaskResponseDTO toResponseDTO(Task taskEntity) {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(taskEntity.getId());
        dto.setCaseNumber(taskEntity.getCaseNumber());
        dto.setTitle(taskEntity.getTitle());
        dto.setDescription(taskEntity.getDescription());
        dto.setStatus(taskEntity.getStatus());
        dto.setDueDate(taskEntity.getDueDate());
        return dto;
    }
}
