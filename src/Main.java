import manager.FileBackedTaskManager;

import model.Task;
import model.Epic;
import model.Subtask;
import model.TaskStatus;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        File file = new File("tasks.csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        // Создаём обычную задачу
        Task task1 = new Task("Покупка продуктов", "Купить продукты в магазине", TaskStatus.NEW);
        task1.setDuration(Duration.ofMinutes(60));
        task1.setStartTime(LocalDateTime.of(2025, 9, 25, 12, 0));
        manager.createTask(task1);

        // Создаём эпик
        Epic epic1 = new Epic("Ремонт квартиры", "Сделать ремонт в гостиной");
        manager.createEpic(epic1);

        // Создаём подзадачи
        Subtask sub1 = new Subtask("Купить краску", "Выбрать и купить краску", TaskStatus.NEW, epic1.getId());
        sub1.setDuration(Duration.ofMinutes(120));
        sub1.setStartTime(LocalDateTime.of(2025, 9, 26, 9, 0));
        manager.createSubtask(sub1);

        Subtask sub2 = new Subtask("Покрасить стены", "Покрасить стены в гостиной", TaskStatus.NEW, epic1.getId());
        sub2.setDuration(Duration.ofMinutes(180));
        sub2.setStartTime(LocalDateTime.of(2025, 9, 26, 12, 0));
        manager.createSubtask(sub2);

        // Обновляем эпик после добавления подзадач
        epic1.updateEpicData(manager.getSubtasksOfEpic(epic1.getId()));

        // Выводим всё в консоль
        System.out.println("Все задачи:");
        for (Task t : manager.getAllTasks()) System.out.println(t);

        System.out.println("\nЭпики:");
        for (Epic e : manager.getAllEpics()) System.out.println(e);

        System.out.println("\nПодзадачи эпика:");
        for (Subtask s : manager.getSubtasksOfEpic(epic1.getId())) System.out.println(s);

        System.out.println("\nИстория:");
        for (Task t : manager.getHistory()) System.out.println(t);
    }
}
