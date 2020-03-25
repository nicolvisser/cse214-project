/**
 * InstructionsScreen
 */
public class InstructionsScreen extends KeyListener {

    public boolean flagBack = false;

    public void draw() {
        StdDraw.setPenColor(StdDraw.WHITE);

        StdDraw.textLeft(-200, 600 - 0 * 25, "Move Left");
        StdDraw.textLeft(-200, 600 - 1 * 25, "Move Right");
        StdDraw.textLeft(-200, 600 - 2 * 25, "Rotate Left");
        StdDraw.textLeft(-200, 600 - 3 * 25, "Rotate Right");
        StdDraw.textLeft(-200, 600 - 4 * 25, "Shoot");

        StdDraw.textLeft(100, 600 - 0 * 25, "A");
        StdDraw.textLeft(100, 600 - 1 * 25, "D");
        StdDraw.textLeft(100, 600 - 2 * 25, "Left Arrow");
        StdDraw.textLeft(100, 600 - 3 * 25, "Right Arrow");
        StdDraw.textLeft(100, 600 - 4 * 25, "Up Arrow");

    }

    public void reset() {
        flagBack = false;
    }

    @Override
    public void onKeyPress(KeyboardKey key) {
        switch (key) {
            case ESC_KEY:
                flagBack = true;
                break;

            default:
                break;
        }
    }
}