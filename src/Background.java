import java.io.Serializable;

/**
 * Background
 */
public class Background implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    RectangleDimension canvas;

    // declare variables associated with stars
    private final int NUM_STARS = 1000;
    private Vector2D[] starPositions = new Vector2D[NUM_STARS];
    private double[] starParallaxSensitivities = new double[NUM_STARS];
    private final double STARS_PARALLAX_SENSITIVITY_MAX = 0.083;

    // declare variables associated with earth
    private Vector2D earthPosition;
    private final double EARTH_PARALLAX_SENSITIVITY = 0.1;

    public Background(RectangleDimension canvas) {
        this.canvas = canvas;
        earthPosition = new Vector2D(0, -100);

        // loop through number of stars
        for (int i = 0; i < NUM_STARS; i++) {
            // set random position for each star within background bounds
            double xpos = canvas.xmin + Math.random() * canvas.width;
            double ypos = canvas.ymin + Math.random() * canvas.height;
            starPositions[i] = new Vector2D(xpos, ypos);

            // set random parallax sensitivity for each star
            starParallaxSensitivities[i] = Math.random() * STARS_PARALLAX_SENSITIVITY_MAX;
        }

    }

    // renders position of each star and earth based on velocity of player to give
    // parallax effect while moving
    public void renderStep(double dt, Vector2D playerVelocity) {

        // earth position
        earthPosition.x -= playerVelocity.x * dt * EARTH_PARALLAX_SENSITIVITY;
        earthPosition.y -= playerVelocity.y * dt * EARTH_PARALLAX_SENSITIVITY;

        for (int i = 0; i < NUM_STARS; i++) {

            // star position
            starPositions[i].x -= playerVelocity.x * dt * starParallaxSensitivities[i];
            starPositions[i].y -= playerVelocity.y * dt * starParallaxSensitivities[i];

            // recycle stars that go out of x bounds
            if (starPositions[i].x < canvas.xmin) {
                starPositions[i].x = canvas.xmax;
            } else if (starPositions[i].x > canvas.xmax) {
                starPositions[i].x = canvas.xmin;
            }

            // recycle stars that go out of y bounds
            if (starPositions[i].y < canvas.ymin) {
                starPositions[i].y = canvas.ymax;
            } else if (starPositions[i].y > canvas.ymax) {
                starPositions[i].y = canvas.ymin;
            }
        }
    }

    public void draw() {

        // draw black space background
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledRectangle(0, 0, canvas.width / 2, canvas.height / 2);

        // for each star
        for (int i = 0; i < NUM_STARS; i++) {

            // simulate flickering of stars by letting star brightness be random greyscale
            // value between [130,255]
            int grayLevel = 130 + (int) (Math.random() * 126);
            StdDraw.setPenColor(grayLevel, grayLevel, grayLevel);

            // draw star as a point
            StdDraw.point(starPositions[i].x, starPositions[i].y);
        }

        // draw earth from png image
        StdDraw.picture(earthPosition.x, earthPosition.y, "resources/images/earth.png");
    }
}