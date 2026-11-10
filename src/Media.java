
import java.util.ArrayList;

public abstract class Media {

    private String title;
    private int releaseYear;
    private ArrayList<Category> categories;
    private double rating;

    public Media(String title, int releaseYear,
                 ArrayList<Category> categories, double rating) {

        this.title = title;
        this.releaseYear = releaseYear;
        this.categories = categories;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public int getInitialReleaseYear() {
        return releaseYear;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public double getRating() {
        return rating;
    }

    public boolean hasCategory(Category category) {
        return categories.contains(category);
    }

    @Override
    public String toString() {

        return title + " (" + releaseYear + ") - Rating: " + rating;
    }
}