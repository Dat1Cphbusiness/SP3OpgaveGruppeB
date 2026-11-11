import Helpers.FileIO;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

    public StreamingService() {
        name = "Streaming Service";
        users = new ArrayList<>();
        media = new ArrayList<>();
        if (!FileIO.fileExists(USERS_PATH)) {
            try {
                FileIO.createEmptyFile(USERS_PATH);
            }
            catch (IOException e)
            {
                System.out.println("Error creating \"" + USERS_PATH + "\" - Users will not be saved!!!");
            }
        }
        loadMediaData();
        loadUsers();

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

    public ArrayList<Movie> browseMovies()
    {
        ArrayList<Movie> movies = new ArrayList<>();

        ArrayList<Media> tmpMovies = new ArrayList<>();
        tmpMovies.addAll(media.stream().filter(m -> m instanceof Movie).toList());
        for(int i = 0; i < tmpMovies.size(); i++)
        {
            movies.add((Movie)tmpMovies.get(i));
        }
        return movies;
    }

    public ArrayList<Series> browseSeries()
    {
        ArrayList<Series> series = new ArrayList<>();

        ArrayList<Media> tmpSeries = new ArrayList<>();
        tmpSeries.addAll(media.stream().filter(m -> m instanceof Series).toList());
        for(int i = 0; i < tmpSeries.size(); i++)
        {
            series.add((Series)tmpSeries.get(i));
        }
        return series;
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

    public Arraylist<Media> getWatchedMedia() {
        Arraylist<Media> result = new Arraylist<>();
        if (currentUser == null) {
            return result;
        }
        for (String title : currentUser.getWatchlist()) {
            Media item = findMediaByTitle(title);
            if (item != null) {
                result.add(item);
            }
        }
        return result;
    }

    // ------------------------------
    // SAVED MEDIA
    // ------------------------------

    public Arraylist<Media> getSavedList() {
        Arraylist<Media> result = new Arraylist();
        if (currentUser == null) {
            return result;
        }
        for (String title : currentUser.getSavedList()) {
            Media item = findMediaByTitle(title)
            if (item != null) {
                result.add(item)
            }
        }
        return result;
    }

    public boolean saveMedia(Media item) {
        if (currentUser == null || item == null) return false;
        boolean saved = currentUser.saveMedia(item.getTitle());
        if (saved) saveUserData();
        return saved;
    }

    public boolean removeSavedMedia(Media item) {
        if (currentUser == null || item == null) return false;
        boolean removed = currentUser.unsaveMedia(item.getTitle());
        if (removed) saveUserData();
        return removed;
    }

    public boolean isMediaSaved(Media item) {
        return currentUser != null && item != null
                && currentUser.getSavedList().contains(item.getTitle());
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
            String[] lines = data.split("\n");
            if(lines.length <= 1)
            {
                System.out.println("Only header available in moviesdata file - Skipping movie loading...");
                return;
            }
            for (int i = 1; i < lines.length; i++) {
                String[] parts = lines[i].split(";");
                try {
                    String title = parts[0].trim();
                    int year = Integer.parseInt(parts[1].trim());
                    ArrayList<Category> categories = parseCategories(parts[2]);
                    double rating = Double.parseDouble(parts[3].trim().replace(",", "."));
                    Movie movie = new Movie(title, year, rating, categories);
                    media.add(movie);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: Failed to parse either release_year or rating for movie CSV line: \"" + lines[i] + "\"");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: Movies data not found - Movies will appear empty!!");
        }
    }

    private void loadSeries() {
        try {
            String data = FileIO.readData(SERIES_PATH);
            String[] lines = data.split("\n");
            if(lines.length <= 1)
            {
                System.out.println("Only header in datafile - Skipping series loading...");
                return;
            }
            for (int i = 1; i < lines.length; i++) {
                String[] parts = lines[i].split(";");
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
                    for(int u = 4; u < parts.length; u++){
                        seasonData.append(parts[u]).append(" ");
                    }
                    Matcher matcher = Pattern.compile("(\\d+)\\s*-\\s*(\\d+)").matcher(seasonData.toString());
                    while (matcher.find()) {
                        int seasonNumber = Integer.parseInt(matcher.group(1));
                        int episodeNumber = Integer.parseInt(matcher.group(2));
                        Season season = new Season (seasonNumber);
                        for (int u = 1; u <= episodeNumber; u++) {
                            season.addEpisode(new Episode(u));
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
            String[] lines = data.split("\n");
            if(lines.length <= 1)
            {
                System.out.println("Userdata file only has header - Skipping userload...");
                return;
            }
            for (int i = 1; i < lines.length; i++) {
                String[] parts = lines[i].split(";");
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
        data.append("username;password\n");
        for (User user : users) {
            data.append(user.getUsername())
                    .append(";")
                    .append(user.getPassword())
                    .append("\n");
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
        StringBuilder data = new StringBuilder();
        data.append("username;list_type;title\n");
        for (User user : users) {
            for (String item : user.getWatchList()) {
                data.append(user.getUsername())
                        .append(";WATCHED;")
                        .append(item)
                        .append("\n");
            }
            for (String item : user.getSavedList()) {
                data.append(user.getUsername())
                        .append(";SAVED;")
                        .append(item)
                        .append("\n");
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
            String[] lines = data.split("\n");
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
