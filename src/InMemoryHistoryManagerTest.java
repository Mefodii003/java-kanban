import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class InMemoryHistoryManagerTest {

    @Test
    public void testAddAndGetTask() {
        TaskManager manager = new InMemoryTaskManager();
        Task task = new Task("Test Task", "Desc", TaskStatus.NEW); // Передаем статус
        manager.createTask(task);
        Task retrieved = manager.getTask(task.getId());

        assertEquals(task, retrieved);
    }

    @Test
    public void testHistoryWithoutDuplicates() {
        TaskManager manager = new InMemoryTaskManager();
        Task task1 = new Task("T1", "D", TaskStatus.NEW);
        manager.createTask(task1);
        manager.getTask(task1.getId());
        manager.getTask(task1.getId());
        List<Task> history = manager.getHistory();

        assertEquals(1, history.size());
    }

    @Test
    public void testRemoveTaskFromHistory() {
        TaskManager manager = new InMemoryTaskManager();
        Task task = new Task("Task", "Desc", TaskStatus.NEW);
        manager.createTask(task);
        manager.getTask(task.getId());

        manager.deleteTask(task.getId());
        assertTrue(manager.getHistory().isEmpty());
    }
}