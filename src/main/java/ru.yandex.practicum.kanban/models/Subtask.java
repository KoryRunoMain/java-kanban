package ru.yandex.practicum.kanban.models;

import ru.yandex.practicum.kanban.enums.Status;
import ru.yandex.practicum.kanban.enums.Type;

import java.time.Instant;
import java.util.Objects;

public class Subtask extends Task {

    protected final int epicId;

    public Subtask(Integer epicId, String name, String description, long duration, Instant startTime) {
        super(name, description, duration, startTime);
        this.type = Type.SUBTASK;
        this.epicId = epicId;
    }

    public Subtask(Integer epicId, String name, String description, long duration, Instant startTime, Status status) {
        super(name, description, duration, startTime, status);
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
                ", status=" + status + ", start time=" + startTime.toEpochMilli() +
                ", duration=" + duration + ", end time=" + getEndTime().toEpochMilli() + '\'' + '}';
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
