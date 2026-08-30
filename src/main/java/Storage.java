import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
     * @throws IOException if the data file cannot be read or has an invalid format
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
    private Task createTask(String savedTask) throws IOException {
        String[] parts = savedTask.split(" \\| ", -1);
        if (parts.length < 3 || !isValidStatus(parts[1])) {
            throw new IOException("The data file has an invalid task format.");
        }
        Task task = switch (parts[0]) {
        case "T" -> createToDo(parts);
        case "D" -> createDeadline(parts);
        case "E" -> createEvent(parts);
        default -> throw new IOException("The data file has an unknown task type.");
        };
        if (parts[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Checks whether a saved completion status is valid.
     *
     * @param status the saved completion status
     * @return whether the status is {@code 0} or {@code 1}
     */
    private boolean isValidStatus(String status) {
        return status.equals("0") || status.equals("1");
    }

    /**
     * Recreates a to-do from its saved fields.
     *
     * @param parts the fields in the saved task line
     * @return the recreated to-do
     * @throws IOException if the saved fields are invalid
     */
    private ToDo createToDo(String[] parts) throws IOException {
        if (parts.length != 3 || parts[2].isBlank()) {
            throw new IOException("The data file has an invalid to-do format.");
        }
        return new ToDo(parts[2]);
    }

    /**
     * Recreates a deadline from its saved fields.
     *
     * @param parts the fields in the saved task line
     * @return the recreated deadline
     * @throws IOException if the saved fields are invalid
     */
    private Deadline createDeadline(String[] parts) throws IOException {
        if (parts.length != 4 || parts[2].isBlank() || parts[3].isBlank()) {
            throw new IOException("The data file has an invalid deadline format.");
        }
        return new Deadline(parts[2], parseSavedDate(parts[3]));
    }

    /**
     * Recreates an event from its saved fields.
     *
     * @param parts the fields in the saved task line
     * @return the recreated event
     * @throws IOException if the saved fields are invalid
     */
    private Event createEvent(String[] parts) throws IOException {
        if (parts.length != 5 || parts[2].isBlank() || parts[3].isBlank() || parts[4].isBlank()) {
            throw new IOException("The data file has an invalid event format.");
        }
        return new Event(parts[2], parseSavedDate(parts[3]), parseSavedDate(parts[4]));
    }

    /**
     * Parses one ISO-8601 date stored in Moon's data file.
     *
     * @param dateText the saved date
     * @return the parsed date
     * @throws IOException if the saved date is invalid
     */
    private LocalDate parseSavedDate(String dateText) throws IOException {
        try {
            return LocalDate.parse(dateText);
        } catch (DateTimeParseException exception) {
            throw new IOException("The data file has an invalid date.", exception);
        }
    }
}
