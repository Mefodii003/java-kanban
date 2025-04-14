import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class InMemoryHistoryManagerTest {

    @Test
    public void testAddHistoryAndNoDuplicates() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task("T", "D");
        task.setId(1);
        historyManager.add(task);
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
    }

    @Test
    public void testRemoveFromHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task("T", "D");
        task.setId(1);
        historyManager.add(task);
        historyManager.remove(1);

        assertTrue(historyManager.getHistory().isEmpty());
    }
}