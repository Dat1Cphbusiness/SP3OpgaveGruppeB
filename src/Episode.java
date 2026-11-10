public class Episode {

    private int number;

    public Episode(int number) {

        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void play() {
        System.out.println("Episode " + number + " afspilles nu...");
    }

}
