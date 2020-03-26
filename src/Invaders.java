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
        TITLE_SCREEN, NEW_GAME, PLAYING, PAUSE, SAVE_GAME, LOAD_GAME, INSTRUCTIONS, GAME_OVER, QUIT;
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
    static MenuScreen titleScreen = new MenuScreen("Main Menu", titleScreenOptions);

    static String[] pauseScreenOptions = { "Resume Game", "Save Game", "Quit To Main Menu" };
    static MenuScreen pauseScreen = new MenuScreen("Paused", pauseScreenOptions);

    static GameOverScreen gameOverScreen = new GameOverScreen();

    static InstructionsScreen instructionsScreen = new InstructionsScreen();

    public static void main(String[] args) {

        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        StdDraw.setXscale(CANVAS_XMIN, CANVAS_XMAX);
        StdDraw.setYscale(CANVAS_YMIN, CANVAS_YMAX);

        StdAudio.play("resources/audio/Cinematic Sci-fi Beat.wav");

        gameLoop();

        System.exit(0);

        System.out.println("TEST");

    }

    public static void gameLoop() {
        currentDisplayState = DisplayState.TITLE_SCREEN;

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

                    if (loadedInvaderGameState.gameOverFlag) {
                        loadedInvaderGameState.resetFlags();
                        currentDisplayState = DisplayState.GAME_OVER;
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

                case GAME_OVER:

                    gameOverScreen.setScore(loadedInvaderGameState.score);
                    gameOverScreen.draw();
                    gameOverScreen.listenForInputChanges();

                    switch (gameOverScreen.selectedOption) {
                        case -1:
                            break;
                        case 0:
                            gameOverScreen.reset();
                            currentDisplayState = DisplayState.NEW_GAME;
                            break;

                        case 1:
                            gameOverScreen.reset();
                            currentDisplayState = DisplayState.LOAD_GAME;
                            break;

                        case 2:
                            // TODO Save Highscore
                            break;

                        case 3:
                            currentDisplayState = DisplayState.QUIT;
                            break;

                        default:
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

    public static boolean isPointOnCanvas(Vector2D pos) {
        if ((pos.x >= CANVAS_XMIN) && (pos.x <= CANVAS_XMAX) && (pos.y >= CANVAS_YMIN) && (pos.y <= CANVAS_YMAX))
            return true;
        else
            return false;
    }

}