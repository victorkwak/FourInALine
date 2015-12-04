/**
 * Artificial Intelligence Alph-Beta Pruning
 * December 3, 2015
 *
 * Brian Zhao
 * Victor kwak
 */
public abstract class Piece {
    private Position position;
    private char side;


    public Piece(Position position, char side) {
        this.position = position;
        this.side = side;
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

    @Override
    public String toString() {
        return String.valueOf(side);
    }

}
