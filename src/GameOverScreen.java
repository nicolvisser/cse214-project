/**
 * TitleScreen
 */
public class GameOverScreen extends MenuScreen {

    static String[] options = { "New Game", "Load Game", "Save Highscore", "Quit Game" };

    int score;

    public GameOverScreen() {
        super("GAME OVER", options);
        score = 0;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void draw() {
        StdDraw.setPenColor(StdDraw.ORANGE);
        StdDraw.text(0, 600, title);
        StdDraw.text(0, 550, "Your Score: " + score);
        for (int i = 0; i < textOptions.length; i++) {
            StdDraw.setPenColor(i == highlightedOption ? StdDraw.RED : StdDraw.WHITE);
            int y = 500 - i * (BUTTON_HEIGHT + BUTTON_SPACING);
            StdDraw.rectangle(0, y, BUTTON_WIDTH / 2, BUTTON_HEIGHT / 2);
            StdDraw.text(0, y, textOptions[i]);
        }
    }

}