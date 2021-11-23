import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DCInvestigatorReaderShould {
    String currentDirectory = System.getProperty("user.dir") + "\\src\\test\\resources\\";
    DCInvestigatorReader reader = new DCInvestigatorReader(currentDirectory+"DCInvestigatorReport.xml");
    @Test
    void getFailedEvents() {
        Set<FailedEvent> actual = reader.GetFailedEvents();
        assertEquals(770,actual.size());
    }
    @Test
    void getBadLifecycles() {
        Integer[] expected = new Integer[]{320, 195, 201, 202, 204, 205, 78, 79, 207, 208, 339, 211, 212, 213, 214, 215, 216, 217, 219, 220, 94, 479, 480, 98, 296, 40, 237, 498, 57, 250, 59, 380, 319};
        assertTrue(Arrays.asList(expected).containsAll(reader.GetBadLifecycles()));
    }
    @Test
    void getBadEventsPerLifecycle() {
        Integer[] expected = new Integer[]{36, 47};
        for(Integer event: reader.GetBadEventsPerLifecycle(76)){
            assertTrue(Arrays.asList(expected).contains(event));
        }
    }
}