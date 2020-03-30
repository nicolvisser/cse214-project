/**
 * TitleScreen
 */
public class MenuScreen extends KeyListener {

    static final int BUTTON_WIDTH = 100;
    static final int BUTTON_HEIGHT = 10;
    static final int BUTTON_SPACING = 5;

    public String title;
    public String subtitle;

    public String[] textOptions;
    public int highlightedOption;

    public int selectedOption;

    public MenuScreen(String title, String[] textOptionsArray) {
        this(title, "", textOptionsArray);
    }

    public MenuScreen(String title, String subtitle, String[] textOptionsArray) {
        this.title = title;
        this.subtitle = subtitle;
        textOptions = textOptionsArray;
        highlightedOption = 0;
        selectedOption = -1;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setOptions(String[] textOptionsArray) {
        textOptions = textOptionsArray;
    }

    public void resetSelection() {
        selectedOption = -1;
    }

    public void resetHiglight() {
        highlightedOption = 0;
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
                selectOptionToGoBack();
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

    public void selectOptionToGoBack() {
        selectedOption = -2;
        StdAudio.play("resources/audio/click7.wav");
    }

    public void draw() {
        StdDraw.setPenColor(StdDraw.ORANGE);
        StdDraw.text(0, 50, title);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.text(0, 35, subtitle);
        for (int i = 0; i < textOptions.length; i++) {
            StdDraw.setPenColor(i == highlightedOption ? StdDraw.RED : StdDraw.WHITE);
            double y = 20 - i * (BUTTON_HEIGHT + BUTTON_SPACING);
            StdDraw.rectangle(0, y, BUTTON_WIDTH / 2, BUTTON_HEIGHT / 2);
            StdDraw.text(0, y, textOptions[i]);
        }
    }

}