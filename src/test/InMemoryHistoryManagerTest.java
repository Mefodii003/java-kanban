package test;

import model.Task;

import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryHistoryManagerTest {

    private HistoryManager historyManager;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        task1 = new Task("Task 1", "Description 1", TaskStatus.NEW);
        task1.setId(1);
        task2 = new Task("Task 2", "Description 2", TaskStatus.IN_PROGRESS);
        task2.setId(2);
        task3 = new Task("Task 3", "Description 3", TaskStatus.DONE);
        task3.setId(3);
    }

    @Test
    void addTasksToHistory_shouldStoreInOrder() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        List<Task> history = historyManager.getHistory();
        assertEquals(3, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
        assertEquals(task3, history.get(2));
    }

    @Test
    void addDuplicateTask_shouldMoveToEndWithoutDuplicates() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1); // повторный просмотр

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task2, history.get(0));
        assertEquals(task1, history.get(1)); // task1 должен быть в конце
    }

    @Test
    void removeTask_shouldRemoveFromHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(task1.getId());

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task2, history.get(0));
    }

    @Test
    void getHistory_whenEmpty_shouldReturnEmptyList() {
        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty());
    }

    @Test
    void addNullTask_shouldDoNothing() {
        historyManager.add(null);
        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty());
    }

    @Test
    void removeNonExistentId_shouldDoNothing() {
        historyManager.add(task1);
        historyManager.remove(999); // id которого нет
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
    }
}