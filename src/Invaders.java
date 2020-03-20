/**
 * Invaders
 */
public class Invaders {

    static final int TITLE_SCREEN_OPTIONS_NUM = 3;
    static final String[] TITLE_SCREEN_OPTIONS_TEXT = { "New Game", "Instructions", "Quit" };

    static final int TITLE_SCREEN_BUTTON_WIDTH = 200;
    static final int TITLE_SCREEN_BUTTON_HEIGHT = 40;
    static final int TITLE_SCREEN_BUTTON_SPACING = 10;

    static int titleScreenSelectedOption = 0;

    public static void main(String[] args) {

        StdDraw.enableDoubleBuffering();

        StdDraw.setCanvasSize(800, 800);
        StdDraw.setXscale(-400, 400);
        StdDraw.setYscale(0, 800);

        titleScreenLoop();

    }

    public static void titleScreenLoop() {
        while (true) {
            drawTitleScreen();
            StdDraw.show();
            StdDraw.pause(1000 / 10);

            // controls select up
            if (StdDraw.isKeyPressed(40)) {
                titleScreenSelectedOption = Math.min(titleScreenSelectedOption + 1, TITLE_SCREEN_OPTIONS_NUM - 1);
            }

            // controls select down
            if (StdDraw.isKeyPressed(38)) {
                titleScreenSelectedOption = Math.max(titleScreenSelectedOption - 1, 0);
            }

            if (StdDraw.isKeyPressed(10)) {

                switch (titleScreenSelectedOption) {
                    case 0: // new game

                        InvaderGameState game = new InvaderGameState();
                        game.start();

                        break;

                    case 1: // instructions

                        // Todo add view instructions logic in here

                        break;

                    case 2: // quit

                        // Todo add quit logic in here

                        break;

                    default:
                        break;
                }

            }
        }
    }

    public static void drawTitleScreen() {
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledRectangle(0, 400, 400, 400);
        for (int i = 0; i < TITLE_SCREEN_OPTIONS_NUM; i++) {
            StdDraw.setPenColor(i == titleScreenSelectedOption ? StdDraw.RED : StdDraw.WHITE);
            int y = 600 - i * (TITLE_SCREEN_BUTTON_HEIGHT + TITLE_SCREEN_BUTTON_SPACING);
            StdDraw.rectangle(0, y, TITLE_SCREEN_BUTTON_WIDTH / 2, TITLE_SCREEN_BUTTON_HEIGHT / 2);
            StdDraw.textRight(0, y, TITLE_SCREEN_OPTIONS_TEXT[i]);
        }
    }
}