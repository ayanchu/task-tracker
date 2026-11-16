package com.example.tasktracker.controller;

import com.example.tasktracker.dto.TaskRequest;
import com.example.tasktracker.model.Task;
import com.example.tasktracker.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService service;

    public TaskController(TaskService taskService) {
        this.service = taskService;
    }

    @Tag(name = "get")
    @Operation(summary = "Get all tasks",
    description = "Returns a list of all tasks")
    @GetMapping
    public List<Task> getAll(@RequestParam(required = false) Boolean done){
        return service.findTasks(done);
    }

    @Tag(name = "get", description = "Retrieves a task by its ID")
    @GetMapping("/{id}")
    public Task getById(@PathVariable Long id){
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(
            @Valid @RequestBody TaskRequest req){
        return service.createTask(req);
    }

    @PutMapping("/{id}")
    public Task updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest req
            ){
        return service.updateTask(id, req);
    }

    @PatchMapping("/{id}/toggle")
    public Task toggleTask(@PathVariable Long id){
        return service.toggleTask(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id){
        service.deleteTask(id);
    }

    @Tag(name = "get", description = "Searches for tasks based on a query")
    @GetMapping("/search")
    public List<Task> searchTasks(@RequestParam String query){
        return service.searchTasks(query);
    }

}
