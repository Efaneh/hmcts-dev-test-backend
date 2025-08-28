package uk.gov.hmcts.reform.dev.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long id) {
        super(String.format("Task with ID %s could not be found!", id));
    }
}
