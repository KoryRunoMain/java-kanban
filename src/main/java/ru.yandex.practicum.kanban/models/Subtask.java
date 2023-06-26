package ru.yandex.practicum.kanban.models;

import java.util.Objects;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String taskName, String taskDescription, int taskId, Status status, int epicId) {
        super(taskName, taskDescription);
        this.type = Type.SUBTASK;
        this.status = status;
        this.taskId = taskId;
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskId=" + taskId +
                ", status=" + status +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }
}
