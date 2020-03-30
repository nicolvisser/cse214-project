import java.io.Serializable;

/**
 * CanvasDimension
 */
public class RectangleDimension implements Serializable {

    private static final long serialVersionUID = 1L;

    int width, height;
    double xcenter, ycenter, xmin, xmax, ymin, ymax, aspectRatio;

    public RectangleDimension(double xcenter, double ycenter, int width, int height) {
        set(xcenter, ycenter, width, height);
    }

    public void set(double xcenter, double ycenter, int width, int height) {
        this.xcenter = xcenter;
        this.ycenter = ycenter;
        this.width = width;
        this.height = height;
        this.xmin = xcenter - width / 2;
        this.xmax = xcenter + width / 2;
        this.ymin = xcenter - height / 2;
        this.ymax = xcenter + height / 2;
        this.aspectRatio = width / height;
    }

    public void setFrom(RectangleDimension other) {
        set(other.xcenter, other.ycenter, other.width, other.height);
    }

    public void setPosition(double xcenter, double ycenter) {
        set(xcenter, ycenter, this.width, this.height);
    }

    public void setSize(int width, int height) {
        set(this.xcenter, this.ycenter, width, height);
    }

    public double x(double percentage) {
        return xmin + percentage * width;
    }

    public double y(double percentage) {
        return ymin + percentage * height;
    }

    public boolean doesContainPoint(Vector2D pos) {
        return (pos.x >= xmin) && (pos.x <= xmax) && (pos.y >= ymin) && (pos.y <= ymax);
    }
}