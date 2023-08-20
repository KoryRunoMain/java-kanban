package ru.yandex.practicum.kanban.services.taskManagers.CSVFormatHandler;

import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.models.Task;
import ru.yandex.practicum.kanban.models.enums.Status;
import ru.yandex.practicum.kanban.models.enums.Type;
import ru.yandex.practicum.kanban.services.historyManagers.HistoryManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CSVFormatHandler {

    private static final String DELIMITER = ",";

    /* Получить заголовок */
    public String getHeader() {
        return "id,type,name,status,description,epic" + "\n";
    }

    /* Получить строку задач*/
    public String generateToString(Task task) {
        String result = task.getTaskId() + DELIMITER +      // id
                task.getType() + DELIMITER +                // type
                task.getTaskName() + DELIMITER +            // name
                task.getStatus() + DELIMITER +              //status
                task.getTaskDescription() + DELIMITER;      // description

        if (task.getType() == Type.SUBTASK) {
            result += ((Subtask) task).getEpicId();         // epic id
        }
        return result;
    }

    /* Получить задачи из строки */
    public Task fromString(String value) {
        String[] values = value.split(DELIMITER);

        int id = Integer.parseInt(values[0]);               // id
        Type type = Type.valueOf(values[1]);                // type
        String name = values[2];                            // name
        Status status = Status.valueOf(values[3]);          // status
        String description = values[4];                     // description
        int epicId = Integer.parseInt(values[5]);           // epic id

        switch (type) {
            case EPIC -> {
                Epic epic = new Epic(name, description, status);
                epic.setTaskId(id);
                epic.setStatus(status);
                return epic;
            }
            case SUBTASK -> {
                Subtask subtask = new Subtask(epicId, name, description, status);
                subtask.setTaskId(id);
                subtask.setStatus(status);
                return subtask;
            }
            default -> {
                Task task = new Task(name, description, status);
                task.setTaskId(id);
                return task;
            }
        }
    }

    public String generateHistoryToString(HistoryManager historyManager) {
        List<String> result = new ArrayList<>();
        for (Task task: historyManager.getHistory()) {
            result.add(String.valueOf(task.getTaskId()));
        }
        return String.join(DELIMITER, result);
    }

    public List<Integer> historyFromString(String values) {
//        if (values.isEmpty()) {
//            return Collections.emptyList();
//        }
        List<Integer> taskIDs = new ArrayList<>();
        if (values == null) {
            return taskIDs;
        }
        String[] splitValue = values.split(DELIMITER);
        for (String value : splitValue) {
            taskIDs.add(Integer.parseInt(value));
        }
        return taskIDs;
    }
}
