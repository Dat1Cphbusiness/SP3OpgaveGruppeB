import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StreamingServiceTester {

    private StreamingService testService;

    public StreamingServiceTester()
    {
        this.testService = new StreamingService();
    }

    @Test
    public void addUserTest()
    {
        String username = "Bent";
        testService.registerUser(username,"SomeGreatPassword");
        assertFalse(testService.getUsers().stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username+"d")));
        assertTrue(testService.getUsers().stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username)));

        //Testing that duplicates will fail
        assertFalse(testService.registerUser(username,"SomeGreatPassword2"));
    }
    @Test
    public void loadMediaTest()
    {
        System.out.println(testService.getMedia());
        assertFalse(testService.getMedia().isEmpty());
        assertEquals(100,testService.browseMovies().size());
        assertEquals(100,testService.browseSeries().size());
    }
}
