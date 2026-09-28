package com.example.taskflow.service;

import com.example.taskflow.dto.TaskRequest;
import com.example.taskflow.dto.TaskResponse;
import com.example.taskflow.exception.TaskNotFoundException;
import com.example.taskflow.model.Task;
import com.example.taskflow.model.TaskPriority;
import com.example.taskflow.model.TaskStatus;
import com.example.taskflow.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public TaskResponse create(TaskRequest request) {
        Task task = new Task();
        apply(task, request);
        return TaskResponse.from(repository.save(task));
    }

    public TaskResponse get(Long id) {
        return TaskResponse.from(find(id));
    }

    public Page<TaskResponse> list(TaskStatus status, Pageable pageable) {
        Page<Task> page = (status == null)
                ? repository.findAll(pageable)
                : repository.findByStatus(status, pageable);
        return page.map(TaskResponse::from);
    }

    @Transactional
    public TaskResponse update(Long id, TaskRequest request) {
        Task task = find(id);
        apply(task, request);
        return TaskResponse.from(repository.save(task));
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(find(id));
    }

    private Task find(Long id) {
        return repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    private void apply(Task task, TaskRequest request) {
        task.setTitle(request.title().trim());
        task.setDescription(request.description());
        task.setStatus(request.status() != null ? request.status() : TaskStatus.TODO);
        task.setPriority(request.priority() != null ? request.priority() : TaskPriority.MEDIUM);
        task.setDueDate(request.dueDate());
    }
}
