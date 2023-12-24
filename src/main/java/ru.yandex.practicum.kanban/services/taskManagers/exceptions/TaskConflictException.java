package ru.yandex.practicum.kanban.services.taskManagers.exceptions;

public class TaskConflictException extends RuntimeException{
    public TaskConflictException(final String message) {
        super(message);
    }
}
