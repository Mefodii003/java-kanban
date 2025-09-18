import java.io.File;

public class FileBackedTaskManagerDemo {
    public static void main(String[] args) {
        try {
            File file = new File("tasks.csv");
            FileBackedTaskManager manager = new FileBackedTaskManager(file);

            // Создаём задачи
            Task task1 = new Task("Task 1", "Description 1", TaskStatus.NEW);
            Task task2 = new Task("Task 2", "Description 2", TaskStatus.IN_PROGRESS);
            manager.createTask(task1);
            manager.createTask(task2);

            // Создаём эпик и подзадачи
            Epic epic = new Epic("Epic 1", "Epic Description", TaskStatus.NEW);
            manager.createEpic(epic);
            Subtask sub1 = new Subtask("Subtask 1", "Subtask Desc", TaskStatus.NEW, epic.getId());
            Subtask sub2 = new Subtask("Subtask 2", "Subtask Desc", TaskStatus.DONE, epic.getId());
            manager.createSubtask(sub1);
            manager.createSubtask(sub2);

            // Просматриваем задачи для истории
            manager.getTask(task1.getId());
            manager.getEpic(epic.getId());
            manager.getSubtask(sub1.getId());

            System.out.println("=== Исходный менеджер ===");
            System.out.println("Задачи: " + manager.getAllTasks());
            System.out.println("Эпики: " + manager.getAllEpics());
            System.out.println("Подзадачи: " + manager.getAllSubtasks());
            System.out.println("История: " + manager.getHistory());

            // Загружаем новый менеджер из файла
            FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);

            System.out.println("\n=== Загруженный менеджер ===");
            System.out.println("Задачи: " + loadedManager.getAllTasks());
            System.out.println("Эпики: " + loadedManager.getAllEpics());
            System.out.println("Подзадачи: " + loadedManager.getAllSubtasks());
            System.out.println("История: " + loadedManager.getHistory());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}