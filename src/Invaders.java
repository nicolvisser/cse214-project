import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Invaders
 */
public class Invaders {

    enum DisplayState {
        TITLE_SCREEN, NEW_GAME, PLAYING, PAUSE, SAVE_GAME, LOAD_GAME, INSTRUCTIONS, QUIT;
    }

    static final int CANVAS_WIDTH = 800;
    static final int CANVAS_HEIGHT = 800;
    static final int CANVAS_XMIN = -CANVAS_WIDTH / 2;
    static final int CANVAS_XMAX = CANVAS_WIDTH / 2;
    static final int CANVAS_YMIN = 0;
    static final int CANVAS_YMAX = CANVAS_HEIGHT;

    static final int FPS = 60;
    static final int dt_ms = 1000 / FPS;
    static final double dt = dt_ms / 1000.0;

    static DisplayState currentDisplayState;
    static InvaderGameState loadedInvaderGameState;

    static String[] titleScreenOptions = { "New Game", "Load Game", "Instructions", "Quit Game" };
    static MenuScreen titleScreen = new MenuScreen(titleScreenOptions);

    static String[] pauseScreenOptions = { "Resume Game", "Save Game", "Quit To Main Menu" };
    static MenuScreen pauseScreen = new MenuScreen(pauseScreenOptions);

    static InstructionsScreen instructionsScreen = new InstructionsScreen();

    public static void main(String[] args) {

        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        StdDraw.setXscale(CANVAS_XMIN, CANVAS_XMAX);
        StdDraw.setYscale(CANVAS_YMIN, CANVAS_YMAX);

        currentDisplayState = DisplayState.TITLE_SCREEN;

        gameLoop();

        System.exit(0);

    }

    public static void gameLoop() {
        while (currentDisplayState != DisplayState.QUIT) {

            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.filledRectangle(0, 400, 400, 400);

            switch (currentDisplayState) {
                case TITLE_SCREEN:

                    titleScreen.draw();
                    titleScreen.listenForInputChanges();

                    switch (titleScreen.selectedOption) {
                        case -1:
                            break;
                        case 0:
                            titleScreen.reset();
                            currentDisplayState = DisplayState.NEW_GAME;
                            break;

                        case 1:
                            titleScreen.reset();
                            currentDisplayState = DisplayState.LOAD_GAME;
                            break;

                        case 2:
                            titleScreen.reset();
                            currentDisplayState = DisplayState.INSTRUCTIONS;
                            break;

                        case 3:
                            titleScreen.reset();
                            currentDisplayState = DisplayState.QUIT;

                            break;

                        default:
                            break;
                    }

                    break;

                case NEW_GAME:

                    loadedInvaderGameState = new InvaderGameState(CANVAS_XMIN, CANVAS_XMAX, CANVAS_YMIN, CANVAS_YMAX);
                    currentDisplayState = DisplayState.PLAYING;

                    break;

                case PLAYING:

                    loadedInvaderGameState.draw();
                    loadedInvaderGameState.renderStep(dt);
                    loadedInvaderGameState.listenForInputChanges();

                    if (loadedInvaderGameState.pauseFlag) {
                        loadedInvaderGameState.resetFlags();
                        currentDisplayState = DisplayState.PAUSE;
                        break;
                    }

                    if (loadedInvaderGameState.quitFlag) {
                        loadedInvaderGameState.resetFlags();
                        currentDisplayState = DisplayState.QUIT;
                        break;
                    }

                    break;

                case PAUSE:

                    pauseScreen.draw();
                    pauseScreen.listenForInputChanges();

                    if (pauseScreen.flagBack) {
                        pauseScreen.reset();
                        currentDisplayState = DisplayState.PLAYING;
                        break;
                    }

                    switch (pauseScreen.selectedOption) {
                        case -1:
                            break;
                        case 0:
                            pauseScreen.reset();
                            currentDisplayState = DisplayState.PLAYING;
                            break;

                        case 1:
                            pauseScreen.reset();
                            currentDisplayState = DisplayState.SAVE_GAME;
                            break;

                        case 2:
                            pauseScreen.reset();
                            currentDisplayState = DisplayState.TITLE_SCREEN;
                            break;

                        default:
                            break;
                    }

                    break;

                case SAVE_GAME:

                    loadedInvaderGameState.resetFlags(); // so as not to save true flags in game state

                    ObjectOutputStream out;
                    try {
                        out = new ObjectOutputStream(new FileOutputStream("savedata.txt"));
                        out.writeObject(loadedInvaderGameState);
                        out.close();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                    currentDisplayState = DisplayState.PAUSE;

                    break;

                case LOAD_GAME:

                    try {
                        ObjectInputStream in = new ObjectInputStream(new FileInputStream("savedata.txt"));
                        loadedInvaderGameState = (InvaderGameState) in.readObject();
                        in.close();
                    } catch (IOException | ClassNotFoundException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                    currentDisplayState = DisplayState.PLAYING;

                    break;

                case INSTRUCTIONS:

                    instructionsScreen.draw();
                    instructionsScreen.listenForInputChanges();

                    if (instructionsScreen.flagBack) {
                        instructionsScreen.reset();
                        currentDisplayState = DisplayState.TITLE_SCREEN;
                        break;
                    }

                    break;

                default:
                    break;
            }

            StdDraw.show();
            StdDraw.pause(dt_ms);

        }
    }

}