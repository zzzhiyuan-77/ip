package moon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** Tests Moon's user-interface-independent command engine. */
public class MoonEngineTest {
    /** Verifies that a newly created engine can report its loading status. */
    @Test
    public void constructor_loadsValidSavedData() {
        MoonEngine engine = new MoonEngine();

        assertFalse(engine.hasLoadingError());
    }

    /** Verifies that the engine handles a command without a required keyword. */
    @Test
    public void getResponse_findWithoutKeyword_returnsHelpfulError() {
        MoonEngine engine = new MoonEngine();

        String response = engine.getResponse("find");

        assertTrue(response.contains("your find needs a keyword."));
    }

    /** Verifies that the GUI can display Moon's farewell response. */
    @Test
    public void getResponse_bye_returnsFarewell() {
        MoonEngine engine = new MoonEngine();

        assertEquals("Bye. Hope to see you again soon!", engine.getResponse("bye"));
    }

    /** Verifies that undo reports clearly when no previous mutation exists. */
    @Test
    public void getResponse_undoWithoutPreviousCommand_reportsNoAction() {
        MoonEngine engine = new MoonEngine();

        assertTrue(engine.getResponse("undo").contains("Nothing to undo."));
    }
}
