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
        this.rowLine = new RowLine(this.position);
        this.columnLine = new ColumnLine(this.position);
    }

    public ColumnLine getColumnLine() {
        return columnLine;
    }


    public RowLine getRowLine() {
        return rowLine;
    }


    public Position getPosition() {
        return position;
    }

    public char getSide() {
        return side;
    }

    public boolean isSameSide(Piece piece){
        return this.getSide() == piece.getSide();
    }

    public void mergeRow(Piece piece){
        this.rowLine.merge(piece.rowLine);
        piece.rowLine = this.rowLine;
    }

    public void mergeColumn(Piece piece){
        this.columnLine.merge(piece.columnLine);
        piece.columnLine = this.columnLine;

    }


    @Override
    public String toString() {
        return String.valueOf(side);
    }
}
