import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

public class Bunker {

    private class Block extends Particle2D {
        private static final long serialVersionUID = 1L;
        private double halfWidth, halfHeight;

        public Block(Vector2D position, double halfWidth, double halfHeight) {
            super(position);
            this.halfWidth = halfWidth;
            this.halfHeight = halfHeight;
        }

        public void draw() {
            int r = 60 + (int) (Math.random() * 20);
            StdDraw.setPenColor(new Color(r, r, r));
            StdDraw.filledRectangle(position.x, position.y, halfWidth, halfHeight);
        }
    }

    Rectangle boundingRect;
    ArrayList<Block> blocks;
    double collisionRadius;

    public Bunker(Rectangle boundingRect, int numRows, int numCols) {
        this.boundingRect = boundingRect;
        blocks = new ArrayList<>();
        double halfWidth = boundingRect.width / numRows / 2;
        double halfHeight = boundingRect.height / numCols / 2;
        this.collisionRadius = (halfWidth + halfHeight) / 2;

        for (double x = boundingRect.xmin() + halfWidth; x < boundingRect.xmax(); x += 2 * halfWidth) {
            for (double y = boundingRect.ymin() + halfHeight; y < boundingRect.ymax(); y += 2 * halfHeight) {
                Block block = new Block(new Vector2D(x, y), halfWidth, halfHeight);
                blocks.add(block);
            }
        }
    }

    public void draw() {
        for (Block block : blocks) {
            block.draw();
        }

        // -----> for debugging:
        StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
        boundingRect.draw();
        //
    }

    public void handlePossibleCollisionWith(Missile missile) {
        boolean burst = false;
        int burstRadius = 5;

        if (missile.state == Missile.MissileState.TRAVELLING) { // missile is alive and travelling

            if (boundingRect.contains(missile.position)) { // missile entered bunker region

                Iterator<Block> blockIterator = blocks.iterator();
                while (blockIterator.hasNext()) {
                    Block block = blockIterator.next();

                    double distance = block.position.subtract(missile.position).magnitude();

                    if (distance < collisionRadius) {
                        burst = true;
                    }

                    if (burst && distance < burstRadius) {
                        blockIterator.remove();
                        missile.takeDamage();
                    }

                }

            }
        }
    }
}