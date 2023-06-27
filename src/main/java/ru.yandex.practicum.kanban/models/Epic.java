package ru.yandex.practicum.kanban.models;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private final ArrayList<Integer> subTasks;

    public Epic(String taskName, String taskDescription, int taskId, Status status) {
        super(taskName, taskDescription);
        this.status = status;
        this.taskId = taskId;
        this.type = Type.EPIC;
        this.subTasks = new ArrayList<>();
    }

    public ArrayList<Integer> getSubTask() {
        return subTasks;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "taskName='" + taskName + '\'' +
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
        Epic epic = (Epic) o;
        return Objects.equals(subTasks, epic.subTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasks);
    }
}
