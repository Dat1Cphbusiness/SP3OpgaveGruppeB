import java.util.Scanner;
import java.util.ArrayList;

public class TextUI {
    private final StreamingService streamingService;
    private Scanner input = new Scanner(System.in);

    boolean shutdown = false;

    public TextUI() {
        this.streamingService = new StreamingService();
    }

    public void displayUI() {
        System.out.println("Welcome to StreamingService!");
        while (!shutdown) {
            if (streamingService.getCurrentUser() == null) {
                showStartMenu();
            } else {
                showBrowseOptions();
            }
        }
    }

    private void showStartMenu() {
        System.out.println("1. Select user");
        System.out.println("2. Register user");
        System.out.println("3. Exit");

        int choice = readChoice();

        //har ikke brug for loop siden det allerede looper på displayUI()
        switch (choice) {
            case 1:
                System.out.println("Input username");
                String username = input.nextLine();
                System.out.println("Input password");
                String password = input.nextLine();
                if (!streamingService.login(username, password)) {
                    System.out.println("Wrong username and/or password.");
                }
                break;
            case 2:
                System.out.println("Input new username");
                String newName = input.nextLine();
                System.out.println("Input new password");
                String newPassword = input.nextLine();
                streamingService.registerUser(newName, newPassword);
                break;
            case 3:
                exitStreamingService();
                break;
            default:
                System.out.println("Invalid choice, pick between 1-3.");
        }

    }

    private void showBrowseOptions() {
        System.out.println("1. Search title");
        System.out.println("2. Browse category");
        System.out.println("3. Browse watched");
        System.out.println("4. Browse saved");
        System.out.println("5. Logout");

        int choice = readChoice();

        switch (choice) {
            case 1:
                System.out.println("Input title");
                String search = input.nextLine();
                chooseMedia(streamingService.searchMedia(search));
                break;
            case 2:
                chooseCategory();
                break;
            case 3:
                chooseMedia(streamingService.getWatchedMedia());
                break;
            case 4:
                chooseMedia(streamingService.getSavedMedia());
                break;
            case 5:
                streamingService.logout();
                break;
            default:
                System.out.println("Invalid choice, pick between 1-5.");
        }
    }

    private void chooseCategory() {
        Category[] categories = Category.values();
        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i]);
        }

        int choice = readChoice();

        if (choice >= 1 && choice <= categories.length) {
            Category category = categories[choice - 1];
            chooseMedia(streamingService.browseByCategory(category));
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private void chooseMedia(ArrayList<Media> mediaList) {
        if (mediaList.isEmpty()) {
            System.out.println("Nothing to show.");
            return;
        }
        for (int i = 0; i < mediaList.size(); i++) {
            System.out.println((i + 1) + ". " + mediaList.get(i));
        }
        System.out.println("0. Back");

        int choice = readChoice();

        if (choice >= 1 && choice <= mediaList.size()) {
            Media media = mediaList.get(choice - 1);
            showMediaMenu(media);
        }
    }

    private void showMediaMenu(Media media) {
        boolean saved = streamingService.isMediaSaved(media);

        System.out.println(media);
        System.out.println("1. Watch");
        if (saved) {
            System.out.println("2. Remove from saved");
        } else {
            System.out.println("2. Save");
        }
        System.out.println("0. Back");

        int choice = readChoice();

        switch (choice) {
            case 1:
                streamingService.watchMedia(media);
                break;
            case 2:
                if (saved) {
                    streamingService.removeSavedMedia(media);
                    System.out.println(media.getTitle() + " was removed from your saved list.");
                } else {
                    streamingService.saveMedia(media);
                    System.out.println(media.getTitle() + " was added to your saved list.");
                }
                break;
            case 0:
                break;
            default:
                System.out.println("Invalid choice, pick between 0-2.");
        }
    }

    //læser et tal fra brugeren
    private int readChoice() {
        //bruger skal inputte et tal ellers spørger den igen
        while (!input.hasNextInt()) {
            System.out.println("Please input a number.");
            input.nextLine();
        }
        int choice = input.nextInt();
        //scanner bug
        input.nextLine();
        return choice;
    }

    public void exitStreamingService() {
        input.close();
        shutdown = true;
        System.out.println("StreamingService has been shutdown");
        System.exit(0);
    }
    
}