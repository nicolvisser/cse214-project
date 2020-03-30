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
        MAIN_MENU, NEW_GAME, PLAYING, PAUSE, SAVE_GAME, LOAD_GAME, HIGH_SCORES, SETTINGS, CONTROLS, SET_RESOLUTION,
        GAME_OVER, QUIT;
    }

    static final RectangleDimension canvas = new RectangleDimension(0, 0, 200, 200);

    static final int FPS = 60;
    static final int dt_ms = 1000 / FPS;
    static final double dt = dt_ms / 1000.0;

    static DisplayState currentDisplayState;
    static InvaderGameState loadedInvaderGameState;

    static String[] mainMenuScreenOptions = { "New Game", "Load Game", "High Scores", "Settings", "Quit Game" };
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

    static HighScoreScreen highScoreScreen = new HighScoreScreen();

    static GameOverScreen gameOverScreen = new GameOverScreen();

    static ControlsScreen controlsScreen = new ControlsScreen();

    static Background background;

    public static void main(String[] args) {

        StdDraw.enableDoubleBuffering();

        setupStdDrawCanvas(800, 800); // using default 'resolution' of 800x800

        background = new Background(canvas);

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
            background.draw();

            if (currentDisplayState != DisplayState.PLAYING) {
                background.renderStep(dt, new Vector2D(200, 200));
            }

            switch (currentDisplayState) {
                case MAIN_MENU:

                    mainMenuScreen.draw();
                    mainMenuScreen.listenForInputChanges();

                    switch (mainMenuScreen.selectedOption) {
                        case -2: // back (nowhere to go back to)
                        case -1: // not yet selected
                            break;

                        case 0: // new game
                            mainMenuScreen.resetSelection();
                            currentDisplayState = DisplayState.NEW_GAME;
                            break;

                        case 1: // loadgame
                            mainMenuScreen.resetSelection();
                            currentDisplayState = DisplayState.LOAD_GAME;
                            setMenuScreenOptionsFromSaveFiles(loadGameScreen);
                            break;

                        case 2: // high scores
                            mainMenuScreen.resetSelection();
                            currentDisplayState = DisplayState.HIGH_SCORES;
                            break;

                        case 3: // settings
                            mainMenuScreen.resetSelection();
                            currentDisplayState = DisplayState.SETTINGS;
                            break;

                        case 4: // quit
                            currentDisplayState = DisplayState.QUIT;
                            break;

                        default:
                            break;
                    }

                    break;

                case NEW_GAME:

                    // todo remove this as display state and make function for it

                    loadedInvaderGameState = new InvaderGameState(canvas);
                    currentDisplayState = DisplayState.PLAYING;

                    break;

                case PLAYING:

                    loadedInvaderGameState.draw();
                    loadedInvaderGameState.renderStep(dt);
                    loadedInvaderGameState.listenForInputChanges();

                    background.renderStep(dt, loadedInvaderGameState.getShooterVelocity());

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

                    switch (pauseScreen.selectedOption) {
                        case -2: // back (to playing game)
                            pauseScreen.resetSelection();
                            currentDisplayState = DisplayState.PLAYING;

                        case -1: // not yet selected
                            break;

                        case 0: // resume game
                            pauseScreen.selectOptionToGoBack();
                            break;

                        case 1: // save game
                            pauseScreen.resetSelection();
                            currentDisplayState = DisplayState.SAVE_GAME;
                            setMenuScreenOptionsFromSaveFiles(saveGameScreen);
                            break;

                        case 2: // quit to main menu
                            pauseScreen.resetSelection();
                            pauseScreen.resetHiglight();
                            currentDisplayState = DisplayState.MAIN_MENU;
                            break;

                        default:
                            break;
                    }

                    break;

                case SAVE_GAME:

                    saveGameScreen.draw();
                    saveGameScreen.listenForInputChanges();

                    switch (saveGameScreen.selectedOption) {
                        case -2: // back (to pause screen)
                            saveGameScreen.resetSelection();
                            saveGameScreen.resetHiglight();
                            currentDisplayState = DisplayState.PAUSE;
                            break;

                        case -1: // not yet selected
                            break;

                        case 0: // slot 1
                        case 1: // slot 2
                        case 2: // slot 3
                        case 3: // slot 4
                            int slot = saveGameScreen.selectedOption + 1;
                            saveInvaderGameState(slot);
                            saveGameScreen.resetSelection();
                            break;

                        case 4: // cancel
                            saveGameScreen.selectOptionToGoBack();
                            break;

                        default:
                            break;
                    }

                    break;

                case LOAD_GAME:

                    loadGameScreen.draw();
                    loadGameScreen.listenForInputChanges();

                    switch (loadGameScreen.selectedOption) {
                        case -2: // back (to main menu)
                            loadGameScreen.resetSelection();
                            loadGameScreen.resetHiglight();
                            loadGameScreen.setSubtitle(""); // clear error message if any
                            currentDisplayState = DisplayState.MAIN_MENU;
                            break;

                        case -1: // not yet selected
                            break;

                        case 0: // slot 1
                        case 1: // slot 2
                        case 2: // slot 3
                        case 3: // slot 4
                            int slot = loadGameScreen.selectedOption + 1;
                            if (loadInvaderGameState(slot)) {
                                loadGameScreen.resetHiglight();
                                currentDisplayState = DisplayState.PLAYING;
                            } else {
                                loadGameScreen.setSubtitle("Failed to load game from slot " + slot + ".");
                            }
                            loadGameScreen.resetSelection();

                            break;

                        case 4: // cancel
                            loadGameScreen.selectOptionToGoBack();

                        default:
                            break;
                    }

                    break;

                case HIGH_SCORES:

                    highScoreScreen.draw();
                    highScoreScreen.listenForInputChanges();

                    switch (highScoreScreen.selectedOption) {
                        case -2: // back (to main menu)
                            highScoreScreen.resetSelection();
                            highScoreScreen.resetHiglight();
                            currentDisplayState = DisplayState.MAIN_MENU;
                            break;

                        case -1: // not yet selected
                            break;

                        case 0: // reset
                            highScoreScreen.resetHighScores();
                            highScoreScreen.resetSelection();
                            break;

                        case 1: // back
                            highScoreScreen.selectOptionToGoBack();
                            break;

                        default:
                            break;
                    }

                    break;

                case SETTINGS:

                    settingsScreen.draw();
                    settingsScreen.listenForInputChanges();

                    switch (settingsScreen.selectedOption) {
                        case -2: // back (to main menu)
                            settingsScreen.resetSelection();
                            settingsScreen.resetHiglight();
                            currentDisplayState = DisplayState.MAIN_MENU;
                            break;

                        case -1: // not yet selected
                            break;

                        case 0: // set resolution
                            currentDisplayState = DisplayState.SET_RESOLUTION;
                            settingsScreen.resetSelection();
                            break;

                        case 1: // controls
                            currentDisplayState = DisplayState.CONTROLS;
                            settingsScreen.resetSelection();
                            break;

                        case 2: // back
                            settingsScreen.selectOptionToGoBack();
                            break;

                        default:
                            break;
                    }

                    break;

                case CONTROLS:

                    controlsScreen.draw();
                    controlsScreen.listenForInputChanges();

                    switch (controlsScreen.selectedOption) {
                        case -2: // back (to settings screen)
                            controlsScreen.resetSelection();
                            controlsScreen.resetHiglight();
                            currentDisplayState = DisplayState.SETTINGS;
                            break;

                        case -1: // not yet selected
                            break;

                        case 0: // back
                            controlsScreen.selectOptionToGoBack();
                            break;

                        default:
                            break;
                    }

                    break;

                case SET_RESOLUTION:

                    resolutionScreen.draw();
                    resolutionScreen.listenForInputChanges();

                    switch (resolutionScreen.selectedOption) {
                        case -2: // back (to setting screen)
                            currentDisplayState = DisplayState.SETTINGS;
                            resolutionScreen.resetSelection();
                            resolutionScreen.resetHiglight();
                            break;

                        case -1: // not yet selected
                            break;

                        case 0: // 600x600
                            setupStdDrawCanvas(600, 600);
                            resolutionScreen.resetSelection();
                            break;

                        case 1: // 800x800
                            setupStdDrawCanvas(800, 800);
                            resolutionScreen.resetSelection();
                            break;

                        case 2: // 1000x1000
                            setupStdDrawCanvas(1000, 1000);
                            resolutionScreen.resetSelection();
                            break;

                        case 3: // cancel
                            resolutionScreen.selectOptionToGoBack();
                            break;

                        default:
                            break;
                    }

                    break;

                case GAME_OVER:

                    int score;

                    if (loadedInvaderGameState != null) {
                        score = loadedInvaderGameState.score;

                        if (highScoreScreen.isNewHighScore(score) && loadedInvaderGameState != null) {
                            highScoreScreen.addEntry("No Name", loadedInvaderGameState.score);
                        }

                        String[] options = { "Enter Name", "Back to Main Menu" };
                        highScoreScreen.setOptions(options);

                        loadedInvaderGameState = null;
                    }

                    highScoreScreen.draw();
                    highScoreScreen.listenForInputChanges();

                    switch (highScoreScreen.selectedOption) {
                        case -2: // back (to main menu)
                            highScoreScreen.resetSelection();
                            highScoreScreen.resetHiglight();
                            currentDisplayState = DisplayState.MAIN_MENU;
                            break;

                        case -1: // not yet selected
                            break;

                        case 0: // enter name
                            // todo renaming high score username
                            break;

                        case 1: // back
                            highScoreScreen.selectOptionToGoBack();
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

    static boolean loadInvaderGameState(int slot) {
        try {
            String filename = filenameOfSaveFile(slot);
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
            loadedInvaderGameState = (InvaderGameState) in.readObject();
            in.close();
            loadGameScreen.setSubtitle(""); // clear error message if any
            return true;
        } catch (Exception e1) {
            // e1.printStackTrace();
            return false;
            // dealt with error message for user inside gameloop using return value
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