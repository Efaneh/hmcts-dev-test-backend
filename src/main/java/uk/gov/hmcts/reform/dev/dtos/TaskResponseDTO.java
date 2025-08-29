package uk.gov.hmcts.reform.dev.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDTO {

    @NotNull
    private Long id;

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
