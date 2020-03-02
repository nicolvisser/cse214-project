/**
 * Represents a vector in 2D space with two cartesian components.
 */
public class Vector2D {

    /**
     * Stores the x component of vector as in cartesian coordinates
     */
    public double x;

    /**
     * Stores the y component of vector as in cartesian coordinates
     */
    public double y;

    /**
     * Creates vector object by specifying the two cartesian coordinates
     * 
     * @param x x-component of vector
     * @param y y-component of vector
     */
    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Calculates magnitude of vector.
     * 
     * @return Magnitude of the vector.
     */
    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Calculates unit vector.
     * 
     * @return Unit vector of the vector.
     */
    public Vector2D unitVector() {
        double mag = magnitude();
        return new Vector2D(x / mag, y / mag);
    }

    /**
     * Returns zero vector.
     * 
     * @return New vector with (x,y) = (0,0)
     */
    public static Vector2D zeroVector() {
        return new Vector2D(0, 0);
    }

    /**
     * Calculates and returns the sum of two vectors.
     * 
     * @param vec1 First vector
     * @param vec2 Second vector
     * @return Sum of the two vectors passed.
     */
    public static Vector2D sum(Vector2D vec1, Vector2D vec2) {
        return new Vector2D(vec1.x + vec2.x, vec1.y + vec2.y);
    }

    // calculates and returns the scalar multiplication of a scalar and vector

    /**
     * Calculates and returns the scalar multiplication of a scalar and vector
     * 
     * @param c   Scalar value.
     * @param vec A Vector.
     * @return Scalar multiplication of passed scalar and vector.
     */
    public static Vector2D scalarMultiplication(double c, Vector2D vec) {
        return new Vector2D(c * vec.x, c * vec.y);
    }
}