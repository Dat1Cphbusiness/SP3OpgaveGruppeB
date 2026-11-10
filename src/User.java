import java.util.ArrayList;
import java.util.Arrays;

public class User {
    private String username;
    private String password;
    private ArrayList<String> watchList;
    private ArrayList<String> savedList;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.watchList = new ArrayList<>();
        this.savedList = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<String> getWatchList() {
        return watchList;
    }

    public ArrayList<String> getSavedList() {
        return savedList;
    }

    public boolean saveMedia(String title) {
        if (savedList.contains(title)) return false;
        savedList.add(title);
        return true;
    }

    public boolean unsaveMedia(String title) {
        return savedList.remove(title);
    }

    public void watchMedia(String title) {
        if (!watchList.contains(title)) {
            watchList.add(title);
        }
    }

}
