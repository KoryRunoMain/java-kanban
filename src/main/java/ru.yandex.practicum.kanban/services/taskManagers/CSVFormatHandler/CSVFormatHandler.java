package ru.yandex.practicum.kanban.services.taskManagers.CSVFormatHandler;

import ru.yandex.practicum.kanban.models.Epic;
import ru.yandex.practicum.kanban.models.Subtask;
import ru.yandex.practicum.kanban.models.Task;
import ru.yandex.practicum.kanban.models.enums.Status;
import ru.yandex.practicum.kanban.models.enums.Type;
import ru.yandex.practicum.kanban.services.historyManagers.HistoryManager;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CSVFormatHandler {

    private static final String DELIMITER = ",";
    private static final Integer TASK_LENGTH = 5;
    private static final Integer TASK_LENGTH_WITH_EPICID = 6;

    /* Получить заголовок */
    public String getHeader() {
        return "id,type,name,status,description,epic,duration,startTime" + "\n";
    }

    /* Получить строку из задач */
    public String generateToString(Task task) {
        return  task.getId() + DELIMITER +      // id
                task.getType() + DELIMITER +            // type
                task.getName() + DELIMITER +            // name
                task.getStatus() + DELIMITER +          //status
                task.getDescription() + DELIMITER +     // description
                getEpicIdOfSubTask(task) + DELIMITER +  // epic id
                task.getDuration() + DELIMITER +        // duration
                getTaskStartTime(task);                 // startTime
    }

    private static Instant getTaskStartTime(Task task) {
        return task.getStartTime();
    }

    private static String getEpicIdOfSubTask(Task task) {
        String epicId = "";
        if (task.getType() == Type.SUBTASK) {
            Subtask subtask = (Subtask) task;
            epicId = String.valueOf(subtask.getEpicId());
            return epicId;
        }
        return epicId;
    }

    /* Получить задачи из строк */
    public Task fromString(String value) {
        String[] values = value.split(DELIMITER);
        int valuesLength = values.length;

        if (valuesLength != TASK_LENGTH &&
            valuesLength != TASK_LENGTH_WITH_EPICID) {
            return null;
        }
        int id = Integer.parseInt(values[0]);               // id
        Type type = Type.valueOf(values[1]);                // type
        String name = values[2];                            // name
        Status status = Status.valueOf(values[3]);          // status
        String description = values[4];                     // description
        int epicId = 0;
        if (type.equals(Type.SUBTASK)) {
            epicId = Integer.parseInt(values[5]);           // epic id
        }

        switch (type) {
            case EPIC -> {
                Epic epic = new Epic(name, description);
                epic.setId(id);
                epic.setStatus(status);
                return epic;
            }
            case SUBTASK -> {
                Subtask subtask = new Subtask(epicId, name, description);
                subtask.setId(id);
                subtask.setStatus(status);
                return subtask;
            }
            default -> {
                return new Task(id, name, description, status, type);
            }
        }
    }

    /* Получить строку из истории */
    public String generateHistoryToString(HistoryManager historyManager) {
        List<String> result = new ArrayList<>();
        for (Task task: historyManager.getHistory()) {
            result.add(String.valueOf(task.getId()));
        }
        return String.join(DELIMITER, result);
    }

    /* Получить историю из строки */
    public List<Integer> historyFromString(String values) {
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
