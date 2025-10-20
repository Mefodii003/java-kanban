public class Main {
    public static void main(String[] args) {
        HistoryManager historyManager = new InMemoryHistoryManager();
        TaskManager manager = new InMemoryTaskManager(historyManager);

        // создаем эпик
        Epic epic = new Epic("Эпик 1", "Description of epic 1", TaskStatus.NEW);
        manager.createEpic(epic);  // теперь у эпика есть id

        int epicId = epic.getId();  // получаем id созданного эпика

        // создаем подзадачу с прав epicId
        Subtask subtask = new Subtask("Subtask 1", "Description of subtask 1", TaskStatus.NEW, epicId);
        manager.createSubtask(subtask);

        // проверяем, что epicId у подзадачи установлен норм
        System.out.println("Epic ID in Subtask: " + subtask.getEpicId());  // Должно вывести id эпика

        // получаем и выводим подзадачу из менеджера
        Subtask retrievedSubtask = manager.getSubtask(subtask.getId());
        System.out.println("Retrieved Subtask: " + retrievedSubtask);

        // получаем и выводим эпик
        Epic retrievedEpic = manager.getEpic(epicId);
        System.out.println("Retrieved Epic subtasks count: " + retrievedEpic.getSubtasks().size());
    }
}