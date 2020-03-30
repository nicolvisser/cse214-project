/**
 * InstructionsScreen
 */
public class InstructionsScreen extends KeyListener {

    public boolean flagBack;

    public InstructionsScreen() {
        flagBack = false;
    }

    public void draw() {
        StdDraw.setPenColor(StdDraw.ORANGE);
        StdDraw.text(0, 75, "Controls:");

        StdDraw.setPenColor(StdDraw.WHITE);
        // Todo sort out repetitive code
        StdDraw.textLeft(-50, 50 - 0 * 15, "Move Left");
        StdDraw.textLeft(-50, 50 - 1 * 15, "Move Right");
        StdDraw.textLeft(-50, 50 - 2 * 15, "Rotate Left");
        StdDraw.textLeft(-50, 50 - 3 * 15, "Rotate Right");
        StdDraw.textLeft(-50, 50 - 4 * 15, "Shoot");
        StdDraw.textLeft(-50, 50 - 5 * 15, "Block with Shield");

        StdDraw.textLeft(20, 50 - 0 * 15, "A");
        StdDraw.textLeft(20, 50 - 1 * 15, "D");
        StdDraw.textLeft(20, 50 - 2 * 15, "Left Arrow");
        StdDraw.textLeft(20, 50 - 3 * 15, "Right Arrow");
        StdDraw.textLeft(20, 50 - 4 * 15, "Up Arrow");
        StdDraw.textLeft(20, 50 - 5 * 15, "Down Arrow");

        StdDraw.setPenColor(StdDraw.ORANGE);
        StdDraw.text(0, 50 - 7 * 15, "Press Escape to Go Back To Main Menu");

    }

    public void reset() {
        flagBack = false;
    }

    public void flagToGoBack() {
        flagBack = true;
        StdAudio.play("resources/audio/click7.wav");
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