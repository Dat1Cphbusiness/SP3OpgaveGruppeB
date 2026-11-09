public enum Category {

    ACTION("Action"),
    ADVENTURE("Adventure"),
    ANIMATION("Animation"),
    BIOGRAPHY("Biography"),
    COMEDY("Comedy"),
    CRIME("Crime"),
    DOCUMENTARY("Documentary"),
    DRAMA("Drama"),
    FAMILY("Family"),
    FANTASY("Fantasy"),
    FILM_NOIR("Film Noir"),
    HISTORY("History"),
    HORROR("Horror"),
    MUSIC("Music"),
    MUSICAL("Musical"),
    MYSTERY("Mystery"),
    ROMANCE("Romance"),
    SCI_FI("Sci-Fi"),
    SPORT("Sport"),
    TALK_SHOW("Talk Show"),
    THRILLER("Thriller"),
    WAR("War"),
    WESTERN("Western");

    private final String readableFormat;

    private Category(String readableFormat)
    {
        this.readableFormat = readableFormat;
    }


    public static Category fromString (String name) {
        String category = name.trim()
                .toUpperCase()
                .replace("-", "_")
                .replace (" ", "_");
        try {
            return Category.valueOf(category);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String toString()
    {
        return this.readableFormat;
    }

}