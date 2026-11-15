package com.example.tasktracker.service;

import com.example.tasktracker.dto.TaskRequest;
import com.example.tasktracker.exception.NoTasksSearchFoundException;
import com.example.tasktracker.exception.SerchedTaskNotFoundException;
import com.example.tasktracker.model.Task;
import com.example.tasktracker.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskRequest taskDto;

    @InjectMocks
    private TaskService taskService;

    @ParameterizedTest
    @CsvSource({
            "1, HW, Do HW, true",
            "2, HW2, Do HW2, false",
            "34, HW2, Do HW2, false",
    })
    void updateTask(Long id, String title, String description, boolean done) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setDone(done);
        when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));
        TaskRequest request = new TaskRequest(title, description);


        Task result = taskService.updateTask(id, request);


        assertEquals(title, result.getTitle());
        assertEquals(description, result.getDescription());
        verify(taskRepository).save(task);
    }

    @Test
    void update_whenTaskNotFound_throwsException() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(SerchedTaskNotFoundException.class,
                () -> taskService.updateTask(99L, new TaskRequest("HW", "Do HW")));

        verify(taskRepository, never()).save(any());
    }

    @Test
    void deleteTask() {
        Long id = 1L;
        Task task = new Task();
        when(taskRepository.findById(id)).thenReturn(Optional.of(task));

        taskService.deleteTask(id);

        verify(taskRepository).delete(task);
    }

    @Test
    void deleteTask_whenTaskNotFound_throwsException() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(SerchedTaskNotFoundException.class, () -> taskService.deleteTask(99L));

        verify(taskRepository, never()).delete(any(Task.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"h", "hw", "HW"})
    void searchTasks(String rq) {
        Task task = new Task();
        task.setTitle("HW");
        when(taskRepository.findByTitleContainingIgnoreCase(rq)).thenReturn(List.of(task));

        List<Task> result = taskService.searchTasks(rq);

        assertEquals(1, result.size());
        assertEquals("HW", result.getFirst().getTitle());
        verify(taskRepository).findByTitleContainingIgnoreCase(rq);
    }

    @ParameterizedTest
    @ValueSource(strings = {"zzz"})
    void searchTasks_whenNothingFound_returnsEmptyList(String qr) {
        when(taskRepository.findByTitleContainingIgnoreCase(qr)).thenReturn(List.of());

        assertThrows(NoTasksSearchFoundException.class,
                () -> taskService.searchTasks(qr));
    }

    @Test
    void searchTasks_whenNothingFound_throwsException() {
        String query = "nothing";
        when(taskRepository.findByTitleContainingIgnoreCase(query)).thenReturn(List.of());

        assertThrows(NoTasksSearchFoundException.class, () -> taskService.searchTasks(query));
    }


}