package com.example.tasktracker.exception;

public class SerchedTaskNotFoundException extends RuntimeException{
    public SerchedTaskNotFoundException(Long id){
        super("Задача с id " + id + " не найдена");
    }
}
