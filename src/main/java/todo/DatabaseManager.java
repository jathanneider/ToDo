package todo;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_NAME = "todo_db";
    private static final String COLLECTION_NAME = "tasks";
    private final MongoCollection<Document> collection;

    public DatabaseManager() {
        startMongoContainer();
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase(DB_NAME);
        collection = database.getCollection(COLLECTION_NAME);
    }

    public void addTask(Task task) {
        Document doc = new Document("description", task.getDescription())
                .append("completed", task.isCompleted());
        collection.insertOne(doc);
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        for (Document doc : collection.find()) {
            Task task = new Task(doc.getString("description"));
            task.setCompleted(doc.getBoolean("completed", false));
            task.setId(doc.getObjectId("_id").toString());
            tasks.add(task);
        }
        return tasks;
    }

    public void removeTask(String taskId) {
        collection.deleteOne(Filters.eq("_id", new org.bson.types.ObjectId(taskId)));
    }

    public void markTaskCompleted(String taskId) {
        collection.updateOne(Filters.eq("_id", new org.bson.types.ObjectId(taskId)),
                new Document("$set", new Document("completed", true)));
    }

    private void startMongoContainer() {
        if (!isContainerRunning("todo-mongo")) {
            System.out.println("Starting MongoDB container...");
            executeCommand("docker-compose up -d");
        }
    }

    public void stopMongoContainer() {
        System.out.println("Stopping MongoDB container...");
        executeCommand("docker-compose down");
    }

    private boolean isContainerRunning(String containerName) {
        try {
            Process process = new ProcessBuilder("docker", "ps", "--filter", "name=" + containerName, "--format", "{{.Names}}")
                    .start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().equals(containerName)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void executeCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}