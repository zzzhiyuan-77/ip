import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves and loads Moon's tasks from a file in the project directory.
 */
public class Storage {
    /** The relative, operating-system-independent location of Moon's data file. */
    private static final Path FILE_PATH = Path.of("data", "duke.txt");

    /**
     * Saves every task, replacing the previous saved task list.
     *
     * @param tasks the tasks to save
     * @throws IOException if the data folder or file cannot be written
     */
    public void save(List<Task> tasks) throws IOException {
        Files.createDirectories(FILE_PATH.getParent());

        List<String> savedTasks = new ArrayList<>();
        for (Task task : tasks) {
            savedTasks.add(task.toSaveFormat());
        }
        Files.write(FILE_PATH, savedTasks, StandardCharsets.UTF_8);
    }

    /**
     * Loads the tasks saved by Moon, or returns an empty list on Moon's first run.
     *
     * @return the tasks stored in Moon's data file
     * @throws IOException if the data file cannot be read
     */
    public List<Task> load() throws IOException {
        List<Task> tasks = new ArrayList<>();
        if (!Files.exists(FILE_PATH)) {
            return tasks;
        }

        for (String savedTask : Files.readAllLines(FILE_PATH, StandardCharsets.UTF_8)) {
            tasks.add(createTask(savedTask));
        }
        return tasks;
    }

    /**
     * Recreates one task from a line in Moon's data-file format.
     *
     * @param savedTask one saved task line
     * @return the recreated task
     */
    private Task createTask(String savedTask) {
        String[] parts = savedTask.split(" \\| ");
        Task task = switch (parts[0]) {
        case "T" -> new ToDo(parts[2]);
        case "D" -> new Deadline(parts[2], parts[3]);
        case "E" -> new Event(parts[2], parts[3], parts[4]);
        default -> throw new IllegalArgumentException("Unknown task type: " + parts[0]);
        };
        if (parts[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }
}
