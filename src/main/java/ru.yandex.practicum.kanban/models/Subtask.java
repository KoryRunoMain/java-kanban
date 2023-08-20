package ru.yandex.practicum.kanban.models;

import ru.yandex.practicum.kanban.models.enums.Status;
import ru.yandex.practicum.kanban.models.enums.Type;

import javax.xml.stream.events.StartDocument;
import java.util.Objects;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(Integer epicId, String taskName, String taskDescription, Status status) {
        super(taskName, taskDescription, status);
        this.epicId = epicId;
        this.type = Type.SUBTASK;
    }

//    public Subtask(Integer epicId, String taskName, String taskDescription, Status status) {
//        super(taskName, taskDescription, status);
//        this.epicId = epicId;
//        this.type = Type.SUBTASK;
//    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return type + " {" +
                "ID= " + id +
                ", epicID= " + epicId +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
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
