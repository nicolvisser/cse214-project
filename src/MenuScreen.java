/**
 * TitleScreen
 */
public class MenuScreen extends KeyListener {

    static final int BUTTON_WIDTH = 300;
    static final int BUTTON_HEIGHT = 40;
    static final int BUTTON_SPACING = 10;

    private String[] textOptions;
    private int highlightedOption;

    public int selectedOption;

    public MenuScreen(String[] textOptionsArray) {
        textOptions = textOptionsArray;
        highlightedOption = 0;
        selectedOption = -1;
    }

    public void reset() {
        highlightedOption = 0;
        selectedOption = -1;
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
                selectedOption = highlightedOption;
                break;

            default:
                break;
        }
    }

    public void changeSelectionUp() {
        highlightedOption--;
        if (highlightedOption < 0)
            highlightedOption += textOptions.length;
    }

    public void changeSelectionDown() {
        highlightedOption++;
        if (highlightedOption >= textOptions.length)
            highlightedOption -= textOptions.length;
    }

    public void draw() {
        for (int i = 0; i < textOptions.length; i++) {
            StdDraw.setPenColor(i == highlightedOption ? StdDraw.RED : StdDraw.WHITE);
            int y = 600 - i * (BUTTON_HEIGHT + BUTTON_SPACING);
            StdDraw.rectangle(0, y, BUTTON_WIDTH / 2, BUTTON_HEIGHT / 2);
            StdDraw.text(0, y, textOptions[i]);
        }
    }

}