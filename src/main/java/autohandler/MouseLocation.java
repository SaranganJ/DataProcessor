package autohandler;

public class MouseLocation {
    private int x;
    private int y;

    @Override
    public String toString() {
        return "autohandler.MouseLocation{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public MouseLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
