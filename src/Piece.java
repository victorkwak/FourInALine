/**
 * Created by Victor Kwak on 11/30/15.
 */
public abstract class Piece {
    private int x;
    private int y;
    private char side;

    public Piece(int x, int y, char side) {
        this.x = x;
        this.y = y;
        this.side = side;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return String.valueOf(side);
    }
}
