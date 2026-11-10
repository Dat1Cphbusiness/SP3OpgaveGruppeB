import java.util.Scanner;

public class TextUI {
private final StreamingService streamingService;
private Scanner input = new Scanner(System.in);

boolean shutdown = false;

public TextUI () {
    this.streamingService = new StreamingService();
}

    private void showBrowseOptions(){

}

public void displayUI() {
    System.out.println("Welcome to StreamingService!");
    System.out.println("Please choose one of the following:");
    System.out.println("1. Select User.");
    System.out.println("2. Register User.");
    System.out.println("3. Exit.");

    while (!shutdown) {
        String userInput = input.nextLine().trim();

        switch (userInput) {
            case "1":
                System.out.println("Here is available users");
                break;
            case "2":
                System.out.println("Insert username, then password");
                //logic
                break;
            case "3":
                exitStreamingService();
                break;
            default:
                System.out.println("Please choose between 1-3");

        }

    }

}

public void exitStreamingService(){
    input.close();
    shutdown = true;
    System.out.println("StreamingService has been shutdown");
    System.exit(0);
}

}
