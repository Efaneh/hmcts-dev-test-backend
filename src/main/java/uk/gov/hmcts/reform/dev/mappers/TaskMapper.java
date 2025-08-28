package uk.gov.hmcts.reform.dev.mappers;

import uk.gov.hmcts.reform.dev.dtos.CreateTaskDTO;
import uk.gov.hmcts.reform.dev.dtos.TaskResponseDTO;
import uk.gov.hmcts.reform.dev.models.Task;

public interface TaskMapper {
    Task toEntity(CreateTaskDTO dto);
    TaskResponseDTO toResponseDTO(Task taskEntity);
}
