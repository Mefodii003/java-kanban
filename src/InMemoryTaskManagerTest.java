import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class InMemoryTaskManagerTest {

    @Test
    public void testAddAndGetTask() {
        TaskManager manager = new InMemoryTaskManager();
        Task task = new Task("Test Task", "Desc", TaskStatus.NEW); // Передаем статус
        manager.createTask(task);
        Task retrieved = manager.getTask(task.getId());

        assertEquals(task, retrieved);
    }

    @Test
    public void testAddEpicAndSubtaskUpdatesStatus() {
        TaskManager manager = new InMemoryTaskManager();
        Epic epic = new Epic("Epic", "Desc");
        manager.createEpic(epic);
        Subtask sub1 = new Subtask("Sub1", "Desc", TaskStatus.NEW, epic.getId());
        Subtask sub2 = new Subtask("Sub2", "Desc", TaskStatus.DONE, epic.getId());
        manager.createSubtask(sub1);
        manager.createSubtask(sub2);

        Epic updated = manager.getEpic(epic.getId());
        assertEquals(TaskStatus.IN_PROGRESS, updated.getStatus());
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
    public void testRemoveAlsoRemovesFromHistory() {
        TaskManager manager = new InMemoryTaskManager();
        Task task = new Task("Task", "Desc", TaskStatus.NEW);
        manager.createTask(task);
        manager.getTask(task.getId());

        manager.deleteTask(task.getId());
        assertTrue(manager.getHistory().isEmpty());
    }
}