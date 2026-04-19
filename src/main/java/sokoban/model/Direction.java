package sokoban.model;

public enum Direction {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);

    public final int dx; // 行
    public final int dy; // 列

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
}