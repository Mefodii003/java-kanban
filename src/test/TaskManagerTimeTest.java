package test;

import model.Task;
import manager.InMemoryTaskManager;
import manager.Managers;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTimeTest {

    private InMemoryTaskManager manager;

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager(Managers.getDefaultHistory());
    }

    @Test
    void testGetPrioritizedTasks() {
        // Создаем задачи с временем старта
        Task task1 = new Task("Task 1", "Desc", TaskStatus.NEW);
        task1.setStartTime(LocalDateTime.of(2025, 9, 25, 10, 0));
        task1.setDuration(Duration.ofMinutes(30));
        manager.createTask(task1);

        Task task2 = new Task("Task 2", "Desc", TaskStatus.NEW);
        task2.setStartTime(LocalDateTime.of(2025, 9, 25, 11, 0));
        task2.setDuration(Duration.ofMinutes(45));
        manager.createTask(task2);

        List<Task> prioritized = manager.getPrioritizedTasks();
        assertEquals(2, prioritized.size());
        assertEquals(task1, prioritized.get(0));
        assertEquals(task2, prioritized.get(1));
    }

    @Test
    void testTimeIntersection() {
        // Создаем первую задачу
        Task task1 = new Task("Task 1", "Desc", TaskStatus.NEW);
        task1.setStartTime(LocalDateTime.of(2025, 9, 25, 10, 0));
        task1.setDuration(Duration.ofMinutes(60));
        manager.createTask(task1);

        // Создаем вторую задачу, которая пересекается
        Task task2 = new Task("Task 2", "Desc", TaskStatus.NEW);
        task2.setStartTime(LocalDateTime.of(2025, 9, 25, 10, 30));
        task2.setDuration(Duration.ofMinutes(30));

        boolean intersects = manager.isTimeIntersection(task2);
        assertTrue(intersects, "Ожидается пересечение по времени");

        // Создаем третью задачу, которая не пересекается
        Task task3 = new Task("Task 3", "Desc", TaskStatus.NEW);
        task3.setStartTime(LocalDateTime.of(2025, 9, 25, 11, 30));
        task3.setDuration(Duration.ofMinutes(30));

        assertFalse(manager.isTimeIntersection(task3), "Ожидается отсутствие пересечения");
    }
}
