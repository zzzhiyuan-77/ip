package moon;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests the state changes and persistence format of a basic task.
 */
public class TaskTest {
    /** Verifies that marking and unmarking changes both representations. */
    @Test
    void taskStatus_markAndUnmark_updatesIconAndSaveFormat() {
        Task task = new Task("read book");

        assertEquals(" ", task.getStatusIcon());
        assertEquals("T | 0 | read book", task.toSaveFormat());

        task.markAsDone();
        assertEquals("X", task.getStatusIcon());
        assertEquals("T | 1 | read book", task.toSaveFormat());

        task.unmarkAsDone();
        assertEquals(" ", task.getStatusIcon());
        assertEquals("T | 0 | read book", task.toSaveFormat());
    }
}
