import java.util.ArrayList;

public abstract class Media {

    private String title;
    private int releaseYear;
    private double rating;
    private ArrayList<Category> categories;

    public Media (String title, int releaseYear, double rating,ArrayList<Category> categories){
        this.title = title;
        this.releaseYear = releaseYear;
        this.rating = rating;
        this.categories = categories;
    }

    public String getTitle () {
        return title;
    }

    public int getInitialReleaseYear () {
        return releaseYear;
    }

    public double getRating () {
        return rating;
    }

    public ArrayList<Category> getCategories () {
        return categories;
    }

    @Override
    public String toString () {
        return title + " (" + releaseYear + ") - Rating: " + rating;
    }

}
