package com.example.tasktracker.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.tasktracker.exception.SerchedTaskNotFoundException;
import com.example.tasktracker.model.Task;
import com.example.tasktracker.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Test
    void testGetAllTasks() throws Exception{
        when(taskService.findTasks(null)).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
        verify(taskService, times(1)).findTasks(null);
    }

    @Test
    void testGetAllTasksByDone() throws Exception{
        Task task = new Task();
        task.setTitle("HW");
        task.setDone(true);

        when(taskService.findTasks(true)).thenReturn(List.of(task));

        mockMvc.perform(get("/api/tasks").param("done", "true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("HW"))
                .andExpect(jsonPath("$[0].done").value(true));
    }

    @Test
    void testDeleteTask() throws Exception{
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());
        verify(taskService, times(1)).deleteTask(1L);
    }

    @Test
    void testDeleteTaskNotFound() throws Exception{
        doThrow(new SerchedTaskNotFoundException(99L)).when(taskService).deleteTask(99L);

        mockMvc.perform(delete("/api/tasks/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Задача с id 99 не найдена"));

        verify(taskService, times(1)).deleteTask(99L);
    }

}
