package com.task.organiser.service;

import com.task.organiser.entity.Comment;
import com.task.organiser.entity.Task;
import com.task.organiser.repository.CommentRepository;
import com.task.organiser.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTests {
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private Comment comment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        task = new Task();
        task.setId(1L);
        task.setStatus("Open");

        comment = new Comment();
        comment.setComment("This is a test comment");
        comment.setCreatedDate(LocalDateTime.now());
        comment.setTask(task);
    }

    @Test
    void createTask_ShouldSaveTask() {
        when(taskRepository.save(task)).thenReturn(task);

        Task createdTask = taskService.createTask(task);

        assertNotNull(createdTask);
        assertEquals(task.getId(), createdTask.getId());
        verify(taskRepository).save(task);
    }

    @Test
    void getTaskById_ShouldReturnTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task foundTask = taskService.getTaskById(1L);

        assertNotNull(foundTask);
        assertEquals(task.getId(), foundTask.getId());
    }

    @Test
    void getTaskById_ShouldReturnNullIfNotFound() {
        when(taskRepository.findById(2L)).thenReturn(Optional.empty());

        Task foundTask = taskService.getTaskById(2L);

        assertNull(foundTask);
    }

    @Test
    void addCommentToTask_ShouldAddComment() {
        task.setCommentList(new ArrayList<>());

        comment.setTask(task);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(taskRepository.save(task)).thenReturn(task);

        Task updatedTask = taskService.addCommentToTask(1L, comment.getComment());

        assertNotNull(updatedTask);
        assertEquals(1, updatedTask.getCommentList().size());
        verify(commentRepository).save(any(Comment.class));
    }



    @Test
    void addCommentToTask_ShouldThrowExceptionIfTaskNotFound() {
        when(taskRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            taskService.addCommentToTask(2L, comment.getComment());
        });
    }

    @Test
    void transitionTask_ShouldUpdateTaskStatus() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        Task updatedTask = taskService.transitionTask(1L, "In Progress");
        assertNotNull(updatedTask);
        assertEquals("In Progress", updatedTask.getStatus());
        verify(taskRepository).save(task);
    }

    @Test
    void transitionTask_ShouldThrowExceptionIfTaskNotFound() {
        when(taskRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> {
            taskService.transitionTask(2L, "In Progress");
        });
    }

    @Test
    void deleteTask_ShouldRemoveTask() {
        doNothing().when(taskRepository).deleteById(1L);
        taskService.deleteTask(1L);
        verify(taskRepository).deleteById(1L);
    }

    @Test
    void deleteTask_ShouldNotThrowExceptionIfTaskDoesNotExist() {
        doNothing().when(taskRepository).deleteById(2L);
        assertDoesNotThrow(() -> taskService.deleteTask(2L));
        verify(taskRepository).deleteById(2L);
    }
}
