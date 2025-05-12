package com.example.task_manger.tasks.request;

import com.example.task_manger.tasks.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class TaskRequest {
    @NotBlank(message = "Title is required")
    @Size(min = 3,max = 80,message = "title must be at least 3 and at most 45 character")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Status is required")
    private TaskStatus status;

    @NotNull(message = "Due date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    public TaskRequest(String title, String description, TaskStatus status, LocalDate dueDate) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
    }

    public @NotBlank(message = "Title is required") @Size(min = 3, max = 80, message = "title must be at least 3 and at most 45 character") String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank(message = "Title is required") @Size(min = 3, max = 80, message = "title must be at least 3 and at most 45 character") String title) {
        this.title = title;
    }

    public  String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public @NotNull(message = "Due date is required") LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(@NotNull(message = "Due date is required") LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
