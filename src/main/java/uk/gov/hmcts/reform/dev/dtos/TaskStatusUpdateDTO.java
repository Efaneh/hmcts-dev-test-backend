package uk.gov.hmcts.reform.dev.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class TaskStatusUpdateDTO {
    @NotBlank
    private String status;
}
