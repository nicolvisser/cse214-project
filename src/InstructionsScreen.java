/**
 * InstructionsScreen
 */
public class InstructionsScreen extends MenuScreen {

    static String[] options = { "Back" };

    static String[] actions = { "Move Left", "Move Right", "Rotate Left", "Rotate Right", "Shoot",
            "Block with Shield" };
    static String[] keys = { "A", "D", "Left Arrow", "Right Arrow", "Up Arrow", "Down Arrow" };

    public InstructionsScreen() {
        super("Controls", options);
        flagBack = false;
    }

    public void draw() {

        int y = 60;
        StdDraw.setPenColor(StdDraw.ORANGE);
        StdDraw.text(0, y, title);

        y -= 15;
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.text(0, y, subtitle);

        StdDraw.setPenColor(StdDraw.WHITE);
        for (int i = 0; i < actions.length; i++) {
            y -= 15;
            StdDraw.textLeft(-50, y, actions[i]);
            StdDraw.textLeft(+20, y, keys[i]);
        }

        for (int i = 0; i < textOptions.length; i++) {
            StdDraw.setPenColor(i == highlightedOption ? StdDraw.RED : StdDraw.WHITE);
            y -= (BUTTON_HEIGHT + BUTTON_SPACING);
            StdDraw.rectangle(0, y, BUTTON_WIDTH / 2, BUTTON_HEIGHT / 2);
            StdDraw.text(0, y, textOptions[i]);
        }

    }
}