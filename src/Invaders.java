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
        MAIN_MENU, NEW_GAME, PLAYING, PAUSE, SAVE_GAME, LOAD_GAME, INSTRUCTIONS, SET_RESOLUTION, GAME_OVER, QUIT;
    }

    static final RectangleDimension canvas = new RectangleDimension(0, 0, 200, 200);

    static final int FPS = 60;
    static final int dt_ms = 1000 / FPS;
    static final double dt = dt_ms / 1000.0;

    static DisplayState currentDisplayState;
    static InvaderGameState loadedInvaderGameState;

    static String[] titleScreenOptions = { "New Game", "Load Game", "Instructions", "Set Resolution", "Quit Game" };
    static MenuScreen titleScreen = new MenuScreen(canvas, "Main Menu", titleScreenOptions);

    static String[] pauseScreenOptions = { "Resume Game", "Save Game", "Quit To Main Menu" };
    static MenuScreen pauseScreen = new MenuScreen(canvas, "Paused", pauseScreenOptions);

    static String[] resolutionScreenOptions = { "600x600", "800x800", "1000x1000", "Cancel" };
    static MenuScreen resolutionScreen = new MenuScreen(canvas, "Change Resolution", resolutionScreenOptions);

    static GameOverScreen gameOverScreen = new GameOverScreen(canvas);

    static InstructionsScreen instructionsScreen = new InstructionsScreen(canvas);

    public static void main(String[] args) {

        StdDraw.enableDoubleBuffering();

        setupStdDrawCanvas(800, 800);

        // stopped music for now, since irritating, will get beter music and add
        // feautures later
        // StdAudio.play("resources/audio/Cinematic Sci-fi Beat.wav");

        gameLoop();

        System.exit(0);

    }

    public static void setupStdDrawCanvas(int width, int height) {
        StdDraw.setCanvasSize(width, height);
        StdDraw.setXscale(canvas.xmin, canvas.xmax);
        StdDraw.setYscale(canvas.ymin, canvas.ymax);
    }

    public static void gameLoop() {
        currentDisplayState = DisplayState.MAIN_MENU;

        while (currentDisplayState != DisplayState.QUIT) {

            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.filledRectangle(0, 0, canvas.width / 2, canvas.height / 2);

            switch (currentDisplayState) {
                case MAIN_MENU:

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
                            currentDisplayState = DisplayState.SET_RESOLUTION;
                            break;

                        case 4:
                            titleScreen.reset();
                            currentDisplayState = DisplayState.QUIT;

                            break;

                        default:
                            break;
                    }

                    break;

                case NEW_GAME:

                    loadedInvaderGameState = new InvaderGameState(canvas);
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
                            currentDisplayState = DisplayState.MAIN_MENU;
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
                        loadedInvaderGameState.setCanvasDimension(canvas); // change saved game canvas dimensions to
                                                                           // current canvas
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
                        currentDisplayState = DisplayState.MAIN_MENU;
                        break;
                    }

                    break;

                case SET_RESOLUTION:

                    resolutionScreen.draw();
                    resolutionScreen.listenForInputChanges();

                    if (resolutionScreen.flagBack) {
                        currentDisplayState = DisplayState.MAIN_MENU;
                        resolutionScreen.reset();
                    }

                    switch (resolutionScreen.selectedOption) {
                        case -1:
                            break;

                        case 0: // 600x600
                            setupStdDrawCanvas(600, 600);
                            resolutionScreen.reset();
                            currentDisplayState = DisplayState.MAIN_MENU;
                            break;

                        case 1: // 800x800
                            setupStdDrawCanvas(800, 800);
                            resolutionScreen.reset();
                            currentDisplayState = DisplayState.MAIN_MENU;
                            break;

                        case 2: // 1000x1000
                            setupStdDrawCanvas(1000, 1000);
                            resolutionScreen.reset();
                            currentDisplayState = DisplayState.MAIN_MENU;
                            break;

                        case 3: // cancel
                            resolutionScreen.flagToGoBack();
                            break;

                        default:
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
                            currentDisplayState = DisplayState.MAIN_MENU;
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

}