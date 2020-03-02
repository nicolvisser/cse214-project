/**
 * Represents object in 2D space with properties for kinematics (modelled as a
 * point particle but with orientation).
 */
public class Object2D {

    /**
     * Stores current position of object in vector format
     */
    public Vector2D position;

    /**
     * Stores current velocity of object in vector format
     */
    public Vector2D velocity;

    /**
     * Stores current acceleration of object in vector format
     */
    public Vector2D acceleration;

    /**
     * Stores the orientation of object. This is defined as the angle between the
     * positive x-axis and the object's line of sight.
     */
    public double orientation;

    /**
     * Creates an object in 2D space with specified position. Sets other properties
     * to zero.
     * 
     * @param position The object's position as a vector.
     */
    public Object2D(Vector2D position) {
        this.position = position;
        velocity = new Vector2D(0, 0);
        acceleration = new Vector2D(0, 0);

        orientation = 0;
    }

    /**
     * Creates an object in 2D space with specified position and orientation. Sets
     * other properties to zero.
     * 
     * @param position    The object's position as a vector.
     * @param orientation The object's orientation as an angle from positive x-axis.
     */
    public Object2D(Vector2D position, double orientation) {
        this.position = position;
        velocity = new Vector2D(0, 0);
        acceleration = new Vector2D(0, 0);

        this.orientation = orientation;
    }

    /**
     * Adds an x-displacement to the x-component of object's position.
     * 
     * @param deltaX Change in x-component.
     */
    public void translateX(double deltaX) {
        position.x += deltaX;
    }

    /**
     * Adds an y-displacement to the y-component of object's position.
     * 
     * @param deltaY Change in y-component.
     */
    public void translateY(double deltaY) {
        position.y += deltaY;
    }

    /**
     * Rotates object counterclockwise by changing angle associated with
     * orientation. Also ensures new angle will be in the range [0, 2*PI).
     * 
     * @param angleCCW Counterclockwise change in orientation.
     */
    public void rotate(double angleCCW) {
        // add change in rotation angle to current rotation angle
        orientation += angleCCW;

        // keep rotation within range [0, 2*PI)
        while (orientation >= 2 * Math.PI)
            orientation -= 2 * Math.PI;
        while (orientation < 2 * Math.PI)
            orientation += 2 * Math.PI;
    }

    /**
     * Orients the object towards a target in space defined by x and y coordinates.
     * 
     * @param targetX Target's x position.
     * @param targetY Target's y position.
     */
    public void lookAt(double targetX, double targetY) {
        double dx = targetX - this.position.x;
        double dy = targetY - this.position.y;
        orientation = Math.atan2(dy, dx);
    }

    /**
     * Orients the object towards another target object.
     * 
     * @param other Target object.
     */
    public void lookAt(Object2D other) {
        double dx = other.position.x - this.position.x;
        double dy = other.position.y - this.position.y;
        orientation = Math.atan2(dy, dx);
    }

    //

    /**
     * Returns the forward looking unit vector as defined by orientation of object.
     * 
     * @return The forward looking unit vector as defined by orientation of object.
     */
    public Vector2D FWDVector() {
        return new Vector2D(Math.cos(orientation), Math.sin(orientation));
    }

    /**
     * Returns the backward looking unit vector as defined by orientation of object.
     * 
     * @return The backward looking unit vector as defined by orientation of object.
     */
    public Vector2D BWDVector() {
        return new Vector2D(-Math.cos(orientation), -Math.sin(orientation));
    }

    /**
     * Returns the left looking unit vector as defined by orientation of object.
     * 
     * @return The left looking unit vector as defined by orientation of object.
     */
    public Vector2D leftVector() {
        return new Vector2D(-Math.sin(orientation), Math.cos(orientation));
    }

    /**
     * Returns the right looking unit vector as defined by orientation of object.
     * 
     * @return The right looking unit vector as defined by orientation of object.
     */
    public Vector2D rightVector() {
        return new Vector2D(Math.sin(orientation), -Math.cos(orientation));
    }

    // renders a single timestep, dt, by asuming constant acceleration over the
    // interval dt. Updates position and velocity values.

    /**
     * renders a single timestep, dt, by asuming constant acceleration over the
     * interval dt. Updates position and velocity values.
     * 
     * @param dt Size of timestep in seconds.
     */
    public void renderStep(double dt) {
        velocity.x += acceleration.x * dt;
        velocity.y += acceleration.y * dt;
        position.x += velocity.x * dt + 0.5 * acceleration.x * dt * dt;
        position.y += velocity.y * dt + 0.5 * acceleration.y * dt * dt;
    }

}