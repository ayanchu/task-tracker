package com.example.tasktracker.service;

import com.example.tasktracker.dto.TaskRequest;
import com.example.tasktracker.exception.NoTasksSearchFoundException;
import com.example.tasktracker.exception.TaskNotFoundException;
import com.example.tasktracker.model.Task;
import com.example.tasktracker.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> findTasks(Boolean status){
        if(status == null){
            return repository.findAll();
        }
        return repository.findByDone(status);
    }

    public Task findById(Long id){
        return repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task createTask(TaskRequest req){
        Task task = new Task();
        task.setTitle(req.title());
        task.setDescription(req.description());
        return repository.save(task);
    }

    public Task updateTask(Long id, TaskRequest req){
        Task task = findById(id);
        task.setTitle(req.title());
        task.setDescription(req.description());
        return repository.save(task);
    }

    public Task toggleTask(Long id){
        Task task = findById(id);
        task.setDone(!task.isDone());
        return repository.save(task);
    }

    public void deleteTask(Long id){
        Task task = findById(id);
        repository.delete(task);
    }

    public List<Task> searchTasks(String query) {

        if (query == null || query.isBlank()) {
            return Collections.emptyList();
        }

        List<Task> list = repository.findByTitleContainingIgnoreCase(query);
        if(list.isEmpty()){
            throw new NoTasksSearchFoundException(query);
        }
        return list;
    }
}
