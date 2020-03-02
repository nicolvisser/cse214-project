class Vector2D {
    public double x;
    public double y;

    // constructor
    // creates vector object by specifying the two cartesian coordinates
    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // calculates and returns the magnitude of the vector specified by class
    // properties
    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    // calculates and returns the unit vector of the vector specified by class
    // properties (i.e. returns normalised vector)
    public Vector2D unitVector() {
        double mag = magnitude();
        return new Vector2D(x / mag, y / mag);
    }

    // returns a zero vector object
    public static Vector2D zeroVector() {
        return new Vector2D(0, 0);
    }

    // calculates and returns the sum of two specified vector objects
    public static Vector2D sum(Vector2D vec1, Vector2D vec2) {
        return new Vector2D(vec1.x + vec2.x, vec1.y + vec2.y);
    }

    // calculates and returns the scalar multiplication of a scalar and vector
    public static Vector2D scalarMultiplication(double c, Vector2D vec) {
        return new Vector2D(c * vec.x, c * vec.y);
    }
}