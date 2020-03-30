import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Invaders
 */
public class Invaders {

    enum DisplayState {
        MAIN_MENU, NEW_GAME, PLAYING, PAUSE, SAVE_GAME, LOAD_GAME, SETTINGS, CONTROLS, SET_RESOLUTION, GAME_OVER, QUIT;
    }

    static final RectangleDimension canvas = new RectangleDimension(0, 0, 200, 200);

    static final int FPS = 60;
    static final int dt_ms = 1000 / FPS;
    static final double dt = dt_ms / 1000.0;

    static DisplayState currentDisplayState;
    static InvaderGameState loadedInvaderGameState;

    static String[] mainMenuScreenOptions = { "New Game", "Load Game", "Settings", "Quit Game" };
    static MenuScreen mainMenuScreen = new MenuScreen("Main Menu", mainMenuScreenOptions);

    static String[] pauseScreenOptions = { "Resume Game", "Save Game", "Quit To Main Menu" };
    static MenuScreen pauseScreen = new MenuScreen("Paused", pauseScreenOptions);

    static String[] settingsScreenOptions = { "Set Resolution", "Controls", "Back" };
    static MenuScreen settingsScreen = new MenuScreen("Settings", settingsScreenOptions);

    static String[] resolutionScreenOptions = { "600x600", "800x800", "1000x1000", "Cancel" };
    static MenuScreen resolutionScreen = new MenuScreen("Change Resolution", resolutionScreenOptions);

    static String[] saveGameScreenOptions = { "Slot 1", "Slot 2", "Slot 3", "Slot 4", "Cancel" };
    static MenuScreen saveGameScreen = new MenuScreen("Save Game", saveGameScreenOptions);

    static String[] loadGameScreenOptions = { "Slot 1", "Slot 2", "Slot 3", "Slot 4", "Cancel" };
    static MenuScreen loadGameScreen = new MenuScreen("Load Game", loadGameScreenOptions);

    static GameOverScreen gameOverScreen = new GameOverScreen();

    static InstructionsScreen controlsScreen = new InstructionsScreen();

