/**
 * TitleScreen
 */
public class MenuScreen extends KeyListener {

    static final int BUTTON_WIDTH = 100;
    static final int BUTTON_HEIGHT = 10;
    static final int BUTTON_SPACING = 5;

    public String title;
    public String[] textOptions;
    public int highlightedOption;

    public int selectedOption;

    public boolean flagBack = false;

    public MenuScreen(String title, String[] textOptionsArray) {
        this.title = title;
        textOptions = textOptionsArray;
        highlightedOption = 0;
        selectedOption = -1;
    }

    public void reset() {
        highlightedOption = 0;
        selectedOption = -1;
        flagBack = false;
    }

    @Override
    public void onKeyPress(KeyboardKey key) {
        switch (key) {
            case UP_ARROW:
                changeSelectionUp();
                break;
            case DOWN_ARROW:
                changeSelectionDown();
                break;
            case ENTER_KEY:
                selectCurrentOption();
                break;
            case ESC_KEY:
                flagToGoBack();
                break;

            default:
                break;
        }
    }

    public void changeSelectionUp() {
        highlightedOption--;
        if (highlightedOption < 0)
            highlightedOption += textOptions.length;
        StdAudio.play("resources/audio/cut.wav");
    }

    public void changeSelectionDown() {
        highlightedOption++;
        if (highlightedOption >= textOptions.length)
            highlightedOption -= textOptions.length;
        StdAudio.play("resources/audio/cut.wav");
    }

    public void selectCurrentOption() {
        selectedOption = highlightedOption;
        StdAudio.play("resources/audio/click7.wav");
    }

    public void flagToGoBack() {
        flagBack = true;
        StdAudio.play("resources/audio/click7.wav");
    }

    public void draw() {
        StdDraw.setPenColor(StdDraw.ORANGE);
        StdDraw.text(0, 50, title);
        for (int i = 0; i < textOptions.length; i++) {
            StdDraw.setPenColor(i == highlightedOption ? StdDraw.RED : StdDraw.WHITE);
            double y = 20 - i * (BUTTON_HEIGHT + BUTTON_SPACING);
            StdDraw.rectangle(0, y, BUTTON_WIDTH / 2, BUTTON_HEIGHT / 2);
            StdDraw.text(0, y, textOptions[i]);
        }
    }

}