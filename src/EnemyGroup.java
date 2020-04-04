import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class EnemyGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    Vector2D position;
    Vector2D velocity;
    Vector2D acceleration;

    Rectangle boundingRect;
    ArrayList<Enemy> enemies;
    Rectangle canvas;

    public EnemyGroup(Rectangle canvas, Rectangle boundingRect, int numEnemiesInRow, int numEnemiesInCol) {
        super();
        this.canvas = canvas; // TODO take out of constructor and give own method (also to other constructors)
        enemies = new ArrayList<>();

        this.position = boundingRect.center;
        this.velocity = Vector2D.zero();
        this.acceleration = Vector2D.zero();

        this.boundingRect = boundingRect;

        int r = Enemy.DEFAULT_COLLISION_RADIUS;

        double xSpacing = (boundingRect.width - 2 * r * numEnemiesInRow) / (numEnemiesInRow - 1);
        double ySpacing = (boundingRect.height - 2 * r * numEnemiesInCol) / (numEnemiesInCol - 1);

        for (double x = boundingRect.xmin() + r; x < boundingRect.xmax(); x += xSpacing + 2 * r) {
            for (double y = boundingRect.ymin() + r; y < boundingRect.ymax(); y += ySpacing + 2 * r) {
                Enemy enemy = new Enemy(canvas, new Vector2D(x, y), 3 * Math.PI / 2);
                enemy.allowRotation = false;
                enemies.add(enemy);
            }
        }

    }

    public boolean isCleared() {
        return enemies.size() == 0;
    }

    public void draw() {

        // ---------> for debugging:
        if (Invaders.DEBGGING_ON) {
            StdDraw.setPenColor(StdDraw.PINK);
            boundingRect.draw();
        }
        // <------------------------

        if (canvas.intersects(boundingRect)) {
            for (Enemy enemy : enemies) {
                enemy.draw();
            }
        }

    }

    public void render(double dt) {

        velocity = velocity.add(acceleration.scale(dt));

        // calculate change in position of group object
        double dx = velocity.x * dt + 0.5 * acceleration.x * dt * dt;
        double dy = velocity.y * dt + 0.5 * acceleration.y * dt * dt;

        position.x += dx;
        position.y += dy;

        boundingRect.center = position; // TODO: Stop forcing these to be equal, and use some other mechanism

        Iterator<Enemy> itr = enemies.iterator();
        while (itr.hasNext()) {
            Enemy enemy = itr.next();

            // render new position of enemy, independent of group
            enemy.render(dt);

            // add group movement effect to enemy
            enemy.translateX(dx);
            enemy.translateY(dy);

            // remove enemy if no longer alive
            if (enemy.state == Enemy.EnemyState.DEAD) {
                itr.remove();
            }
        }
    }

    public int handlePossibleCollisionWithMissile(Missile missile) {
        int points = 0;
        if (boundingRect.intersects(missile.getBoundingShape())) {
            for (Enemy enemy : enemies) {
                points += enemy.handleCollisionWithMissile(missile);
            }
        }
        return points;
    }

    public boolean isCollidingWith(Shooter shooter) {

        if (boundingRect.intersects(shooter.getBoundingShape()))
            for (Enemy enemy : enemies) {
                if (enemy.isCollidingWith(shooter)) {
                    return true;
                }
            }
        return false;
    }

    public boolean isCollidingWithBottomOfCanvas() {
        // a ray along the bottom of canvas
        Ray groundRay = new Ray(new Vector2D(canvas.xmin(), canvas.ymin()), new Vector2D(1, 0));
        if (boundingRect.intersects(groundRay)) {
            for (Enemy enemy : enemies) {
                if (enemy.getBoundingShape().intersects(groundRay)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Enemy getRandomEnemy() {
        if (isCleared()) {
            return null;
        } else {
            int randomIndex = (int) (Math.random() * enemies.size());
            return enemies.get(randomIndex);
        }
    }

}