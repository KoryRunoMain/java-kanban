package ru.yandex.practicum.kanban.exceptions;

public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException(final String message) {
        super(message);
    }

}
