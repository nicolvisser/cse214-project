public class Starfield {

    private static final int NUM_STARS = 1000;
    private static final double STARS_PARALLAX_SENSITIVITY_MAX = 0.083;

    private final Rectangle drawArea;
    private final Star[] stars;

    private static class Star extends Particle2D {
        private static final long serialVersionUID = 1L;
        public double parallaxSensitivity;

        public Star(Vector2D position, double parallaxSensitivity) {
            super(position);
            this.parallaxSensitivity = parallaxSensitivity;
        }
    }

    public Starfield(Rectangle drawArea) {
        this.drawArea = drawArea;
        stars = new Star[NUM_STARS];

        for (int i = 0; i < NUM_STARS; i++) {
            Vector2D position = drawArea.getRandomPositionInside();
            double parallaxSensitivity = Math.random() * STARS_PARALLAX_SENSITIVITY_MAX;
            stars[i] = new Star(position, parallaxSensitivity);
        }
    }

    public void render(double dt, Vector2D observerVelocity) {
        for (Star star : stars) {
            // give parallax effect based on observer velocity
            star.velocity = observerVelocity.scale(-star.parallaxSensitivity);
            star.render(dt);

            // recycle stars that go out of x or bounds
            if (star.position.x < drawArea.xmin()) {
                star.position.x = drawArea.xmax();
            } else if (star.position.x > drawArea.xmax()) {
                star.position.x = drawArea.xmin();
            }
            if (star.position.y < drawArea.ymin()) {
                star.position.y = drawArea.ymax();
            } else if (star.position.y > drawArea.ymax()) {
                star.position.y = drawArea.ymin();
            }
        }
    }

    public void draw() {
        // draw black space background
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledRectangle(0, 0, drawArea.getWidth() / 2, drawArea.getHeight() / 2);

        // draw each star with a flicker effect
        for (Star star : stars) {
            int grayLevel = 130 + (int) (Math.random() * 126);
            StdDraw.setPenColor(grayLevel, grayLevel, grayLevel);
            StdDraw.point(star.position.x, star.position.y);
        }
    }
}