    public static void main(String[] args) {

        StdDraw.enableDoubleBuffering();

        // Test saving stuff here -------->

        String ts = getPlainTimestamp();

        System.out.println(ts);

        System.out.println(formatTimestamp(ts));

        System.out.println(filenameOfSaveFile(3));

        // <<--------------

        setupStdDrawCanvas(800, 800);

        StdAudio.loop("resources/audio/Mercury.wav");

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

                    mainMenuScreen.draw();
                    mainMenuScreen.listenForInputChanges();

                    switch (mainMenuScreen.selectedOption) {
                        case -1: // not yet selected
                            break;
                        case 0: // new game
                            mainMenuScreen.reset();
                            currentDisplayState = DisplayState.NEW_GAME;
                            break;

                        case 1: // loadgame
                            mainMenuScreen.reset();
                            currentDisplayState = DisplayState.LOAD_GAME;
                            setMenuScreenOptionsFromSaveFiles(loadGameScreen);
                            break;

                        case 2: // settings
                            mainMenuScreen.reset();
                            currentDisplayState = DisplayState.SETTINGS;
                            break;

                        case 3: // not yet selected
                            mainMenuScreen.reset();
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
                            setMenuScreenOptionsFromSaveFiles(saveGameScreen);
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

                    saveGameScreen.draw();
                    saveGameScreen.listenForInputChanges();

                    if (saveGameScreen.flagBack) {
                        saveGameScreen.reset();
                        currentDisplayState = DisplayState.PAUSE;
                        break;
                    }

                    switch (saveGameScreen.selectedOption) {
                        case -1: // not yet selected
                            break;
                        case 0: // slot 1
                        case 1: // slot 2
                        case 2: // slot 3
                        case 3: // slot 4
                            int slot = saveGameScreen.selectedOption + 1;
                            saveInvaderGameState(slot);
                            saveGameScreen.reset();
                            break;
                        case 4: // cancel
                            saveGameScreen.flagToGoBack();
                            break;

                        default:

                    }

                    break;

                case LOAD_GAME:

                    loadGameScreen.draw();
                    loadGameScreen.listenForInputChanges();

                    if (loadGameScreen.flagBack) {
                        loadGameScreen.reset();
                        loadGameScreen.setSubtitle(""); // clear error message if any
                        currentDisplayState = DisplayState.MAIN_MENU;
                        break;
                    }

                    switch (loadGameScreen.selectedOption) {
                        case -1: // not yet selected
                            break;
                        case 0: // slot 1
                        case 1: // slot 2
                        case 2: // slot 3
                        case 3: // slot 4

                            int slot = loadGameScreen.selectedOption + 1;
                            loadInvaderGameState(slot);
                            loadGameScreen.reset();
                            break;
                        case 4: // cancel
                            loadGameScreen.flagToGoBack();
                            break;

                        default:

                    }

                    break;

                case SETTINGS:

                    settingsScreen.draw();
                    settingsScreen.listenForInputChanges();

                    if (settingsScreen.flagBack) {
                        settingsScreen.reset();
                        currentDisplayState = DisplayState.MAIN_MENU;
                    }

                    switch (settingsScreen.selectedOption) {
                        case -1: // not yet selected
                            break;
                        case 0: // set resolution
                            currentDisplayState = DisplayState.SET_RESOLUTION;
                            settingsScreen.reset();
                            break;
                        case 1: // controls
                            currentDisplayState = DisplayState.CONTROLS;
                            settingsScreen.reset();
                            break;
                        case 2: // back
                            settingsScreen.flagToGoBack();
                            break;
                        default:
                            break;
                    }

                    break;

                case CONTROLS:

                    controlsScreen.draw();
                    controlsScreen.listenForInputChanges();

                    if (controlsScreen.flagBack) {
                        controlsScreen.reset();
                        currentDisplayState = DisplayState.MAIN_MENU;
                        break;
                    }

                    switch (controlsScreen.selectedOption) {
                        case -1: // not yet selected
                            break;
                        case 0: // back
                            controlsScreen.flagToGoBack();
                            break;
                        default:
                            break;
                    }

                    break;

                case SET_RESOLUTION:

                    resolutionScreen.draw();
                    resolutionScreen.listenForInputChanges();

                    if (resolutionScreen.flagBack) {
                        currentDisplayState = DisplayState.SETTINGS;
                        resolutionScreen.reset();
                        break;
                    }

                    switch (resolutionScreen.selectedOption) {
                        case -1: // not yet selected
                            break;

                        case 0: // 600x600
                            setupStdDrawCanvas(600, 600);
                            resolutionScreen.reset();
                            currentDisplayState = DisplayState.SETTINGS;
                            break;

                        case 1: // 800x800
                            setupStdDrawCanvas(800, 800);
                            resolutionScreen.reset();
                            currentDisplayState = DisplayState.SETTINGS;
                            break;

                        case 2: // 1000x1000
                            setupStdDrawCanvas(1000, 1000);
                            resolutionScreen.reset();
                            currentDisplayState = DisplayState.SETTINGS;
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

    static void saveInvaderGameState(int slot) {
        loadedInvaderGameState.resetFlags(); // so as not to save true flags in game state
        try {
            // delete old savegame (if any) of this slot first:
            String existingFilename = filenameOfSaveFile(slot);
            if (existingFilename != null) {
                File file = new File(existingFilename);
                if (file.delete()) {
                    System.out.println("Old savegame, " + existingFilename + ", deleted successfully.");
                }
            }

            String filename = "savedata_slot" + slot + "_" + getPlainTimestamp() + ".dat";
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
            out.writeObject(loadedInvaderGameState);
            out.close();
            System.out.println("New savegame, " + filename + ", created successfully.");
            currentDisplayState = DisplayState.PLAYING;
        } catch (Exception e1) {
            // TODO Show message if game failed to saves
            e1.printStackTrace();
        }

    }

    static void loadInvaderGameState(int slot) {
        try {
            String filename = filenameOfSaveFile(slot);
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
            loadedInvaderGameState = (InvaderGameState) in.readObject();
            in.close();
            loadGameScreen.setSubtitle(""); // clear error message if any
            currentDisplayState = DisplayState.PLAYING;
        } catch (Exception e1) {
            // TODO Show message if game failed to load
            loadGameScreen.setSubtitle("Failed to load game from slot " + slot + ".");
            e1.printStackTrace();
        }
    }

    static String getPlainTimestamp() {
        String date = java.time.LocalDate.now().toString().replaceAll("-", "");
        String time = java.time.LocalTime.now().toString().substring(0, 6).replaceAll(":", "");
        return date + time;
    }

    static String formatTimestamp(String plainTimestamp) {
        String date = plainTimestamp.substring(0, 4) + "-" + plainTimestamp.substring(4, 6) + "-"
                + plainTimestamp.substring(6, 8);
        String time = plainTimestamp.substring(8, 10) + ":" + plainTimestamp.substring(10, 12);
        return date + " " + time;
    }

    static String filenameOfSaveFile(int slot) {
        File folder = new File(System.getProperty("user.dir")); // gets "working directory"
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String filename = listOfFiles[i].getName();
                if (filename.startsWith("savedata_slot" + slot)) {
                    return filename;
                }
            }
        }
        return null;
    }

    static void setMenuScreenOptionsFromSaveFiles(MenuScreen menuScreen) {
        String[] options = new String[5];

        for (int i = 0; i < 4; i++) {
            String filename = filenameOfSaveFile(i + 1);
            if (filename != null) {
                options[i] = "Slot " + (i + 1) + " - " + formatTimestamp(filename.substring(15));
            } else {
                options[i] = "Slot " + (i + 1) + " - Empty";
            }
        }

        options[4] = "Cancel";

        menuScreen.setOptions(options);

    }

}