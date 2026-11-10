import java.util.ArrayList;
import java.util.Arrays;

public class User {
    private String username;
    private ArrayList<String> watchList;
    private ArrayList<String> savedList;

    public User(String username, String watchedListData, String savedListData) {
        this.username = username;
        this.watchList = new ArrayList<String>();
        this.savedList = new ArrayList<String>();

        this.watchList.addAll(Arrays.stream(watchedListData.split("\n")).toList());
        this.savedList.addAll(Arrays.stream(savedListData.split("\n")).toList());
    }

    public String getUsername() {
        return username;
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
