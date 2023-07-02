package ru.yandex.practicum.kanban.models;

import java.util.Objects;

public class Task {
    protected String taskName;
    protected String taskDescription;
    protected int id;
    protected Status status;
    protected Type type;

    public Task(String taskName, String taskDescription) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.id = 1;
        this.type = Type.TASK;
        this.status = Status.NEW;
    }


    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public int getTaskId() {
        return id;
    }

    public void setTaskId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return type + " {" +
                "ID=" + id +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id
                && Objects.equals(taskName, task.taskName)
                && Objects.equals(taskDescription, task.taskDescription)
                && status == task.status
                && type == task.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, taskName, taskDescription, status, type);
    }

}
