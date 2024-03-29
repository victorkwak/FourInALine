import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * Created by Victor Kwak on 11/30/15.
 */
public class Board {
    private Piece[][] board;
    private int dimension;
    private Map<Integer, Column> columns;
    private Map<Integer, Row> rows;
    private List<Threat> threats;


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
        int x = piece.getPosition().getX();
        int y = piece.getPosition().getY();
        if (isOnBoard(x, y) && isNotAlreadyOccupied(x, y)) {
            board[x][y] = piece;
            merge(piece);
            return true;
        } else {
            return false;
        }
    }

    public boolean placeX(int x, int y) {
        return place(new XPiece(new Position(x, y)));
    }

    public boolean placeY(int x, int y) {
        return place(new OPiece(new Position(x, y)));
    }

    private boolean isOnBoard(int x, int y) {
        return x >= 0 && x < dimension && y >= 0 && y < dimension;
    }

    private boolean isNotAlreadyOccupied(int x, int y) {
        return board[x][y] != null;
    }

    private boolean isEmpty(int x, int y) {
        return board[x][y] == null;
    }

    private void merge(Piece piece) {
        int x = piece.getPosition().getX();
        int y = piece.getPosition().getY();
        //all the way left
        if (x == 0) {
            if (!isEmpty(x + 1, y)) {

            }
        }
        //all the way right
        if (x == Constants.BOARD_DIMENSION - 1) {
            if (!isEmpty(x + 1, y)) {

            }
        }
        //all the way top
        if (y == 0) {
            if (!isEmpty(x, y + 1)) {

            }
        }
        //all the way bottom
        if (y == Constants.BOARD_DIMENSION - 1) {
            if (!isEmpty(x, y - 1)) {

            }
        }
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
