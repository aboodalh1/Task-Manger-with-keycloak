package com.example.task_manger;

import com.example.task_manger.config.UserContext;
import com.example.task_manger.tasks.model.Task;
import com.example.task_manger.tasks.repository.TaskRepository;
import com.example.task_manger.tasks.service.TaskService;
import com.example.task_manger.util.exceptions.TaskNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        // Simulate logged in user
        UserContext.setUserId("test-user-id");
    }

    @Test
    void getTasksForUser_ShouldReturnPagedTasks() {
        Task task = new Task();
        task.setOwnerId("test-user-id");
        Page<Task> taskPage = new PageImpl<>(List.of(task));

        when(taskRepository.findByOwnerId(any(Pageable.class), eq("test-user-id")))
            .thenReturn(taskPage);

        Page<Task> result = taskService.getTasksForUser(0, 5);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void deleteTask_ShouldDeleteOwnedTask() {
        Task task = new Task();
        task.setId(1L);
        task.setOwnerId("test-user-id");

        when(taskRepository.existsById(1L)).thenReturn(true);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        String response = taskService.deleteTask(1L);

        assertEquals("Task deleted Successfully", response);
        verify(taskRepository).deleteById(1L);
    }

    @Test
    void deleteTask_ShouldThrowIfNotOwner() {
        Task task = new Task();
        task.setId(1L);
        task.setOwnerId("another-user");

        when(taskRepository.existsById(1L)).thenReturn(true);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(1L));
    }
}
