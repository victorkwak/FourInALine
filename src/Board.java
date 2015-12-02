import java.util.stream.IntStream;

/**
 * Created by Victor Kwak on 11/30/15.
 */
public class Board {
    private Piece[][] board;
    private int dimension;

    public Board(int dimension) {
        this.dimension = dimension;
        board = new Piece[dimension][dimension];
    }

    /**
     * places piece on board, returns whether action was successful
     * @param piece
     * @return
     */
    private boolean place(Piece piece) {
        int x = piece.getX();
        int y = piece.getY();
        if (isOnBoard(x, y) && isNotAlreadyOccuppied(x, y)) {
            board[piece.getX()][piece.getY()] = piece;
            //TODO perform kruskal set merge
            return true;
        } else {
            return false;
        }
    }

    public boolean placeX(int x, int y) {
        return place(new XPiece(x, y));
    }

    public boolean placeY(int x, int y) {
        return place(new OPiece(x, y));
    }

    private boolean isOnBoard(int x, int y) {
        return x >= 0 && x < dimension && y >= 0 && y < dimension;
    }

    private boolean isNotAlreadyOccuppied(int x, int y) {
        return board[x][y] != null;
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("   ");
        IntStream.range(1, Constants.BOARD_DIMENSION +1).forEach(i -> stringBuilder.append(i).append(" "));
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
