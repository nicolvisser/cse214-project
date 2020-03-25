/**
 * AnimatedPicture
 */
public class AnimatedPicture {

    enum AnimationType {
        ONCE, REPEAT, FWD_BWD_ONCE, FWD_BWD_REPEAT;
    }

    String filenameNoIndex;
    String extension;
    int numFrames;
    AnimationType animationType;

    int currentFrameIndex;
    boolean finished;

    boolean reverseIncrement;

    public AnimatedPicture(String filenameNoIndex, String extension, int numFrames, AnimationType animationType) {
        this.filenameNoIndex = filenameNoIndex;
        this.extension = extension;
        this.numFrames = numFrames;
        this.animationType = animationType;

        currentFrameIndex = 0;
        finished = false;

        reverseIncrement = false;
    }

    public void reset() {
        currentFrameIndex = 0;
        finished = false;
        reverseIncrement = false;
    }

    public void draw(double x, double y, double degrees) {
        if (!finished) {
            String filename = String.format("%s%05d.%s", filenameNoIndex, currentFrameIndex, extension);

            // StdOut.println(filename);

            StdDraw.picture(x, y, filename, degrees);

            if (reverseIncrement) {
                currentFrameIndex--;
            } else {
                currentFrameIndex++;
            }

            switch (animationType) {
                case ONCE:
                    if (currentFrameIndex == numFrames) {
                        finished = true;
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
                        finished = true;
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
}