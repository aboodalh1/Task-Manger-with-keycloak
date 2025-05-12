package com.example.task_manger.tasks.controller;

import com.example.task_manger.tasks.model.Task;
import com.example.task_manger.tasks.request.TaskRequest;
import com.example.task_manger.tasks.response.TaskResponse;
import com.example.task_manger.tasks.service.TaskService;
import com.example.task_manger.util.response.MyAPIResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Manage tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    @Operation(
            summary = "Get All Tasks",
            description = "Retrieve all tasks for a specific user.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved books",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MyAPIResponse.class),
                                    examples = @ExampleObject(
                                            value = "{ \"message\": \"Get all books\"," +
                                                    " \"status\": \"OK\"," +
                                                    " \"body\": { \"tasks\": [ { \"id\": \"1\", \"title\": \"Wake up\", \"description\": \"Wake up at 7 oclock\", \"status\": \"PENDING\",  } ] } }"
                                    )
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<MyAPIResponse<Page<Task>>> getTasks(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(
                new MyAPIResponse<>(
                        true, 200, taskService.getTasksForUser(page, size)));
    }

    // Get specific task
    @GetMapping("/{id}")
    public ResponseEntity<MyAPIResponse<TaskResponse>> getTask(@PathVariable Long id) {
        return ResponseEntity.ok(
                new MyAPIResponse<>(
                        true, 200, taskService.getTaskByIdAndOwner(id)));
    }

    // Create new task
    @PostMapping
    public ResponseEntity<MyAPIResponse<TaskResponse>> createTask(@Valid @RequestBody TaskRequest task) {
        return ResponseEntity.ok(
                new MyAPIResponse<>(
                        true, 201, taskService.createTask(task)));
    }

    // Update existing task
    @PutMapping("/{id}")
    public ResponseEntity<MyAPIResponse<TaskResponse>> updateTask(@PathVariable Long id,@Valid @RequestBody TaskRequest updatedTask) {
        return ResponseEntity.ok(
                new MyAPIResponse<>(
                        true, 200, taskService.updateTask(id, updatedTask)));
    }

    // Delete task
    @DeleteMapping("/{id}")
    public MyAPIResponse<String> deleteTask(@PathVariable Long id) {
        return new MyAPIResponse<>(true, 200, taskService.deleteTask(id));
    }


}