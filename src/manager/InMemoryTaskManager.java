package manager;//import java.time.Duration;

import model.Task;
import model.Epic;
import model.Subtask;
import model.TaskStatus;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager historyManager;

    private int nextId = 1;

    // TreeSet для хранения задач в порядке времени
    private final Set<Task> prioritizedTasks = new TreeSet<>(
            Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(Task::getId)
    );

    public InMemoryTaskManager() {
        this.historyManager = new InMemoryHistoryManager();
    }

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    //  Создание задач
    @Override
    public void createTask(Task task) {
        if (isTimeIntersection(task)) throw new IllegalArgumentException("Пересечение по времени");
        task.setId(nextId++);
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        if (isTimeIntersection(subtask)) throw new IllegalArgumentException("Пересечение по времени");
        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);
        prioritizedTasks.add(subtask);

        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtask(subtask.getId());
            updateEpicStatus(epic);
        }
    }

    //  Получение задач
    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task != null) historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) historyManager.add(subtask);
        return subtask;
    }

    //  Обновление задач
    @Override
    public void updateTask(Task task) {
        if (isTimeIntersection(task)) throw new IllegalArgumentException("Пересечение по времени");
        tasks.put(task.getId(), task);
        prioritizedTasks.remove(task);
        prioritizedTasks.add(task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (isTimeIntersection(subtask)) throw new IllegalArgumentException("Пересечение по времени");
        subtasks.put(subtask.getId(), subtask);
        prioritizedTasks.remove(subtask);
        prioritizedTasks.add(subtask);

        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) updateEpicStatus(epic);
    }

    //  Удаление задач
    @Override
    public void deleteTask(int id) {
        Task task = tasks.remove(id);
        if (task != null) {
            historyManager.remove(id);
            prioritizedTasks.remove(task);
        }
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (Integer subId : epic.getSubtaskIds()) {
                Subtask sub = subtasks.remove(subId);
                if (sub != null) prioritizedTasks.remove(sub);
                historyManager.remove(subId);
            }
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.getSubtaskIds().remove((Integer) id);
                updateEpicStatus(epic);
            }
            historyManager.remove(id);
            prioritizedTasks.remove(subtask);
        }
    }

    @Override
    public void deleteAllTasks() {
        for (Integer id : tasks.keySet()) historyManager.remove(id);
        tasks.clear();
        prioritizedTasks.removeIf(t -> t instanceof Task && !(t instanceof Subtask));
    }

    @Override
    public void deleteAllEpics() {
        for (Epic epic : epics.values()) {
            for (Integer subId : epic.getSubtaskIds()) {
                Subtask sub = subtasks.remove(subId);
                if (sub != null) prioritizedTasks.remove(sub);
                historyManager.remove(subId);
            }
            historyManager.remove(epic.getId());
        }
        epics.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (Subtask sub : subtasks.values()) {
            historyManager.remove(sub.getId());
            prioritizedTasks.remove(sub);
        }
        subtasks.clear();
        for (Epic epic : epics.values()) epic.getSubtaskIds().clear();
    }

    //  Списки всех задач
    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Subtask> getSubtasksOfEpic(int epicId) {
        Epic epic = epics.get(epicId);
        List<Subtask> list = new ArrayList<>();
        if (epic != null) {
            for (Integer subId : epic.getSubtaskIds()) {
                Subtask sub = subtasks.get(subId);
                if (sub != null) list.add(sub);
            }
        }
        return list;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    //  Исправленные методы
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    public boolean isTimeIntersection(Task task) {
        if (task.getStartTime() == null || task.getDuration() == null) return false;

        LocalDateTime start = task.getStartTime();
        LocalDateTime end = start.plus(task.getDuration());

        for (Task t : prioritizedTasks) {
            if (t.getId() == task.getId() || t.getStartTime() == null || t.getDuration() == null)
                continue;
            LocalDateTime tStart = t.getStartTime();
            LocalDateTime tEnd = tStart.plus(t.getDuration());
            if (!(end.isBefore(tStart) || start.isAfter(tEnd))) return true;
        }
        return false;
    }

    // Вспомогательные методы
    private void updateEpicStatus(Epic epic) {
        List<Subtask> epicSubs = getSubtasksOfEpic(epic.getId());
        epic.updateEpicData(epicSubs);
    }
}
