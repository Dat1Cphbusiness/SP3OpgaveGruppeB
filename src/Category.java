public enum Category {

    ACTION,
    ADVENTURE,
    ANIMATION,
    BIOGRAPHY,
    COMEDY,
    CRIME,
    DOCUMENTARY,
    DRAMA,
    FAMILY,
    FANTASY,
    FILM_NOIR,
    HISTORY,
    HORROR,
    MUSIC,
    MUSICAL,
    MYSTERY,
    ROMANCE,
    SCI_FI,
    SPORT,
    TALK_SHOW,
    THRILLER,
    WAR,
    WESTEREN;

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
}