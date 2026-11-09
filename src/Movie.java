import java.util.ArrayList;

public class Movie extends Media {

    public Movie (String title, int releaseYear, double rating, ArrayList<Category> categories) {
        super (title, releaseYear, rating, categories);
    }

    @Override
    public void play () {
        System.out.println(getTitle() + " afspilles nu...");
    }
}
