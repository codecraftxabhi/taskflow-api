package com.example.taskflow.dto;

import com.example.taskflow.model.TaskPriority;
import com.example.taskflow.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record TaskRequest(
        @NotBlank(message = "title must not be blank")
        @Size(max = 150, message = "title must be at most 150 characters")
        String title,

        @Size(max = 2000, message = "description must be at most 2000 characters")
        String description,

        TaskStatus status,

        TaskPriority priority,

        LocalDate dueDate
) {
}
