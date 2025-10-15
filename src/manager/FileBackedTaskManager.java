package manager;

import model.Task;
import model.Epic;
import model.Subtask;
import model.TaskStatus;

import model.Subtask;
import model.TaskStatus;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        super(); // используем конструктор без аргументов
        this.file = file;
    }

    //  Переопределяем методы для автоматического сохранения
    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    // Сохранение в файл
    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,startTime,duration,epic\n");
            for (Task task : tasks.values()) writer.write(toCsv(task) + "\n");
            for (Epic epic : epics.values()) writer.write(toCsv(epic) + "\n");
            for (Subtask subtask : subtasks.values()) writer.write(toCsv(subtask) + "\n");

            writer.write("\n");
            writer.write(historyToString(historyManager));
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении в файл", e);
        }
    }

    private String toCsv(Task task) {
        String start = task.getStartTime() != null ? task.getStartTime().toString() : "";
        String dur = task.getDuration() != null ? String.valueOf(task.getDuration().toMinutes()) : "";
        String epicId = task instanceof Subtask ? String.valueOf(((Subtask) task).getEpicId()) : "";
        String type = task instanceof Epic ? "EPIC" : task instanceof Subtask ? "SUBTASK" : "TASK";
        return String.format("%d,%s,%s,%s,%s,%s,%s,%s", task.getId(), type, task.getName(), task.getStatus(), task.getDescription(), start, dur, epicId);
    }

    private String historyToString(HistoryManager manager) {
        List<Task> history = manager.getHistory();
        List<String> ids = new ArrayList<>();
        for (Task t : history) ids.add(String.valueOf(t.getId()));
        return String.join(",", ids);
    }

    // Загрузка из файла
    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isHistory = false;

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    isHistory = true;
                    continue;
                }
                if (line.startsWith("id,type")) continue;

                if (!isHistory) {
                    Task task = fromCsv(line);
                    if (task instanceof Epic) manager.epics.put(task.getId(), (Epic) task);
                    else if (task instanceof Subtask) manager.subtasks.put(task.getId(), (Subtask) task);
                    else manager.tasks.put(task.getId(), task);
                } else {
                    String[] ids = line.split(",");
                    for (String idStr : ids) {
                        int id = Integer.parseInt(idStr.trim());
                        Task t = manager.tasks.get(id);
                        if (t == null) t = manager.epics.get(id);
                        if (t == null) t = manager.subtasks.get(id);
                        if (t != null) manager.historyManager.add(t);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке из файла", e);
        }

        return manager;
    }

    private static Task fromCsv(String line) {
        String[] parts = line.split(",");
        int id = Integer.parseInt(parts[0]);
        String type = parts[1];
        String name = parts[2];
        TaskStatus status = TaskStatus.valueOf(parts[3]);
        String description = parts.length > 4 ? parts[4] : "";
        String startTimeStr = parts.length > 5 ? parts[5] : "";
        String durationStr = parts.length > 6 ? parts[6] : "";
        String epicIdStr = parts.length > 7 ? parts[7] : "";

        LocalDateTime start = startTimeStr.isBlank() ? null : LocalDateTime.parse(startTimeStr);
        Duration duration = durationStr.isBlank() ? null : Duration.ofMinutes(Long.parseLong(durationStr));

        switch (type) {
            case "TASK":
                Task task = new Task(name, description, status);
                task.setId(id);
                task.setStartTime(start);
                task.setDuration(duration);
                return task;
            case "EPIC":
                Epic epic = new Epic(name, description);
                epic.setId(id);
                return epic;
            case "SUBTASK":
                Subtask subtask = new Subtask(name, description, status, Integer.parseInt(epicIdStr));
                subtask.setId(id);
                subtask.setStartTime(start);
                subtask.setDuration(duration);
                return subtask;
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }
    }
}
