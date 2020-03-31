import java.io.Serializable;

public class Background implements Serializable {

    private static final long serialVersionUID = 1L;

    Rectangle canvas;

    private final int NUM_STARS = 1000;
    private Vector2D[] starPositions = new Vector2D[NUM_STARS];
    private double[] starParallaxSensitivities = new double[NUM_STARS];
    private final double STARS_PARALLAX_SENSITIVITY_MAX = 0.083;

    public Background(Rectangle canvas) {
        this.canvas = canvas;

        for (int i = 0; i < NUM_STARS; i++) {
            // set random position for each star within background bounds
            double xpos = canvas.getXmin() + Math.random() * canvas.getWidth();
            double ypos = canvas.getYmin() + Math.random() * canvas.getHeight();
            starPositions[i] = new Vector2D(xpos, ypos);

            // set random parallax sensitivity for each star
            starParallaxSensitivities[i] = Math.random() * STARS_PARALLAX_SENSITIVITY_MAX;
        }

    }

    // renders position of each star and earth based on velocity of player to give
    // parallax effect while moving
    public void render(double dt, Vector2D playerVelocity) {

        for (int i = 0; i < NUM_STARS; i++) {

            // star position
            starPositions[i].x -= playerVelocity.x * dt * starParallaxSensitivities[i];
            starPositions[i].y -= playerVelocity.y * dt * starParallaxSensitivities[i];

            // recycle stars that go out of x bounds
            if (starPositions[i].x < canvas.getXmin()) {
                starPositions[i].x = canvas.getXmax();
            } else if (starPositions[i].x > canvas.getXmax()) {
                starPositions[i].x = canvas.getXmin();
            }

            // recycle stars that go out of y bounds
            if (starPositions[i].y < canvas.getYmin()) {
                starPositions[i].y = canvas.getYmax();
            } else if (starPositions[i].y > canvas.getYmax()) {
                starPositions[i].y = canvas.getYmin();
            }
        }
    }

    public void draw() {

        // draw black space background
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledRectangle(0, 0, canvas.getWidth() / 2, canvas.getHeight() / 2);

        for (int i = 0; i < NUM_STARS; i++) {
            // simulate flickering of stars by letting star brightness be random greyscale
            // value between [130,255]
            int grayLevel = 130 + (int) (Math.random() * 126);
            StdDraw.setPenColor(grayLevel, grayLevel, grayLevel);
            StdDraw.point(starPositions[i].x, starPositions[i].y);
        }
    }
}