/**
 * TitleScreen
 */
public class GameOverScreen extends MenuScreen {

    static String[] options = { "Play Again", "Load Game", "Save Highscore", "Quit to Main Menu" };

    int score;

    public GameOverScreen(RectangleDimension canvas) {
        super(canvas, "GAME OVER", options);
        score = 0;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void draw() {
        StdDraw.setPenColor(StdDraw.ORANGE);
        StdDraw.text(0, 50, title);
        StdDraw.text(0, 35, "Your Score: " + score);
        for (int i = 0; i < textOptions.length; i++) {
            StdDraw.setPenColor(i == highlightedOption ? StdDraw.RED : StdDraw.WHITE);
            double y = 20 - i * (BUTTON_HEIGHT + BUTTON_SPACING);
            StdDraw.rectangle(0, y, BUTTON_WIDTH / 2, BUTTON_HEIGHT / 2);
            StdDraw.text(0, y, textOptions[i]);
        }
    }

}