package ru.yandex.practicum.kanban.models;

import java.util.Objects;

public class Task {
    protected String taskName;
    protected String taskDescription;
    protected int taskId;
    protected Status status;
    protected Type type;

    public Task(String taskName, String taskDescription) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskId = 0;
        this.status = Status.NEW;
    }

//    public Task(String taskName, String taskDescription) {
//        this.taskName = taskName;
//        this.taskDescription = taskDescription;
//    }

    public Type getType() {
        return Type.TASK;
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
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskId=" + taskId +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskId == task.taskId
                && Objects.equals(taskName, task.taskName)
                && Objects.equals(taskDescription, task.taskDescription)
                && status == task.status
                && type == task.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskName, taskDescription, taskId, status, type);
    }

}
