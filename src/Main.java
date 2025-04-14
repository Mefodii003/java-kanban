public class Main {
    public static void main(String[] args) {
        // Использование конкретной реализации TaskManager
        TaskManager taskManager = new InMemoryTaskManager();

        // Создание задачи Epic
        Task epic = new Epic("Epic Task", "Epic Description");

        // Добавление эпика в TaskManager
        taskManager.addEpic((Epic) epic);

        // Создание подзадачи Subtask
        Subtask subtask = new Subtask("Subtask", "Subtask description", TaskStatus.NEW, 1);

        // Добавление подзадачи в TaskManager
        taskManager.addSubtask(subtask);

        // Пример получения задачи по id (чтобы добавить в историю)
        taskManager.getTask(1);  // Это вызовет добавление задачи в историю
        taskManager.getSubtask(1);  // Это также добавит подзадачу в историю
        taskManager.getEpic(1);  // Это добавит эпик в историю

        // Получение истории
        System.out.println("История просмотров:");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
    }
}