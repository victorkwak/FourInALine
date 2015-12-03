/**
 * Created by Victor Kwak on 11/30/15.
 */
public abstract class Piece {
    private Position position;
    private char side;
    private RowLine rowLine;
    private ColumnLine columnLine;


    public Piece(Position position, char side) {
        this.position = position;
        this.side = side;
    }

    public ColumnLine getColumnLine() {
        return columnLine;
    }

    public void setColumnLine(ColumnLine columnLine) {
        this.columnLine = columnLine;
    }

    public RowLine getRowLine() {
        return rowLine;
    }

    public void setRowLine(RowLine rowLine) {
        this.rowLine = rowLine;
    }

    public Position getPosition() {
        return position;
    }

    public char getSide() {
        return side;
    }


    @Override
    public String toString() {
        return String.valueOf(side);
    }
}
