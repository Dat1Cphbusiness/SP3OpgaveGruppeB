public class StreamingServiceTester {

    private StreamingService testService;

    public StreamingServiceTester()
    {
        this.testService = new StreamingService();
    }

    public void startTests()
    {
        loadMediaTest();
        addUserTest();
    }

    private void addUserTest()
    {
        String username = "Bent";
        testService.registerUser(username,"SomeGreatPassword");
        testService.getUsers().stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username+"d"));
    }
    private void loadMediaTest()
    {
        System.out.println(testService.getMedia());
    }
}
