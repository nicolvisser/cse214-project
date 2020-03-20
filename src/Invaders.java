import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    static DisplayState currentDisplayState;
    static InvaderGameState loadedInvaderGameState;
    static int FPS = 60;
    static int dt_ms = 1000 / FPS;
    static double dt = dt_ms / 1000.0;

    public static void main(String[] args) {

        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize(800, 800);
        StdDraw.setXscale(-400, 400);
        StdDraw.setYscale(0, 800);

        currentDisplayState = DisplayState.TITLE_SCREEN;

        gameLoop();

        System.exit(0);

    }

    public static void gameLoop() {
        while (currentDisplayState != DisplayState.QUIT) {
            switch (currentDisplayState) {
                case TITLE_SCREEN:

                    TitleScreen.draw();
                    TitleScreen.listenForInputChanges();

                    if (StdDraw.isKeyPressed(KeyEvent.VK_ENTER)) {
                        switch (TitleScreen.titleScreenSelectedOption) {
                            case 0:
                                currentDisplayState = DisplayState.NEW_GAME;
                                break;

                            case 1:
                                currentDisplayState = DisplayState.LOAD_GAME;
                                break;

                            case 2:
                                currentDisplayState = DisplayState.QUIT;
                                break;

                            default:
                                break;
                        }
                    }

                    break;

                case NEW_GAME:

                    loadedInvaderGameState = new InvaderGameState();
                    currentDisplayState = DisplayState.PLAYING;

                    break;

                case PLAYING:

                    loadedInvaderGameState.draw();
                    loadedInvaderGameState.renderStep(dt);
                    loadedInvaderGameState.listenForInputChanges();

                    if (StdDraw.isKeyPressed(KeyEvent.VK_ESCAPE)) {
                        currentDisplayState = DisplayState.PAUSE;
                    }

                    break;

                case PAUSE:

                    PauseScreen.draw();
                    PauseScreen.listenForInputChanges();

                    if (StdDraw.isKeyPressed(KeyEvent.VK_ENTER)) {
                        switch (PauseScreen.titleScreenSelectedOption) {
                            case 0:
                                currentDisplayState = DisplayState.PLAYING;
                                break;

                            case 1:
                                currentDisplayState = DisplayState.SAVE_GAME;
                                break;

                            case 2:
                                currentDisplayState = DisplayState.QUIT;
                                break;

                            default:
                                break;
                        }
                    }

                    break;

                case SAVE_GAME:

                    try {
                        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("savedata.txt"));
                        out.writeObject(loadedInvaderGameState);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    currentDisplayState = DisplayState.PAUSE;

                    break;

                case LOAD_GAME:

                    try {
                        ObjectInputStream in = new ObjectInputStream(new FileInputStream("savedata.txt"));
                        loadedInvaderGameState = (InvaderGameState) in.readObject();

                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    currentDisplayState = DisplayState.PLAYING;

                    break;

                default:
                    break;
            }

            StdDraw.show();
            StdDraw.pause(dt_ms);

        }
    }

}