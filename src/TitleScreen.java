/**
 * TitleScreen
 */
public class TitleScreen {

    static final int TITLE_SCREEN_OPTIONS_NUM = 3;
    static final String[] TITLE_SCREEN_OPTIONS_TEXT = { "New Game", "Load Game", "Quit" };

    static final int TITLE_SCREEN_BUTTON_WIDTH = 200;
    static final int TITLE_SCREEN_BUTTON_HEIGHT = 40;
    static final int TITLE_SCREEN_BUTTON_SPACING = 10;

    static int titleScreenSelectedOption = 0;

    enum KeyboardKey {
        UP(38), DOWN(40);

        public int keyCode;
        public boolean isDown;

        private KeyboardKey(int value) {
            this.keyCode = value;
            this.isDown = false;
        }
    }

    public static void listenForInputChanges() {
        /**
         * for each key in the set of keys used by game, get the key's new state from
         * StdDraw. If the state changed from previous, update the new state and call
         * appropriate function to handle the change event.
         *
         **/
        for (KeyboardKey key : KeyboardKey.values()) {
            boolean keyIsDownInNewFrame = StdDraw.isKeyPressed(key.keyCode);
            if (!key.isDown && keyIsDownInNewFrame) {
                key.isDown = true;
                onKeyPress(key);
            } else if (key.isDown && !keyIsDownInNewFrame) {
                key.isDown = false;
                onKeyRelease(key);
            }
        }
    }

    private static void onKeyPress(KeyboardKey key) {
        switch (key) {
            case UP:
                changeSelectionUp();
                break;
            case DOWN:
                changeSelectionDown();
                break;

            default:
                break;
        }
    }

    private static void onKeyRelease(KeyboardKey key) {
        switch (key) {

            default:
                break;
        }
    }

    public static void changeSelectionUp() {
        titleScreenSelectedOption--;
        if (titleScreenSelectedOption < 0)
            titleScreenSelectedOption += TITLE_SCREEN_OPTIONS_NUM;
    }

    public static void changeSelectionDown() {
        titleScreenSelectedOption++;
        if (titleScreenSelectedOption >= TITLE_SCREEN_OPTIONS_NUM)
            titleScreenSelectedOption -= TITLE_SCREEN_OPTIONS_NUM;
    }

    public static void draw() {
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