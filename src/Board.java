import java.util.stream.IntStream;

/**
 * Created by Victor Kwak on 11/30/15.
 */
public class Board {
    private Piece[][] board;
    private int dimension;
    private Position lastPlacePosition;

    public Board(int dimension) {
        this.dimension = dimension;
        board = new Piece[dimension][dimension];
    }

    /**
     * places piece on board, returns whether action was successful
     *
     * @param piece
     * @return
     */
    private boolean place(Piece piece) {
        int x = piece.getPosition().getRow();
        int y = piece.getPosition().getColumn();
        if (isOnBoard(x, y) && isEmpty(x, y)) {
            board[x][y] = piece;
            lastPlacePosition = piece.getPosition();

            return true;
        } else {
            return false;
        }
    }





    public boolean placeX(int x, int y) {
        return place(new XPiece(new Position(x, y)));
    }

    public boolean placeO(int x, int y) {
        return place(new OPiece(new Position(x, y)));
    }

    public boolean placeX(Position position) {
        return place(new XPiece(position));
    }
    public boolean placeO(Position position) {
        return place(new OPiece(position));
    }

    private boolean isOnBoard(int x, int y) {
        return x >= 0 && x < dimension && y >= 0 && y < dimension;
    }


    private boolean isEmpty(int x, int y) {
        return board[x][y] == null;
    }

    private void merge(Piece piece) {
        int x = piece.getPosition().getRow();
        int y = piece.getPosition().getColumn();
        //not on the edges
        //look left and right
        if (!isEmpty(x - 1, y)) {

        }
        if (!isEmpty(x - 2, y)) {

        }
        if (!isEmpty(x + 1, y)) {

        }
        if (!isEmpty(x + 2, y)) {

        }
        //look up and down
        if (!isEmpty(x, y + 1)) {

        }
        if (!isEmpty(x, y + 2)) {

        }
        if (!isEmpty(x, y - 1)) {

        }
        if (!isEmpty(x, y - 2)) {

        }
    }

    public boolean gameIsOver() {

    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("   ");
        IntStream.range(1, Constants.BOARD_DIMENSION + 1).forEach(i -> stringBuilder.append(i).append(" "));
        stringBuilder.append("\n");
        char current = 'A';
        for (Piece[] pieces : board) {
            stringBuilder.append(current++).append(" ");
            for (Piece piece : pieces) {
                stringBuilder.append(" ");
                if (piece == null) {
                    stringBuilder.append("-");
                } else {
                    stringBuilder.append(piece);
                }
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }


}
