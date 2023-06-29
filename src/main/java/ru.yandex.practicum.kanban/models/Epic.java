package ru.yandex.practicum.kanban.models;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subTasksId;
    protected Type type;

    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription);
        this.subTasksId = new ArrayList<>();
        this.type = Type.EPIC;
    }

    public ArrayList<Integer> getSubTask() {
        return subTasksId;
    }

    @Override
    public String toString() {
        return type + " {" +
                "ID= " + id +
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
        Epic epic = (Epic) o;
        return Objects.equals(subTasksId, epic.subTasksId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasksId);
    }
}
