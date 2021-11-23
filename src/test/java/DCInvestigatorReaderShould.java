import org.junit.internal.runners.statements.Fail;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DCInvestigatorReaderShould {
    String currentDirectory = System.getProperty("user.dir") + "\\src\\test\\resources\\";

    @Test
    void getFailedEvents() {
        DCInvestigatorReader reader = new DCInvestigatorReader(currentDirectory+"DCInvestigatorReport.xml");
        Set<FailedEvent> actual = reader.GetFailedEvents();
        assertEquals(770,actual.size());
    }

    @Test
    void getBadLifecycles() {
    }

    @Test
    void getBadEventsPerLifecycle() {
    }
}