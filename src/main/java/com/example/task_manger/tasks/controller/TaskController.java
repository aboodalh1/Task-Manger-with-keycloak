package com.example.task_manger.tasks.controller;

import com.example.task_manger.tasks.model.Task;
import com.example.task_manger.tasks.request.TaskRequest;
import com.example.task_manger.tasks.response.TaskResponse;
import com.example.task_manger.tasks.service.TaskService;
import com.example.task_manger.users.request.RegisterRequest;
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
@RequestMapping("/api/v1/tasks")
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
                                    schema = @Schema(implementation = MyAPIResponse.class)

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


    @Operation(
            summary = "get specific task by its id",
            description = "Get specific task by its id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved task",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MyAPIResponse.class),
                                    examples = @ExampleObject(
                                            value = "{ \"message\": \"Get specific task\"," +
                                                    " \"status\": \"OK\"," +
                                                    " \"body\": { \"id\": \"1\", \"title\": \"Wake up\", \"description\": \"Wake up at 7 oclock\", \"status\": \"PENDING\",  } }"
                                    )
                            )
                    )
            }
    )
    // Get specific task
    @GetMapping("/{id}")
    public ResponseEntity<MyAPIResponse<TaskResponse>> getTask(@PathVariable Long id) {
        return ResponseEntity.ok(
                new MyAPIResponse<>(
                        true, 200, taskService.getTaskByIdAndOwner(id)));
    }

    @Operation(
            summary = "update existing task",
            description = "Update existing task,the status of the task. Possible values: PENDING, IN_PROGRESS, COMPLETED ",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegisterRequest.class),
                            examples = @ExampleObject(
                                    value = " { \"title\": \"Wake up\", \"description\": \"Wake up at 7 oclock\", \"dueDate\":\"2024-02-01\", \"status\": \"PENDING\" }"

                            )))
    )
    // Update existing task
    @PutMapping("/{id}")
    public ResponseEntity<MyAPIResponse<TaskResponse>> updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequest updatedTask) {
        return ResponseEntity.ok(
                new MyAPIResponse<>(
                        true, 200, taskService.updateTask(id, updatedTask)));
    }

    @Operation(
            summary = "create new task",
            description = "Create new task, the status of the task. Possible values: PENDING, IN_PROGRESS, COMPLETED ",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegisterRequest.class),
                            examples = @ExampleObject(
                                    value = "{ \"message\": \"Get all Tasks\"," +
                                            " \"status\": \"OK\"," +
                                            " \"body\": { \"tasks\": [ { \"id\": \"1\", \"title\": \"Wake up\", \"description\": \"Wake up at 7 oclock\", \"status\": \"PENDING\",  } ] } }"
                            )
                    )
            ),

            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Successfully created task",
                            content = @Content(

                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MyAPIResponse.class),
                                    examples = @ExampleObject(
                                            value = "{ \"message\": \"Create new task\"," +
                                                    " \"status\": \"OK\"," +
                                                    " \"body\": { \"id\": \"1\", \"title\": \"Wake up\", \"dueDate\":\"2024-02-01\",\"description\": \"Wake up at 7 oclock\", \"status\": \"PENDING\",  } }"
                                    )
                            )
                    )
            }
    )
    // Create new task
    @PostMapping
    public ResponseEntity<MyAPIResponse<TaskResponse>> createTask(@Valid @RequestBody TaskRequest task) {
        return ResponseEntity.ok(
                new MyAPIResponse<>(
                        true, 201, taskService.createTask(task)));
    }
    @Operation(
            summary = "delete existing task",
            description = "Delete existing task",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully deleted task",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MyAPIResponse.class)

                            )
                    )
            }
    )
    // Delete task
    @DeleteMapping("/{id}")
    public MyAPIResponse<String> deleteTask(@PathVariable Long id) {
        return new MyAPIResponse<>(true, 200, taskService.deleteTask(id));
    }


}