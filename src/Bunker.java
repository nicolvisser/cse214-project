import java.awt.Color;

public class Bunker {

    enum BlockState {
        EDGE, INNER, DESTROYED;
    }

    Rectangle bunkerArea;
    int M, N;
    BlockState[][] blocks;

    public Bunker(Rectangle bunkerArea, int M, int N) {
        this.bunkerArea = bunkerArea;
        this.M = M; // num rows
        this.N = N; // num cols
        blocks = new BlockState[M][N];
        for (int row = 0; row < M; row++) {
            for (int col = 0; col < N; col++) {
                blocks[row][col] = BlockState.INNER;
            }
        }
        findEdges();
    }

    public void findEdges() {

        for (int col = 0; col < N; col++) {
            // find from top
            for (int row = 0; row < M; row++) {
                if (blocks[row][col] != BlockState.DESTROYED) {
                    blocks[row][col] = BlockState.EDGE;
                    break;
                }
            }

            // find from top
            for (int row = M - 1; row >= 0; row--) {
                if (blocks[row][col] != BlockState.DESTROYED) {
                    blocks[row][col] = BlockState.EDGE;
                    break;
                }
            }
        }

        for (int row = 0; row < M; row++) {
            // find from top
            for (int col = 0; col < N; col++) {
                if (blocks[row][col] != BlockState.DESTROYED) {
                    blocks[row][col] = BlockState.EDGE;
                    break;
                }
            }

            // find from top
            for (int col = N - 1; col >= 0; col--) {
                if (blocks[row][col] != BlockState.DESTROYED) {
                    blocks[row][col] = BlockState.EDGE;
                    break;
                }
            }
        }
    }

    public void draw() {

        double blockHalfWidth = bunkerArea.getWidth() / N / 2;
        double blockHalfHeight = bunkerArea.getHeight() / M / 2;

        for (int row = 0; row < M; row++) {
            double y = bunkerArea.getYmin() + blockHalfHeight + row * 2 * blockHalfHeight;
            for (int col = 0; col < N; col++) {
                double x = bunkerArea.getXmin() + blockHalfWidth + col * 2 * blockHalfWidth;
                int randColorValue = 150 + (int) (Math.random() * 30);

                Color randColor;
                switch (blocks[row][col]) {
                    case EDGE:
                        randColor = new Color(randColorValue, 0, 0);
                        StdDraw.setPenColor(randColor);
                        StdDraw.filledRectangle(x, y, blockHalfWidth, blockHalfHeight);
                        break;
                    case INNER:
                        randColor = new Color(0, 0, randColorValue);
                        StdDraw.setPenColor(randColor);
                        StdDraw.filledRectangle(x, y, blockHalfWidth, blockHalfHeight);
                        break;
                    case DESTROYED:
                        break;

                }
            }
        }

    }

    public static void main(String[] args) {

    }
}