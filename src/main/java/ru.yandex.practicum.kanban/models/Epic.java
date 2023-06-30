package ru.yandex.practicum.kanban.models;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subTasksIds;
    public ArrayList<Integer> subTaskList;

    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription);
        this.subTasksIds = new ArrayList<>();
        this.subTaskList = new ArrayList<>(getSubTask());
        this.type = Type.EPIC;
    }


    public ArrayList<Integer> getSubTask() {
        return subTasksIds;
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
        return Objects.equals(subTasksIds, epic.subTasksIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasksIds);
    }
}
