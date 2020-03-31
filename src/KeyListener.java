import java.awt.event.KeyEvent;

public class KeyListener {

    public KeyListener() {

    }

    enum KeyboardKey {
        ESC_KEY(27), A_KEY(65), D_KEY(68), Q_KEY(81), LEFT_ARROW(37), RIGHT_ARROW(39), UP_ARROW(38), DOWN_ARROW(40),
        ENTER_KEY(10);

        int i = KeyEvent.VK_1;

        public int keyCode;
        public boolean isDown;

        private KeyboardKey(int value) {
            this.keyCode = value;
            this.isDown = false;
        }
    }

    public void listenForInputChanges() {
        /**
         * for each key in the set of keys used by game, get the key's new state from
         * StdDraw. If the state changed from previous, update the new state and call
         * appropriate function to handle the change event.
         *
         **/
        for (KeyboardKey key : KeyboardKey.values()) {
            boolean keyIsDownInNewFrame = StdDraw.isKeyPressed(key.keyCode);
            if (!key.isDown && keyIsDownInNewFrame) {
                key.isDown = true;
                onKeyPress(key);
            } else if (key.isDown && !keyIsDownInNewFrame) {
                key.isDown = false;
                onKeyRelease(key);
            } else if (key.isDown && keyIsDownInNewFrame) {
                onKeyHold(key);
            }
        }
    }

    public void onKeyPress(KeyboardKey key) {

    }

    public void onKeyHold(KeyboardKey key) {

    }

    public void onKeyRelease(KeyboardKey key) {

    }
}