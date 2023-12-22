package ru.yandex.practicum.kanban.models;

import ru.yandex.practicum.kanban.models.enums.Status;
import ru.yandex.practicum.kanban.models.enums.Type;

import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    protected final int epicId;

    public Subtask(Integer epicId, String name, String description) {
        super(name, description);
        this.type = Type.SUBTASK;
        this.epicId = epicId;
    }

    public Subtask(Integer epicId, String name, String description, long duration, LocalDateTime startTime) {
        super(name, description, duration, startTime);
        this.type = Type.SUBTASK;
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return type + " {" +
                "ID= " + id +
                ", epicID= " + epicId +
                ", taskName='" + name + '\'' +
                ", taskDescription='" + description + '\'' +
                ", status=" + status +
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
