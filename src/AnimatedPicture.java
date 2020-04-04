import java.io.Serializable;

public class AnimatedPicture implements Serializable {

    private static final long serialVersionUID = 1L;

    enum AnimationType {
        ONCE, REPEAT, FWD_BWD_ONCE, FWD_BWD_REPEAT;
    }

    String filenameNoIndex;
    String extension;
    int numFrames;
    AnimationType animationType;

    int currentFrameIndex;
    boolean isFinished;

    boolean reverseIncrement;

    public AnimatedPicture(String filenameNoIndex, String extension, int numFrames, AnimationType animationType) {
        this.filenameNoIndex = filenameNoIndex;
        this.extension = extension;
        this.numFrames = numFrames;
        this.animationType = animationType;

        currentFrameIndex = 0;
        isFinished = false;

        reverseIncrement = false;
    }

    public void reset() {
        currentFrameIndex = 0;
        isFinished = false;
        reverseIncrement = false;
    }

    public void draw(double x, double y) {
        if (!isFinished) {
            String filename = String.format("%s%05d.%s", filenameNoIndex, currentFrameIndex, extension);
            StdDraw.picture(x, y, filename);
            updateAnimationProgress();
        }
    }

    public void draw(double x, double y, double degrees) {
        if (!isFinished) {
            String filename = String.format("%s%05d.%s", filenameNoIndex, currentFrameIndex, extension);
            StdDraw.picture(x, y, filename, degrees);
            updateAnimationProgress();
        }
    }

    public void draw(double x, double y, double scaledWidth, double scaledHeight) {
        if (!isFinished) {
            String filename = String.format("%s%05d.%s", filenameNoIndex, currentFrameIndex, extension);
            StdDraw.picture(x, y, filename, scaledWidth, scaledHeight);
            updateAnimationProgress();
        }
    }

    public void draw(double x, double y, double scaledWidth, double scaledHeight, double degrees) {
        if (!isFinished) {
            String filename = String.format("%s%05d.%s", filenameNoIndex, currentFrameIndex, extension);
            StdDraw.picture(x, y, filename, scaledWidth, scaledHeight, degrees);
            updateAnimationProgress();
        }
    }

    private void updateAnimationProgress() {
        if (reverseIncrement) {
            currentFrameIndex--;
        } else {
            currentFrameIndex++;
        }

        switch (animationType) {
            case ONCE:
                if (currentFrameIndex == numFrames) {
                    isFinished = true;
                }
                break;
            case REPEAT:
                if (currentFrameIndex == numFrames) {
                    currentFrameIndex -= numFrames;
                }

                break;

            case FWD_BWD_ONCE:
                if (currentFrameIndex == numFrames) {
                    currentFrameIndex--;
                    reverseIncrement = true;
                }
                if (currentFrameIndex == -1 && reverseIncrement) {
                    isFinished = true;
                }

                break;

            case FWD_BWD_REPEAT:
                if (currentFrameIndex == numFrames && !reverseIncrement) {
                    currentFrameIndex--;
                    reverseIncrement = true;
                }
                if (currentFrameIndex == -1 && reverseIncrement) {
                    currentFrameIndex++;
                    reverseIncrement = false;
                }

                break;

            default:
                break;
        }
    }
}