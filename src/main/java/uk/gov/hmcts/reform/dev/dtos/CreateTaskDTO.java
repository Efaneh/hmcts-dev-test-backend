package uk.gov.hmcts.reform.dev.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
public class CreateTaskDTO {

    @NotBlank
    private String caseNumber;

    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String status;

    @NotNull
    private LocalDateTime dueDate;
}
