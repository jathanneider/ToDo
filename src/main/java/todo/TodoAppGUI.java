package todo;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TodoAppGUI extends JFrame {
    private final DefaultListModel<Task> taskListModel;
    private final JList<Task> taskList;
    private final DatabaseManager dbManager;

    public TodoAppGUI() {
        dbManager = new DatabaseManager();

        setTitle("To-Do List");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        refreshTaskList();

        JScrollPane scrollPane = new JScrollPane(taskList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Task");
        JButton removeButton = new JButton("Remove Task");
        JButton completeButton = new JButton("Mark Completed");

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(completeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addTask());
        removeButton.addActionListener(e -> removeTask());
        completeButton.addActionListener(e -> completeTask());

        setVisible(true);
    }

    private void addTask() {
        String description = JOptionPane.showInputDialog("Enter task description:");
        if (description != null && !description.trim().isEmpty()) {
            Task newTask = new Task(description);
            dbManager.addTask(newTask);
            refreshTaskList();
        }
    }

    private void removeTask() {
        Task selectedTask = taskList.getSelectedValue();
        if (selectedTask != null) {
            dbManager.removeTask(selectedTask.getId());
            refreshTaskList();
        }
    }

    private void completeTask() {
        Task selectedTask = taskList.getSelectedValue();
        if (selectedTask != null) {
            dbManager.markTaskCompleted(selectedTask.getId());
            refreshTaskList();
        }
    }

    private void refreshTaskList() {
        taskListModel.clear();
        List<Task> tasks = dbManager.getAllTasks();
        for (Task task : tasks) {
            taskListModel.addElement(task);
        }
    }
}