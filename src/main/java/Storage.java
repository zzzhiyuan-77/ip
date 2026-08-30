import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves Moon's tasks to a file in the project directory.
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
}
