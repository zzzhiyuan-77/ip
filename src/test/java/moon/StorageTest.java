package moon;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

/** Tests validation of task data loaded from disk. */
public class StorageTest {
    /** Verifies that blank persisted entries are reported as invalid data. */
    @Test
    public void loadFromSaveFormats_blankEntry_throwsIOException() {
        Storage storage = new Storage();

        assertThrows(IOException.class, () -> storage.loadFromSaveFormats(List.of("   ")));
    }

    /** Verifies that missing persisted data is reported as invalid input. */
    @Test
    public void loadFromSaveFormats_missingContents_throwsIOException() {
        Storage storage = new Storage();

        assertThrows(IOException.class, () -> storage.loadFromSaveFormats(null));
    }

    /** Verifies that persisted events must have a forward date range. */
    @Test
    public void loadFromSaveFormats_invalidEventRange_throwsIOException() {
        Storage storage = new Storage();

        assertThrows(IOException.class, () -> storage.loadFromSaveFormats(List.of(
                "E | 0 | concert | 2026-09-20 | 2026-09-20")));
    }

    /** Verifies that persisted duplicate tasks are rejected. */
    @Test
    public void loadFromSaveFormats_duplicateTask_throwsIOException() {
        Storage storage = new Storage();
        String task = "T | 0 | read book";

        assertThrows(IOException.class, () -> storage.loadFromSaveFormats(List.of(task, task)));
    }
}
