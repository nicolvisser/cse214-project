/**
 * Background
 */
public class StarField {

    // declare variables to store background position and size as bounds
    private double xmin, xmax, ymin, ymax;

    // declare variables associated with stars
    private final int NUM_STARS = 1000;
    private Vector2D[] starPositions = new Vector2D[NUM_STARS];
    private double[] starParallaxSensitivities = new double[NUM_STARS];

    // declare variables associated with earth
    private Vector2D earthPosition = Vector2D.zeroVector();
    private final double EARTH_PARALLAX_SENSITIVITY = 0.1;

    public StarField(double x_min, double x_max, double y_min, double y_max) {
        xmin = x_min;
        xmax = x_max;
        ymin = y_min;
        ymax = y_max;

        // loop through number of stars
        for (int i = 0; i < NUM_STARS; i++) {
            // set random position for each star within background bounds
            double xpos = xmin + Math.random() * (xmax - xmin);
            double ypos = ymin + Math.random() * (ymax - ymin);
            starPositions[i] = new Vector2D(xpos, ypos);

            // set random parallax sensitivity for each star
            starParallaxSensitivities[i] = Math.random() / 12; // TODO get rid of hardcoding
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
            if (starPositions[i].x < xmin) {
                starPositions[i].x = xmax;
            } else if (starPositions[i].x > xmax) {
                starPositions[i].x = xmin;
            }

            // recycle stars that go out of y bounds
            if (starPositions[i].y < ymin) {
                starPositions[i].y = ymax;
            } else if (starPositions[i].y > ymax) {
                starPositions[i].y = ymin;
            }
        }
    }

    public void draw() {

        // draw black space background
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledRectangle((xmax + xmin) / 2, (ymax + ymin) / 2, (xmax - xmin) / 2, (ymax - ymin) / 2);

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
        StdDraw.picture(earthPosition.x, earthPosition.y, "resources/earth.png");
    }
}