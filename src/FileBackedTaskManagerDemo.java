import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTaskManagerDemo {
    public static void main(String[] args) {
        File file = new File("tasks.csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        // Создание задач
        Task task = new Task("Уборка", "Убрать квартиру", TaskStatus.NEW);
        task.setDuration(Duration.ofMinutes(90));
        task.setStartTime(LocalDateTime.of(2025, 9, 25, 10, 0));
        manager.createTask(task);

        Epic epic = new Epic("Проект по Java", "Сдать курсовую работу");
        manager.createEpic(epic);

        Subtask subtask1 = new Subtask("Написать код", "Реализовать все классы", TaskStatus.NEW, epic.getId());
        subtask1.setDuration(Duration.ofMinutes(180));
        subtask1.setStartTime(LocalDateTime.of(2025, 9, 25, 13, 0));
        manager.createSubtask(subtask1);

        Subtask subtask2 = new Subtask("Написать тесты", "Проверить код", TaskStatus.NEW, epic.getId());
        subtask2.setDuration(Duration.ofMinutes(120));
        subtask2.setStartTime(LocalDateTime.of(2025, 9, 25, 16, 0));
        manager.createSubtask(subtask2);

        // Обновляем эпик, чтобы пересчитать статус и время
        epic.updateEpicData(manager.getSubtasksOfEpic(epic.getId()));

        // Демонстрация сохранения и загрузки
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);

        System.out.println("Загруженные задачи:");
        for (Task t : loadedManager.getAllTasks()) System.out.println(t);

        System.out.println("\nЗагруженные эпики:");
        for (Epic e : loadedManager.getAllEpics()) System.out.println(e);

        System.out.println("\nЗагруженные подзадачи эпика:");
        for (Subtask s : loadedManager.getSubtasksOfEpic(epic.getId())) System.out.println(s);
    }
}
