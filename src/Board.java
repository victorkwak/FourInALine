import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by Victor Kwak on 11/30/15.
 */
public class Board {
    private Piece[][] board;
    private int dimension;
    private Position lastPlacedPosition;
    private int utility;

    private int alpha;
    private int beta;


    public Board(int dimension) {
        this.dimension = dimension;
        board = new Piece[dimension][dimension];
    }

    /**
     * places piece on board,
     * merges with neighbors
     * returns whether action was successful
     *
     * @param piece
     * @return
     */
    private boolean place(Piece piece) {
        Position piecePosition = piece.getPosition();
        int row = piecePosition.getRow();
        int column = piecePosition.getColumn();
        if (isValidPosition(row, column) && isEmpty(row, column)) {
            board[row][column] = piece;
            lastPlacedPosition = piecePosition;
            merge(piece);
            return true;
        } else {
            return false;
        }
    }


    /**
     * returns whether a given piece can be merged with the given position
     * @param piece
     * @param position
     * @return
     */
    private boolean isMergeable(Piece piece, Position position) {
        return isValidPosition(position) && !isEmpty(position) && getPiece(position).isSameSide(piece);
    }

    private Piece getPiece(Position position) {
        return board[position.getRow()][position.getColumn()];
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

    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < dimension && y >= 0 && y < dimension;
    }

    private boolean isValidPosition(Position position) {
        return isValidPosition(position.getRow(),position.getColumn());
    }

    private Position immediateLeftPostion(Position position) {
        return new Position(position.getRow(), position.getColumn() - 1);
    }

    private Position immediateRightPostion(Position position) {
        return new Position(position.getRow(), position.getColumn() + 1);
    }

    private Position immediateUpPostion(Position position) {
        return new Position(position.getRow()-1, position.getColumn());
    }

    private Position immediateDownPostion(Position position) {
        return new Position(position.getRow()+1, position.getColumn());
    }

    private boolean isEmpty(int x, int y) {
        return board[x][y] == null;
    }

    private boolean isEmpty(Position position) {
        return isEmpty(position.getRow(), position.getColumn());
    }

    private void merge(Piece piece) {
        Position piecePosition = piece.getPosition();
        Position left = immediateLeftPostion(piecePosition);
        if (isMergeable(piece,left)) {
            piece.mergeRow(getPiece(left));
        }

        Position right = immediateRightPostion(piecePosition);
        if (isMergeable(piece, right)) {
            piece.mergeRow(getPiece(right));
        }

        Position up = immediateUpPostion(piecePosition);
        if (isMergeable(piece,up)) {
            piece.mergeColumn(getPiece(up));
        }

        Position down = immediateDownPostion(piecePosition);
        if (isMergeable(piece, down)) {
            piece.mergeColumn(getPiece(down));
        }
    }

    public int getUtility() {
        return utility;
    }

    public void setUtility(int utility) {
        this.utility = utility;
    }

//    public int getAlpha() {
//        return alpha;
//    }
//
//    public void setAlpha(int alpha) {
//        this.alpha = alpha;
//    }
//
//    public int getBeta() {
//        return beta;
//    }
//
//    public void setBeta(int beta) {
//        this.beta = beta;
//    }

    public boolean gameIsOver() {
        Piece piece = getPiece(lastPlacedPosition);
        return piece.getRowLine().size() == 4 || piece.getColumnLine().size() == 4;
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


    /**
     * picks the best children to search using minimax
     * @return
     */
    public List<Board> generateChildren() {
        return null;
    }

    /**
     * returns the utility of being in this board state
     * @return
     */
    public int getStaticEvaluation(){

        return 0;
    }



}
