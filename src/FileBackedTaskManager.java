import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        super(Managers.getDefaultHistory());
        this.file = file;
    }

    // метод сохранения в CSV
    protected void save() {
        try (Writer writer = new FileWriter(file)) {
            // Заголовок
            writer.write("id,type,name,status,description,epic\n");

            // Задачи
            for (Task task : getAllTasks()) {
                writer.write(toString(task) + "\n");
            }
            for (Epic epic : getAllEpics()) {
                writer.write(toString(epic) + "\n");
            }
            for (Subtask subtask : getAllSubtasks()) {
                writer.write(toString(subtask) + "\n");
            }

            // Разделитель
            writer.write("\n");

            // История
            List<Task> history = getHistory();
            if (!history.isEmpty()) {
                String ids = String.join(",",
                        history.stream().map(t -> String.valueOf(t.getId())).toArray(String[]::new));
                writer.write(ids);
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении файла: " + e.getMessage());
        }
    }

    private String toString(Task task) {
        String type;
        if (task instanceof Epic) {
            type = "EPIC";
        } else if (task instanceof Subtask) {
            type = "SUBTASK";
        } else {
            type = "TASK";
        }

        String epicId = "";
        if (task instanceof Subtask) {
            epicId = String.valueOf(((Subtask) task).getEpicId());
        }

        return String.format("%d,%s,%s,%s,%s,%s",
                task.getId(),
                type,
                task.getName(),
                task.getStatus(),
                task.getDescription(),
                epicId
        );
    }

    private static Task fromString(String value) {
        String[] parts = value.split(",");
        int id = Integer.parseInt(parts[0]);
        String type = parts[1];
        String name = parts[2];
        TaskStatus status = TaskStatus.valueOf(parts[3]);
        String description = parts[4];

        switch (type) {
            case "TASK":
                Task task = new Task(name, description, status);
                task.setId(id);
                return task;
            case "EPIC":
                Epic epic = new Epic(name, description, status);
                epic.setId(id);
                return epic;
            case "SUBTASK":
                int epicId = Integer.parseInt(parts[5]);
                Subtask subtask = new Subtask(name, description, status, epicId);
                subtask.setId(id);
                return subtask;
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            String content = Files.readString(file.toPath());
            String[] sections = content.split("\n\n"); // разделяем задачи и историю

            String[] lines = sections[0].split("\n");
            // пропускаем заголовок
            for (int i = 1; i < lines.length; i++) {
                if (lines[i].isBlank()) continue;
                Task task = fromString(lines[i]);
                if (task instanceof Epic) {
                    manager.epics.put(task.getId(), (Epic) task);
                } else if (task instanceof Subtask) {
                    manager.subtasks.put(task.getId(), (Subtask) task);
                } else {
                    manager.tasks.put(task.getId(), task);
                }
                manager.nextId = Math.max(manager.nextId, task.getId() + 1);
            }

            // восстанавливаем связи эпиков и сабтасков
            for (Subtask sub : manager.subtasks.values()) {
                Epic epic = manager.epics.get(sub.getEpicId());
                if (epic != null) {
                    epic.addSubtask(sub);
                }
            }

            // восстанавливаем историю
            if (sections.length > 1 && !sections[1].isBlank()) {
                String[] ids = sections[1].trim().split(",");
                for (String idStr : ids) {
                    int id = Integer.parseInt(idStr);
                    Task task = manager.tasks.get(id);
                    if (task == null) task = manager.epics.get(id);
                    if (task == null) task = manager.subtasks.get(id);
                    if (task != null) {
                        manager.historyManager.add(task);
                    }
                }
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке файла: " + e.getMessage());
        }
        return manager;
    }

    // Переопределяем все модифицирующие методы

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }
}