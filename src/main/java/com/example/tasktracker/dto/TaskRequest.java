package com.example.tasktracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskRequest (
    @NotBlank(message = "Название обязательно")
    @Size(max=100, message = "Максимум 100 символов")
    String title,
    String description
){}
