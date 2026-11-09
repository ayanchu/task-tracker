package com.example.tasktracker.exception;

public class NoTasksSearchFoundException extends RuntimeException {
    public NoTasksSearchFoundException(String message) {

        super("В поиске по запросу: " + message + " не найдено ни одной задачи");
    }
}
