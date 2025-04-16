public class Main {
    public static void main(String[] args) {

        TaskManager taskManager = new InMemoryTaskManager();
        Task epic = new Epic("Epic Task", "Epic Description");

        taskManager.addEpic((Epic) epic);

        Subtask subtask = new Subtask("Subtask", "Subtask description", TaskStatus.NEW, 1);


        taskManager.addSubtask(subtask);


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