public class HighScoreScreen extends MenuScreen {

    static final int NUM_ENTRIES = 10;
    static final String[] OPTIONS = { "Reset", "Back" };

    String[] names;
    int[] scores;
    int highlightedScore;

    public HighScoreScreen() {
        super("High Scores", OPTIONS);
        names = new String[NUM_ENTRIES];
        scores = new int[NUM_ENTRIES];
        highlightedScore = -1;
        loadFromFile();
    }

    public void loadFromFile() {
        In in = new In("highscores.txt");
        for (int i = 0; i < NUM_ENTRIES; i++) {
            names[i] = in.readLine();
            scores[i] = Integer.parseInt(in.readLine());
        }
        in.close();
    }

    public void saveToFile() {
        Out out = new Out("highscores.txt");
        for (int i = 0; i < NUM_ENTRIES; i++) {
            out.println(names[i]);
            out.println(scores[i]);
        }
        out.close();
    }

    public void addEntry(String name, int score) {
        if (isNewHighScore(score)) {

            names[NUM_ENTRIES - 1] = name;
            scores[NUM_ENTRIES - 1] = score;

            for (int i = NUM_ENTRIES - 2; i >= 0; i--) {
                if (scores[i + 1] >= scores[i]) {
                    String tempName = names[i];
                    names[i] = names[i + 1];
                    names[i + 1] = tempName;

                    int tempScore = scores[i];
                    scores[i] = scores[i + 1];
                    scores[i + 1] = tempScore;

                    highlightedScore = i; // highlight last index to show this user's high score on next draw
                } else {
                    break;
                }
            }

            saveToFile();
        }
    }

    public boolean isNewHighScore(int score) {
        return score > scores[NUM_ENTRIES - 1];
    }

    public void resetHighScores() {
        for (int i = 0; i < NUM_ENTRIES; i++) {
            names[i] = "**********";
            scores[i] = 0;
        }
        saveToFile();
    }

    public void setHighlightedScore(int index) {
        highlightedScore = index;
    }

    public void resetHighlightedScore() {
        highlightedScore = -1;
    }

    public void draw() {
        int y = 80;
        StdDraw.setPenColor(StdDraw.ORANGE);
        StdDraw.text(0, y, "High Scores");

        y -= 15;
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.text(0, y, "");

        for (int i = 0; i < NUM_ENTRIES; i++) {
            y -= 10;

            StdDraw.setPenColor(i == highlightedScore ? StdDraw.GREEN : StdDraw.WHITE);

            StdDraw.textLeft(-60, y, "" + (i + 1));
            StdDraw.textLeft(-50, y, names[i]);
            StdDraw.textRight(60, y, "" + scores[i]);
        }

        for (int i = 0; i < textOptions.length; i++) {
            y -= (BUTTON_HEIGHT + BUTTON_SPACING);

            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.filledRectangle(0, y, BUTTON_WIDTH / 2, BUTTON_HEIGHT / 2);

            StdDraw.setPenColor(i == highlightedOption ? StdDraw.RED : StdDraw.WHITE);
            StdDraw.rectangle(0, y, BUTTON_WIDTH / 2, BUTTON_HEIGHT / 2);

            StdDraw.text(0, y, textOptions[i]);
        }
    }

}