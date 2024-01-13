package ru.yandex.practicum.kanban.exceptions;

public class TaskConflictException extends RuntimeException{

    public TaskConflictException(final String message) {
        super(message);
    }
}
