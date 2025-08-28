package uk.gov.hmcts.reform.dev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tasks", schema = "core")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String caseNumber;
    private String title;
    private String description;
    private String status;
    @Column(updatable = false)
    private LocalDateTime createdDate;
    private LocalDateTime dueDate;
}
