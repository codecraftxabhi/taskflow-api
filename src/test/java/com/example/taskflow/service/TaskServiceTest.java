package com.example.taskflow.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.taskflow.dto.TaskRequest;
import com.example.taskflow.dto.TaskResponse;
import com.example.taskflow.exception.TaskNotFoundException;
import com.example.taskflow.model.Task;
import com.example.taskflow.model.TaskPriority;
import com.example.taskflow.model.TaskStatus;
import com.example.taskflow.repository.TaskRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskService service;

    @Test
    void create_appliesDefaultsWhenStatusAndPriorityMissing() {
        when(repository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        TaskResponse response = service.create(new TaskRequest("  Write docs  ", null, null, null, null));

        assertThat(response.title()).isEqualTo("Write docs");
        assertThat(response.status()).isEqualTo(TaskStatus.TODO);
        assertThat(response.priority()).isEqualTo(TaskPriority.MEDIUM);
    }

    @Test
    void get_throwsWhenTaskDoesNotExist() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(99L))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void delete_removesExistingTask() {
        Task task = new Task();
        task.setTitle("Old");
        when(repository.findById(1L)).thenReturn(Optional.of(task));

        service.delete(1L);

        verify(repository).delete(task);
    }
}
