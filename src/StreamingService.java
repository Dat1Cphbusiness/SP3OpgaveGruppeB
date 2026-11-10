import Helpers.FileIO;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StreamingService {
    private String name;
    private ArrayList<User> users;
    private ArrayList<Media> media;
    private User currentUser;
    private final String MOVIE_PATH = "data/MovieData.csv";
    private final String SERIES_PATH = "data/SeriesData.csv";
    private final String USERS_PATH = "data/users.csv";
    private final String LISTS_PATH = "data/userLists.csv";
    private final String USER_WATCHED_PATH = "data/userWatched.csv";

    public StreamingService() {
        name = "Streaming Service";
        users = new ArrayList<>();
        media = new ArrayList<>();
        loadMediaData();
        loadUsers();
        if (FileIO.fileExists(USERS_PATH)) {
            saveUsers();
        }
    }

    // ------------------------------
    // USER MANAGEMENT
    // ------------------------------

    public boolean registerUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                System.out.println("Username already exists.");
                return false;
            }
        }
        User newUser = new User(username, password);
        users.add(newUser);
        currentUser = newUser;
        saveUsers();
        return true;

    }

    public boolean login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    public void logout() {
        saveUserData();
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    // ------------------------------
    // MEDIA MANAGEMENT
    // ------------------------------

    public ArrayList<Media> getMedia() {
        return media;
    }

    public ArrayList<Media> browseAllMedia() {
        return new ArrayList<>(media);
    }

    public ArrayList<Media> searchMedia(String search) {
        ArrayList<Media> results = new ArrayList<>();
        for (Media item : media) {
            if (item.getTitle().toLowerCase().contains(search.toLowerCase())) {
                results.add(item);
            }
        }
        return results;
    }

    public ArrayList<Media> browseByCategory(Category category) {
        ArrayList<Media> results = new ArrayList<>();
        for (Media item : media) {
            if (item.hasCategory(category)) {
                results.add(item);
            }
        }
        return results;
    }

    // ------------------------------
    // WATCHED MEDIA
    // ------------------------------
    public void watchMedia(Media item) {
        if (currentUser == null) {
            return;
        }
        item.play();
        currentUser.watchMedia(item.getTitle());
        saveUserData();
    }

    // ------------------------------
    // SAVED MEDIA
    // ------------------------------
    public void saveMedia(Media item) {
        if (currentUser == null) {
            return;
        }
        currentUser.saveMedia(item.getTitle());
        saveUserData();
    }

    public void removeSavedMedia(Media item) {
        if (currentUser == null) {
            return;
        }
        currentUser.unsaveMedia(item.getTitle());
        saveUserData();
    }

    // ------------------------------
    // FIND MEDIA
    // ------------------------------
    public Media findMediaByTitle(String title) {
        for (Media item : media) {
            if (item.getTitle().equalsIgnoreCase(title)) {
                return item;
            }
        }
        return null;
    }

    // ------------------------------
    // LOAD MOVIES AND SERIES
    // ------------------------------
    private void loadMediaData() {

        loadMovies();
        loadSeries();
    }

    private void loadMovies() {

        try {
            String data = FileIO.readData(MOVIE_PATH);
            String[] lines = data.split("\\R");
            int i = 0;
            for (String line : lines) {
                if (i == 0) {
                    i++;
                    continue;
                }
                String[] parts = line.split(";");
                try {
                    String title = parts[0].trim();
                    int year = Integer.parseInt(parts[1].trim());
                    ArrayList<Category> categories = parseCategories(parts[2]);
                    double rating = Double.parseDouble(parts[3].trim().replace(",", "."));
                    Movie movie = new Movie(title, year, rating, categories);
                    media.add(movie);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: Failed to parse either release_year or rating for movie CSV line: \"" + line + "\"");
                }
                i++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: Movies data not found - Movies will appear empty!!");
        }
    }

    private void loadSeries() {
        try {
            String data = FileIO.readData(SERIES_PATH);
            String[] lines = data.split("\n");
            for (String line : lines) {
                String[] parts = line.split(";");
                try {
                    String title = parts[0].trim();
                    String yearText = parts[1].trim();
                    Matcher yearMatcher = Pattern.compile("\\d{4}").matcher(yearText);

                    if(!yearMatcher.find()) {
                        continue;
                    }
                    int year = Integer.parseInt(yearMatcher.group());
                    ArrayList<Category> categories = parseCategories(parts[2]);
                    double rating = Double.parseDouble(parts[3].trim().replace(",", "."));

                    Series series = new Series(title, year, rating, categories);

                    // Find season and episode information.

                    StringBuilder seasonData = new StringBuilder();
                    for(int i = 4; i < parts.length; i++){
                        seasonData.append(parts[i]).append(" ");
                    }
                    Matcher matcher = Pattern.compile("(\\d+)\\s*-\\s*(\\d+)").matcher(seasonData.toString());
                    while (matcher.find()) {
                        int seasonNumber = Integer.parseInt(matcher.group(1));
                        int episodeNumber = Integer.parseInt(matcher.group(2));
                        Season season = new Season (seasonNumber);
                        for (int i = 1; i <= episodeNumber; i++) {
                            season.addEpisode(new Episode(i));
                        }
                        series.addSeason(season);
                    }
                    media.add(series);
                } catch (IllegalArgumentException e) {

                }
            }
        } catch (IOException e) {
            System.out.println("Error: Could not load SeriesData.csv");
        }
    }

    //------------------------------
    // CATEGORY CONVERSION
    //------------------------------
    private ArrayList<Category> parseCategories(String data) {
        ArrayList<Category> categories = new ArrayList<>();
        String[] values = data.split(",");
        for(String value : values) {
            try {
                Category category = Category.fromString(value);
                if (!categories.contains(category)) {
                    categories.add(category);
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Failed to parse category: \"" + value + "\" - Ignoring as failsafe...");
            }
        }
        return categories;
    }

    //------------------------------
    // LOAD USERS
    //------------------------------
    private void loadUsers() {
        try {
            String data = FileIO.readData(USERS_PATH);
            String[] lines = data.split("\\R");
            for (String line : lines) {
                String[] parts = line.split(";");
                if (parts.length != 2) {
                    continue;
                }
                User user = new User(parts[0].trim(), parts[1].trim());
                users.add(user);
            }
            loadUserLists();
        } catch (IOException e) {
            System.out.println(
                    "No previous users found. Starting fresh."
            );
        }
    }

    //------------------------------
    // SAVE USERS
    //------------------------------
    private void saveUsers() {
        StringBuilder data = new StringBuilder();
        for (User user : users) {
            data.append(user.getUsername())
                    .append(";")
                    .append(user.getPassword())
                    .append(System.lineSeparator());
        }
        try {
            FileIO.writeData(USERS_PATH, data.toString(), false);
        } catch (IOException e) {
            System.out.println("Error: Could not save user information.");
        }
    }

    //------------------------------
    // SAVE USER LIST/DATA
    //------------------------------
    public void saveUserData() {
        saveUsers();
        StringBuilder data = new StringBuilder();
        for (User user : users) {
            for (String item : user.getWatchList()) {
                data.append(user.getUsername())
                        .append(";WATCHED;")
                        .append(item)
                        .append(System.lineSeparator());
            }
            for (String item : user.getSavedList()) {
                data.append(user.getUsername())
                        .append(";SAVED;")
                        .append(item)
                        .append(System.lineSeparator());
            }
        }

        try {
            FileIO.writeData(LISTS_PATH, data.toString(), false);
        } catch (IOException e) {
            System.out.println("Error: Could not save user lists.");
        }
    }

    //------------------------------
    //LOAD USER LIST/DATA
    //------------------------------
    private void loadUserLists() {
        try {
            String data = FileIO.readData(LISTS_PATH);
            String[] lines = data.split("\\R");
            for (String line : lines) {
                String[] parts = line.split(";");
                if (parts.length != 3) {
                    continue;
                }
                String username = parts[0].trim();
                String listType = parts[1].trim();
                String title = parts[2].trim();
                User user = null;
                for (User existingUser : users) {
                    if (existingUser.getUsername()
                            .equals(username)) {
                        user = existingUser;
                        break;
                    }
                }
                if (user == null) {
                    continue;
                }
                Media item = findMediaByTitle(title);
                if (item == null) {
                    continue;
                }
                if (listType.equals("WATCHED")) {
                    user.watchMedia(item.getTitle());
                } else if (listType.equals("SAVED")) {
                    user.saveMedia(item.getTitle());
                }
            }
        } catch (IOException e) {
            System.out.println("No previous user lists found.");
        }
    }

}
