import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class EnemyWave implements Serializable {

    private static final long serialVersionUID = 1L;

    private Rectangle canvas;
    private ArrayList<EnemyGroup> enemyGroups;
    private Shooter shooterRef;
    private double timeUntilNextCounterAttack;
    public ArrayList<Missile> enemyMissiles;

    public EnemyWave(Rectangle canvas, Shooter shooterRef) {
        this.canvas = canvas;

        enemyGroups = new ArrayList<>();

        Rectangle rect0 = new Rectangle(-50, 100, 100, 50);
        enemyGroups.add(new EnemyGroup(canvas, rect0, 5, 4));
        enemyGroups.get(0).velocity = new Vector2D(0, -20);

        Rectangle rect1 = new Rectangle(50, 200, 100, 50);
        enemyGroups.add(new EnemyGroup(canvas, rect1, 5, 4));
        enemyGroups.get(1).velocity = new Vector2D(0, -20);

        this.shooterRef = shooterRef;
        timeUntilNextCounterAttack = 1;
        enemyMissiles = new ArrayList<>();
    }

    public void render(double dt) {
        timeUntilNextCounterAttack -= dt;

        if (checkGameOverConditions(shooterRef)) {
            shooterRef.takeDamage(Integer.MAX_VALUE);
        }

        if (timeUntilNextCounterAttack < 0) {
            counterAttack();
        }

        Iterator<EnemyGroup> enemyGroupIterator = enemyGroups.iterator();
        while (enemyGroupIterator.hasNext()) {
            EnemyGroup enemyGroup = enemyGroupIterator.next();
            enemyGroup.render(dt);

            if (enemyGroup.isCleared()) {
                enemyGroupIterator.remove();
            }
        }

        Iterator<Missile> enemyMissileIterator = enemyMissiles.iterator();
        while (enemyMissileIterator.hasNext()) {
            Missile enemyMissile = enemyMissileIterator.next();
            enemyMissile.render(dt);
            if (enemyMissile.state == Missile.MissileState.DEAD || !canvas.contains(enemyMissile.position)) {
                enemyMissileIterator.remove();
            } else if (enemyMissile.state == Missile.MissileState.TRAVELLING
                    && enemyMissile.isCollidingWith(shooterRef)) {
                if (shooterRef.getShieldState() == false) {
                    shooterRef.takeDamage(enemyMissile.missileDamage);
                } else {
                    StdAudio.play("resources/audio/shieldUp.wav");
                }
                enemyMissile.takeDamage();
            } else if (enemyMissile.state == Missile.MissileState.TRAVELLING) {

                Iterator<Missile> shooterMissileIterator = shooterRef.getMissileLauncher().missiles.iterator();

                while (shooterMissileIterator.hasNext()) {
                    Missile shooterMissile = shooterMissileIterator.next();
                    if (shooterMissile.state == Missile.MissileState.TRAVELLING
                            && enemyMissile.isCollidingWith(shooterMissile)) {
                        shooterMissile.takeDamage();
                        enemyMissile.takeDamage();
                    }
                }

            }
        }
    }

    public void draw() {
        for (EnemyGroup enemyGroup : enemyGroups) {
            enemyGroup.draw();
        }
        for (Missile missile : enemyMissiles) {
            missile.draw();
        }
    }

    public int handlePossibleCollisionWithMissile(Missile missile) {
        int points = 0;
        for (EnemyGroup enemyGroup : enemyGroups) {
            points += enemyGroup.handlePossibleCollisionWithMissile(missile);
        }
        return points;
    }

    public boolean checkGameOverConditions(Shooter shooter) {
        for (EnemyGroup enemyGroup : enemyGroups) {
            if (enemyGroup.isCollidingWith(shooter) || enemyGroup.isCollidingWithBottomOfCanvas()) {
                return true;
            }
        }
        return false;
    }

    public boolean isCleared() {
        return enemyGroups.size() == 0;
    }

    public boolean atLeastOneAliveEnemyOnCanvas() {
        for (EnemyGroup enemyGroup : enemyGroups) {
            for (Enemy enemy : enemyGroup.enemies) {
                if (canvas.contains(enemy.position) && enemy.state == Enemy.EnemyState.ALIVE) {
                    return true;
                }
            }
        }
        return false;
    }

    public EnemyGroup getRandomEnemyGroup() {
        if (isCleared()) {
            return null;
        } else {
            int randomIndex = (int) (Math.random() * enemyGroups.size());
            return enemyGroups.get(randomIndex);
        }
    }

    // Todo: make this more readable
    public Enemy getRandomEnemyOnCanvas() {
        if (atLeastOneAliveEnemyOnCanvas()) {
            Enemy rEnemy;
            do {
                EnemyGroup rGroup = getRandomEnemyGroup();
                if (rGroup == null) {
                    rEnemy = null;
                } else {
                    rEnemy = rGroup.getRandomEnemy();
                }
            } while (rEnemy == null || !canvas.contains(rEnemy.position) || rEnemy.state != Enemy.EnemyState.ALIVE);
            return rEnemy;
        } else {
            return null;
        }
    }

    public void counterAttack() {
        timeUntilNextCounterAttack = 1;
        Enemy attackingEnemy = getRandomEnemyOnCanvas();
        if (attackingEnemy != null) {
            Vector2D missileSpawnLocation = new Vector2D(attackingEnemy.position.x, attackingEnemy.position.y);
            Vector2D missileDirection = shooterRef.positionRelativeTo(attackingEnemy).normalize();
            Missile missile = new Missile(missileSpawnLocation, missileDirection);
            StdDraw.filledCircle(missileSpawnLocation.x, missileSpawnLocation.y, 5);
            missile.velocity = missileDirection.scale(Missile.SPEED);
            enemyMissiles.add(missile);
            StdAudio.play("resources/audio/Gun+1.wav");
        }
    }

}