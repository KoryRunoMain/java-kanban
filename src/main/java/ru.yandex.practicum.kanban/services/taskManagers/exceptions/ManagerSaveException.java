package ru.yandex.practicum.kanban.services.taskManagers.exceptions;

public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException(final String message) {
        super(message);
    }

}
