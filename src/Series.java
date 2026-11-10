
import java.util.ArrayList;

public class Series extends Media {

    private ArrayList<Season> seasons;

    public Series(String title, int releaseYear, double rating, ArrayList<Category> categories) {
        super(title, releaseYear, rating, categories);
        seasons = new ArrayList<>();
    }

    public void addSeason(Season season) {
        seasons.add(season);
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    @Override
    public void play() {
        System.out.println("Serien: " + getTitle() + " afspilles nu...");
    }

    @Override
    public String toString() {
        return super.toString() + " - Seasons: " + seasons.size();
    }

}

