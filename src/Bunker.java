import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

public class Bunker {

    private class Block extends Particle2D {
        private static final long serialVersionUID = 1L;
        private Rectangle boundingRect;
        public Color color;

        public Block(double x, double y, double width, double height) {
            super(new Vector2D(x, y));
            this.boundingRect = new Rectangle(x, y, width, height);
            int r = 80 + (int) (Math.random() * 20);
            color = new Color(r, r, r);
        }

        public void draw() {
            StdDraw.setPenColor(color);
            StdDraw.filledRectangle(position.x, position.y, boundingRect.width / 2, boundingRect.height / 2);
        }
    }

    Rectangle boundingRect;
    ArrayList<Block> blocks;

    public Bunker(Rectangle boundingRect, int numRows, int numCols) {
        this.boundingRect = boundingRect;
        blocks = new ArrayList<>();
        double spacing = 0.1;
        double blockWidth = (boundingRect.width - (numCols - 1) * spacing) / numCols;
        double blockHeight = (boundingRect.height - (numRows - 1) * spacing) / numRows;

        for (double x = boundingRect.xmin() + blockWidth / 2; x < boundingRect.xmax(); x += blockWidth + spacing) {
            for (double y = boundingRect.ymin() + blockHeight / 2; y < boundingRect.ymax(); y += blockHeight
                    + spacing) {
                Block block = new Block(x, y, blockWidth, blockHeight);
                blocks.add(block);
            }
        }
    }

    public boolean isCleared() {
        return blocks.size() == 0;
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

            if (boundingRect.contains(missile.position)) { // missile center inside bunker

                Circle missileBurstCircle = new Circle(missile.position.x, missile.position.y, burstRadius);

                Iterator<Block> blockIterator = blocks.iterator();
                while (blockIterator.hasNext()) {
                    Block block = blockIterator.next();

                    // if missile is not yet bursting, but missile is colliding with block, set
                    // burst true
                    if (!burst && missile.collisionCircle.intersects(block.boundingRect)) {
                        burst = true;
                    }

                    // if missile is bursting and block intersects burst radius, remove block
                    if (burst && missileBurstCircle.intersects(block.boundingRect)) {
                        blockIterator.remove();
                        missile.takeDamage();
                    }

                }

            }
        }
    }
}