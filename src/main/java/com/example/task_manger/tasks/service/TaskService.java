package com.example.task_manger.tasks.service;
import com.example.task_manger.config.UserContext;
import com.example.task_manger.tasks.model.Task;
import com.example.task_manger.tasks.repository.TaskRepository;
import com.example.task_manger.tasks.request.TaskRequest;
import com.example.task_manger.tasks.response.TaskResponse;
import com.example.task_manger.util.exceptions.TaskNotFoundException;
import com.example.task_manger.util.mapper.ClassMapper;
import jakarta.ws.rs.ForbiddenException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Page<Task> getTasksForUser(int page,int size) {
        String userId = UserContext.getUserId();
        System.out.println(userId);
        Pageable pageable = PageRequest.of(page, size);
        return taskRepository.findByOwnerId(pageable, userId);
    }


    public TaskResponse getTaskByIdAndOwner(Long id) {
        String ownerId = UserContext.getUserId();
        System.out.println(ownerId);
        Task task = taskRepository.findById(id).orElse(null);
        if(task!=null){
        if (!Objects.equals(task.getOwnerId(), ownerId)) {
            throw new ForbiddenException("You are not the owner of this task");
        }
        }
        if (task != null && task.getOwnerId().equals(ownerId)) {
            return ClassMapper.INSTANCE.entityToDto(task);
        }
        throw new TaskNotFoundException("There is no task with id: " + id);

    }

    public TaskResponse createTask(TaskRequest request) {
        String ownerId = UserContext.getUserId();

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setDueDate(request.getDueDate());
        task.setOwnerId(ownerId);
        taskRepository.save(task);
        return ClassMapper.INSTANCE.entityToDto(task);
    }

    public TaskResponse updateTask(Long id, TaskRequest updatedTask) {
        String ownerId = UserContext.getUserId();
        if (taskRepository.existsById(id)) {
            Task task = taskRepository.findById(id).get();
            if (!Objects.equals(task.getOwnerId(), ownerId)) {
                throw new TaskNotFoundException("You are not the owner of this task");
            }
            if (task.getOwnerId().equals(ownerId)) {
                task.setTitle(updatedTask.getTitle());
                task.setDescription(updatedTask.getDescription());
                task.setStatus(updatedTask.getStatus());
                task.setDueDate(updatedTask.getDueDate());
                taskRepository.saveAndFlush(task);
                return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), task.getDueDate());
            }
        }
        throw new TaskNotFoundException(
                "There is no task with id: " + id);
    }

    public String deleteTask(Long id) {
        String ownerId = UserContext.getUserId();
        System.out.println(ownerId);
        if (taskRepository.existsById(id)) {
            Task task = taskRepository.findById(id).get();
            if (!Objects.equals(task.getOwnerId(), ownerId)) {
                throw new TaskNotFoundException("You are not the owner of this task");
            }
            taskRepository.deleteById(id);
            return "Task deleted Successfully";
        } else {
            throw new TaskNotFoundException("Not found task with id = " + id);
        }
    }




}