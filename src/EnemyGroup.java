import java.util.ArrayList;
import java.util.Iterator;

public class EnemyGroup extends DefaultCritter {

    private static final long serialVersionUID = 1L;

    Shape boundingShape;
    ArrayList<Enemy> enemies;
    Rectangle canvas;

    public EnemyGroup(Rectangle canvas, Rectangle boundingRectangle, int numEnemiesInRow, int numEnemiesInCol) {
        super(); // TODO fix issue here with collisionradius in super class that does not
                 // correspond to Shape collisionArea (also to other constructors)
        this.canvas = canvas; // TODO take out of constructor and give own method (also to other constructors)
        allowRotation = false;
        enemies = new ArrayList<>();
        this.position = boundingRectangle.center;
        this.boundingShape = boundingRectangle;

        int r = Enemy.DEFAULT_COLLISION_RADIUS;

        double xSpacing = (boundingRectangle.width - 2 * r * numEnemiesInRow) / (numEnemiesInRow - 1);
        double ySpacing = (boundingRectangle.height - 2 * r * numEnemiesInCol) / (numEnemiesInCol - 1);

        for (double x = boundingRectangle.xmin() + r; x < boundingRectangle.xmax(); x += xSpacing + 2 * r) {
            for (double y = boundingRectangle.ymin() + r; y < boundingRectangle.ymax(); y += ySpacing + 2 * r) {
                Enemy enemy = new Enemy(canvas, new Vector2D(x, y), 3 * Math.PI / 2);
                enemy.allowRotation = false;
                enemies.add(enemy);
            }
        }

    }

    public EnemyGroup(Rectangle canvas, Circle boundingCircle, int numEnemies) {
        super();
        this.canvas = canvas;
        allowRotation = false;
        enemies = new ArrayList<>();
        this.position = boundingCircle.center;
        this.boundingShape = boundingCircle;

        for (double theta = 0; theta < 2 * Math.PI; theta += 2 * Math.PI / numEnemies) {
            double x = position.x + (boundingCircle.radius - Enemy.DEFAULT_COLLISION_RADIUS) * Math.cos(theta);
            double y = position.y + (boundingCircle.radius - Enemy.DEFAULT_COLLISION_RADIUS) * Math.sin(theta);
            Enemy enemy = new Enemy(canvas, new Vector2D(x, y), 3 * Math.PI / 2);
            enemy.allowRotation = false;
            enemies.add(enemy);
        }
    }

    public boolean hasEnemies() {
        return enemies.size() > 0;
    }

    @Override
    public void draw() {
        // TODO: Possible performance update: only draw if on screen (by using bounding
        // boxes)
        for (Enemy enemy : enemies) {
            enemy.draw();
        }
    }

    @Override
    public void render(double dt) {
        super.render(dt);

        // calculate change in position of group object
        double dx = velocity.x * dt + 0.5 * acceleration.x * dt * dt;
        double dy = velocity.y * dt + 0.5 * acceleration.y * dt * dt;

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
        if (boundingShape.contains(missile.position)) { // TODO: missile should ideally have its own bounding box
            for (Enemy enemy : enemies) {
                points += enemy.handleCollisionWithMissile(missile);
            }
        }
        return points;
    }

    public boolean isTouchingBottomOrShooter(Shooter shooter) {
        // TODO: Possible big performance boost here if using bounding box that
        // (1) intersects with shooter bounding box
        // (2) interects with line that extends over 'ground' y = -100
        for (Enemy enemy : enemies) {
            if (enemy.isTouchingBottomOrShooter(shooter)) {
                return true;
            }
        }
        return false;
    }

    public Enemy getRandomEnemy() {
        if (!hasEnemies()) {
            return null;
        } else {
            int randomIndex = (int) (Math.random() * enemies.size());
            return enemies.get(randomIndex);
        }
    }

}