/**
 * InstructionsScreen
 */
public class InstructionsScreen extends KeyListener {

    public boolean flagBack;
    private RectangleDimension canvas;

    public InstructionsScreen(RectangleDimension canvas) {
        flagBack = false;
        this.canvas = canvas;
    }

    public void draw() {
        StdDraw.setPenColor(StdDraw.WHITE);

        // Todo sort out repetitive code
        StdDraw.textLeft(-50, 50 - 0 * 25, "Move Left");
        StdDraw.textLeft(-50, 50 - 1 * 25, "Move Right");
        StdDraw.textLeft(-50, 50 - 2 * 25, "Rotate Left");
        StdDraw.textLeft(-50, 50 - 3 * 25, "Rotate Right");
        StdDraw.textLeft(-50, 50 - 4 * 25, "Shoot");

        StdDraw.textLeft(20, 50 - 0 * 25, "A");
        StdDraw.textLeft(20, 50 - 1 * 25, "D");
        StdDraw.textLeft(20, 50 - 2 * 25, "Left Arrow");
        StdDraw.textLeft(20, 50 - 3 * 25, "Right Arrow");
        StdDraw.textLeft(20, 50 - 4 * 25, "Up Arrow");

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