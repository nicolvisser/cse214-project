import java.io.Serializable;

/**
 * CanvasDimension
 */
public class Rectangle implements Serializable {

    private static final long serialVersionUID = 1L;

    private double width, height;
    private double x, y; // position of center of rectangle
    private double xmin, xmax, ymin, ymax;

    public Rectangle(double xcenter, double ycenter, double width, double height) {
        set(xcenter, ycenter, width, height);
    }

    public void set(double xcenter, double ycenter, double width, double height) {
        this.x = xcenter;
        this.y = ycenter;
        this.width = width;
        this.height = height;
        this.xmin = xcenter - width / 2;
        this.xmax = xcenter + width / 2;
        this.ymin = xcenter - height / 2;
        this.ymax = xcenter + height / 2;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return width;
    }

    public double getXmin() {
        return xmin;
    }

    public double getXmax() {
        return xmax;
    }

    public double getYmin() {
        return ymin;
    }

    public double getYmax() {
        return ymax;
    }

    public void setWidth(double width) {
        set(x, y, width, height);
    }

    public void setHeight(double height) {
        set(x, y, width, height);
    }

    public void setXcenter(double xcenter) {
        set(xcenter, y, width, height);
    }

    public void setYcenter(double ycenter) {
        set(x, ycenter, width, height);
    }

    public void setPosition(double xcenter, double ycenter) {
        set(xcenter, ycenter, width, height);
    }

    public void setSize(int width, int height) {
        set(x, y, width, height);
    }

    public boolean containsPoint(Vector2D pos) {
        return (pos.x >= xmin) && (pos.x <= xmax) && (pos.y >= ymin) && (pos.y <= ymax);
    }

    public Vector2D getRandomPosition() {
        double rx = xmin + Math.random() * width;
        double ry = ymin + Math.random() * height;
        return new Vector2D(rx, ry);
    }
}