package manager;

import model.Task;
import model.Epic;
import model.Subtask;

import java.util.List;

public interface TaskManager {
    void createTask(Task task);

    List<Task> getAllTasks();

    void deleteAllTasks();

    Task getTask(int id);

    void updateTask(Task task);

    void deleteTask(int id);

    void createEpic(Epic epic);

    List<Epic> getAllEpics();

    void deleteAllEpics();

    Epic getEpic(int id);

    void updateEpic(Epic epic);

    void deleteEpic(int id);

    void createSubtask(Subtask subtask);

    List<Subtask> getAllSubtasks();

    void deleteAllSubtasks();

    Subtask getSubtask(int id);

    void updateSubtask(Subtask subtask);

    void deleteSubtask(int id);

    List<Subtask> getSubtasksOfEpic(int epicId);

    List<Task> getHistory();
}