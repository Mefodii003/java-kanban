import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class InMemoryTaskManagerTest {

    @Test
    public void testAddAndGetTask() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        TaskManager manager = new InMemoryTaskManager(historyManager);

        Task task = new Task("Test Task", "Desc", TaskStatus.NEW);
        manager.createTask(task);
        Task retrieved = manager.getTask(task.getId());

        assertEquals(task, retrieved);
    }

    @Test
    public void testAddEpicAndSubtaskUpdatesStatus() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        TaskManager manager = new InMemoryTaskManager(historyManager);

        Epic epic = new Epic("Epic", "Desc", TaskStatus.NEW);
        manager.createEpic(epic);

        Subtask sub1 = new Subtask("Sub1", "Desc", TaskStatus.NEW, epic.getId());
        Subtask sub2 = new Subtask("Sub2", "Desc", TaskStatus.DONE, epic.getId());

        manager.createSubtask(sub1);
        manager.createSubtask(sub2);

        Epic updated = manager.getEpic(epic.getId());

        // если у эпика есть и NEW, и DONE сабтаски — статус должен быть IN_PROGRESS
        assertEquals(TaskStatus.IN_PROGRESS, updated.getStatus());
    }

    @Test
    public void testHistoryWithoutDuplicates() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        TaskManager manager = new InMemoryTaskManager(historyManager);

        Task task1 = new Task("T1", "D", TaskStatus.NEW);
        manager.createTask(task1);

        manager.getTask(task1.getId());
        manager.getTask(task1.getId());

        List<Task> history = manager.getHistory();

        assertEquals(1, history.size());
    }

    @Test
    public void testRemoveAlsoRemovesFromHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        TaskManager manager = new InMemoryTaskManager(historyManager);

        Task task = new Task("Task", "Desc", TaskStatus.NEW);
        manager.createTask(task);
        manager.getTask(task.getId());

        manager.deleteTask(task.getId());

        assertTrue(manager.getHistory().isEmpty());
    }
}