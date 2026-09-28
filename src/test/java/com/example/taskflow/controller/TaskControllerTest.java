package com.example.taskflow.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.taskflow.dto.TaskRequest;
import com.example.taskflow.dto.TaskResponse;
import com.example.taskflow.exception.TaskNotFoundException;
import com.example.taskflow.model.TaskPriority;
import com.example.taskflow.model.TaskStatus;
import com.example.taskflow.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService service;

    @Test
    void create_returns201WithLocation() throws Exception {
        TaskResponse saved = new TaskResponse(1L, "Ship it", null, TaskStatus.TODO,
                TaskPriority.HIGH, null, Instant.now(), Instant.now());
        when(service.create(any(TaskRequest.class))).thenReturn(saved);

        TaskRequest body = new TaskRequest("Ship it", null, null, TaskPriority.HIGH, null);

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/v1/tasks/1"))
                .andExpect(jsonPath("$.title").value("Ship it"));
    }

    @Test
    void create_returns400WhenTitleBlank() throws Exception {
        TaskRequest body = new TaskRequest(" ", null, null, null, null);

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.title").exists());
    }

    @Test
    void get_returns404WhenMissing() throws Exception {
        when(service.get(42L)).thenThrow(new TaskNotFoundException(42L));

        mockMvc.perform(get("/api/v1/tasks/42"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Task not found"));
    }
}
