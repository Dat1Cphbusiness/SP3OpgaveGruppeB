import java.util.ArrayList;

public class Season {

    private int seasonNumber;
    private ArrayList<Episode> episodes;

    public Season(int seasonNumber) {

        this.seasonNumber = seasonNumber;
        episodes = new ArrayList<>();
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public void addEpisode(Episode episode) {
        episodes.add(episode);
    }

    public ArrayList<Episode> getEpisodes() {
        return episodes;
    }

    @Override
    public String toString() {
        return "Season: " + seasonNumber + " - Episodes: " + episodes.size();
    }

}
