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

    @Test
    public void saveMediaTest()
    {
        testService.registerUser("legobygger", "numsehul");
        testService.login("legobygger", "numsehul");

        Media media = testService.getMedia().get(0);
        testService.saveMedia(media);
        //passer hvis current user's saved list >0
        assertTrue(testService.getCurrentUser().getSavedList().contains(media.getTitle()));

        testService.saveMedia(media);
        int count = 0;
        for (String title : testService.getCurrentUser().getSavedList()) {
            if (title.equals(media.getTitle())) {
                count++;
            }
        }
        //passer hvis count = 1 (kun en titel i denne test)
        assertEquals(1, count);
        //så disken bliver ryttet efter tilføjet media (kun for test)
        testService.removeSavedMedia(media);
        System.out.println("Test passed");
    }

    @Test
    public void removeSavedMediaTest()
    {
        testService.registerUser("legobygger", "numsehul");
        testService.login("legobygger", "numsehul");

        Media media = testService.getMedia().get(0);
        testService.saveMedia(media);
        testService.removeSavedMedia(media);
        //passer hvis media ikke findes i savedlist
        assertFalse(testService.getCurrentUser().getSavedList().contains(media.getTitle()));
        System.out.println("Test passed");
    }
}
