import java.awt.event.KeyEvent;

public class KeyListener {

    public KeyListener() {

    }

    enum KeyboardKey {
        ESC_KEY(KeyEvent.VK_ESCAPE), A_KEY(KeyEvent.VK_A), D_KEY(KeyEvent.VK_D), Q_KEY(KeyEvent.VK_Q),
        LEFT_ARROW(KeyEvent.VK_LEFT), RIGHT_ARROW(KeyEvent.VK_RIGHT), UP_ARROW(KeyEvent.VK_UP),
        DOWN_ARROW(KeyEvent.VK_DOWN), ENTER_KEY(KeyEvent.VK_ENTER), SPACE(KeyEvent.VK_SPACE);

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