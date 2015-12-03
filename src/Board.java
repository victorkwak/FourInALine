import java.util.*;
import java.util.stream.IntStream;

/**
 * only allow even depth!!!
 */
public class Board {
    private Piece[][] board;
    private int dimension;
    private Position lastPlacedPosition;
    private int utility;
    private List<Piece> pieces;


    public Board(int dimension) {
        this.dimension = dimension;
        board = new Piece[dimension][dimension];
        pieces = new ArrayList<>();
    }


    private Board(Board board) {
        this.dimension = board.dimension;
        this.lastPlacedPosition = board.lastPlacedPosition;
        this.pieces = new ArrayList<>(board.pieces);
//        Set<RowLine> rowLines = new HashSet<>();
//        Set<ColumnLine> columnLines = new HashSet<>();
//        for (Piece[] pieceArray : board.board) {
//            for (Piece piece : pieceArray) {
//                if (piece != null) {
//                    rowLines.add(piece.getRowLine());
//                    columnLines.add(piece.getColumnLine());
//                }
//            }
//        }




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
            pieces.add(piece);
            lastPlacedPosition = piecePosition;
            merge(piece);
            return true;
        } else {
            return false;
        }
    }


    /**
     * returns whether a given piece can be merged with the given position
     *
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
        return isValidPosition(position.getRow(), position.getColumn());
    }

    private Position immediateLeftPostion(Position position) {
        return new Position(position.getRow(), position.getColumn() - 1);
    }

    private Position immediateRightPostion(Position position) {
        return new Position(position.getRow(), position.getColumn() + 1);
    }

    private Position immediateUpPostion(Position position) {
        return new Position(position.getRow() - 1, position.getColumn());
    }

    private Position immediateDownPostion(Position position) {
        return new Position(position.getRow() + 1, position.getColumn());
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
        if (isMergeable(piece, left)) {
            piece.mergeRow(getPiece(left));
        }

        Position right = immediateRightPostion(piecePosition);
        if (isMergeable(piece, right)) {
            piece.mergeRow(getPiece(right));
        }

        Position up = immediateUpPostion(piecePosition);
        if (isMergeable(piece, up)) {
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
     * TODO remmeber to deep copy rowline and columnline
     *
     * @return
     */
    public List<Board> generateChildren() {
        return null;
    }

    /**
     * returns the utility of being in this board state
     *
     * @return
     */
    public int getStaticEvaluation(boolean computerTurn) {
        Set<RowLine> xRows = new LinkedHashSet<>();
        Set<RowLine> oRows = new LinkedHashSet<>();
        Set<ColumnLine> xColumns = new LinkedHashSet<>();
        Set<ColumnLine> oColumns = new LinkedHashSet<>();
        for (Piece[] pieceArray : board) {
            for (Piece piece : pieceArray) {
                if (piece == null) {
                    continue;
                }
                RowLine currentRowLine = piece.getRowLine();
                ColumnLine currentColumnLine = piece.getColumnLine();
                if (piece.getSide() == Constants.OChar) {
                    oRows.add(currentRowLine);
                    oColumns.add(currentColumnLine);
                } else {
                    xRows.add(currentRowLine);
                    xColumns.add(currentColumnLine);
                }
            }
        }

        int xScore = 0;
        int oScore = 0;


        //iterate through rows
        for (RowLine rowLine : xRows) {
            if (computerTurn) {
                if (rowLine.size() >= 4) {
                    //we already won
                    return Integer.MAX_VALUE;
                } else if (rowLine.size() == 3) {
                    Position left = immediateLeftPostion(rowLine.getLeftEnd());
                    Position right = immediateRightPostion(rowLine.getRightEnd());
                    if ((isValidPosition(left) && isEmpty(left))
                            || (isValidPosition(right) && isEmpty(right))) {
                        //if we have an open 3
                        return Integer.MAX_VALUE;
                    } else {
                        //we have a blocked 3
                    }
                } else if (rowLine.size() == 2) {
                    Position left = immediateLeftPostion(rowLine.getLeftEnd());
                    Position right = immediateRightPostion(rowLine.getRightEnd());
                    Position leftLeft = immediateLeftPostion(left);
                    Position rightRight = immediateRightPostion(right);

                    //two on the left and one on the right
                    if (
                            (isValidPosition(left) && isEmpty(left)) &&
                                    (isValidPosition(leftLeft) && isEmpty(leftLeft)) &&
                                    (isValidPosition(right) && isEmpty(right))
                            ) {
                        xScore+=1000;
                    }

                    //two on the right, one on the left
                    if (
                            (isValidPosition(left) && isEmpty(left)) &&
                                    (isValidPosition(right) && isEmpty(right)) &&
                                    (isValidPosition(rightRight) && isEmpty(rightRight))
                            ) {
                        xScore+=1000;
                    }

                    //jump in the middle
                    if ((isValidPosition(right) && isEmpty(right)) &&
                            isValidPosition(rightRight) && !isEmpty(rightRight) && getPiece(rightRight).getSide() == Constants.XChar) {
                        xScore+=1000;
                    }

                    //jump in the middle
                    if ((isValidPosition(left) && isEmpty(left)) &&
                            isValidPosition(leftLeft) && !isEmpty(leftLeft) && getPiece(leftLeft).getSide() == Constants.XChar) {
                        xScore+=1000;
                    }

                } else { //rowline.size is 1
                    //somewhat arbitrary heuristic
                    Position left = immediateLeftPostion(rowLine.getLeftEnd());
                    Position right = immediateRightPostion(rowLine.getRightEnd());
                    if (isValidPosition(left) && isEmpty(left) &&
                            isValidPosition(right) && isEmpty(right)) {
                        xScore += 2;
                    } else if ((isValidPosition(left) && isEmpty(left)) ||
                            (isValidPosition(right) && isEmpty(right))) {
                        xScore += 1;
                    } else {
                        //don't add anything
                    }
                }

            } else {
                throw new RuntimeException("don't know what to do");
                //TODO
                //don't know yet
            }
        }


        //iterate through columns
        for (ColumnLine columnLine : xColumns) {
            if (computerTurn) {
                if (columnLine.size() >= 4) {
                    //we already won
                    return Integer.MAX_VALUE;
                } else if (columnLine.size() == 3) {
                    Position up = immediateUpPostion(columnLine.getUpperEnd());
                    Position down = immediateDownPostion(columnLine.getLowerEnd());
                    if ((isValidPosition(up) && isEmpty(up))
                            || (isValidPosition(down) && isEmpty(down))) {
                        //if we have an open 3
                        return Integer.MAX_VALUE;
                    } else {
                        //we have a blocked 3
                    }
                } else if (columnLine.size() == 2) {
                    Position up = immediateUpPostion(columnLine.getUpperEnd());
                    Position down = immediateDownPostion(columnLine.getLowerEnd());
                    Position upUp = immediateUpPostion(up);
                    Position downDown = immediateDownPostion(down);

                    //two up and one down
                    if (
                            (isValidPosition(up) && isEmpty(up)) &&
                                    (isValidPosition(upUp) && isEmpty(upUp)) &&
                                    (isValidPosition(down) && isEmpty(down))
                            ) {
                        return Integer.MAX_VALUE;
                    }

                    //two down, one up
                    if (
                            (isValidPosition(up) && isEmpty(up)) &&
                                    (isValidPosition(down) && isEmpty(down)) &&
                                    (isValidPosition(downDown) && isEmpty(downDown))
                            ) {
                        return Integer.MAX_VALUE;
                    }

                    //jump in the middle
                    if ((isValidPosition(down) && isEmpty(down)) &&
                            isValidPosition(downDown) && !isEmpty(downDown) && getPiece(downDown).getSide() == Constants.XChar) {
                        return Integer.MAX_VALUE;
                    }

                    //jump in the middle
                    if ((isValidPosition(up) && isEmpty(up)) &&
                            isValidPosition(upUp) && !isEmpty(upUp) && getPiece(upUp).getSide() == Constants.XChar) {
                        return Integer.MAX_VALUE;
                    }

                } else { //rowline.size is 1
                    //somewhat arbitrary heuristic
                    Position up = immediateUpPostion(columnLine.getUpperEnd());
                    Position down = immediateDownPostion(columnLine.getLowerEnd());
                    if (isValidPosition(up) && isEmpty(up) &&
                            isValidPosition(down) && isEmpty(down)) {
                        xScore += 2;
                    } else if ((isValidPosition(up) && isEmpty(up)) ||
                            (isValidPosition(down) && isEmpty(down))) {
                        xScore += 1;
                    } else {
                        //don't add anything
                    }
                }
            } else {
                throw new RuntimeException("don't know what to do");
                //TODO
                //don't know yet
            }
        }



        //iterate through opponent rows
        for (RowLine rowLine : oRows) {
            if (computerTurn) {
                if (rowLine.size() >= 4) {
                    //we lost - this should never happen
                    return Integer.MIN_VALUE;
                } else if (rowLine.size() == 3) {
                    Position left = immediateLeftPostion(rowLine.getLeftEnd());
                    Position right = immediateRightPostion(rowLine.getRightEnd());
                    if ((isValidPosition(left) && isEmpty(left))
                            || (isValidPosition(right) && isEmpty(right))) {
                        //if we have an open 3
                        return Integer.MIN_VALUE;
                    } else {
                        //we have a blocked 3
                    }
                } else if (rowLine.size() == 2) {
                    Position left = immediateLeftPostion(rowLine.getLeftEnd());
                    Position right = immediateRightPostion(rowLine.getRightEnd());
                    Position leftLeft = immediateLeftPostion(left);
                    Position rightRight = immediateRightPostion(right);

                    //two on the left and one on the right
                    if (
                            (isValidPosition(left) && isEmpty(left)) &&
                                    (isValidPosition(leftLeft) && isEmpty(leftLeft)) &&
                                    (isValidPosition(right) && isEmpty(right))
                            ) {
                        oScore+=1000;
                    }

                    //two on the right, one on the left
                    if (
                            (isValidPosition(left) && isEmpty(left)) &&
                                    (isValidPosition(right) && isEmpty(right)) &&
                                    (isValidPosition(rightRight) && isEmpty(rightRight))
                            ) {
                        oScore+=1000;
                    }

                    //jump in the middle
                    if ((isValidPosition(right) && isEmpty(right)) &&
                            isValidPosition(rightRight) && !isEmpty(rightRight) && getPiece(rightRight).getSide() == Constants.OChar) {
                        oScore+=1000;
                    }

                    //jump in the middle
                    if ((isValidPosition(left) && isEmpty(left)) &&
                            isValidPosition(leftLeft) && !isEmpty(leftLeft) && getPiece(leftLeft).getSide() == Constants.OChar) {
                        oScore+=1000;
                    }

                } else { //rowline.size is 1
                    //somewhat arbitrary heuristic
                    Position left = immediateLeftPostion(rowLine.getLeftEnd());
                    Position right = immediateRightPostion(rowLine.getRightEnd());
                    if (isValidPosition(left) && isEmpty(left) &&
                            isValidPosition(right) && isEmpty(right)) {
                        oScore += 2;
                    } else if ((isValidPosition(left) && isEmpty(left)) ||
                            (isValidPosition(right) && isEmpty(right))) {
                        oScore += 1;
                    } else {
                        //don't add anything
                    }
                }

            } else {
                throw new RuntimeException("don't know what to do");
                //TODO
                //don't know yet
            }
        }



        //iterate through opponent columns
        for (ColumnLine columnLine : oColumns) {
            if (computerTurn) {
                if (columnLine.size() >= 4) {
                    //we already won
                    return Integer.MIN_VALUE;
                } else if (columnLine.size() == 3) {
                    Position up = immediateUpPostion(columnLine.getUpperEnd());
                    Position down = immediateDownPostion(columnLine.getLowerEnd());
                    if ((isValidPosition(up) && isEmpty(up))
                            || (isValidPosition(down) && isEmpty(down))) {
                        //if we have an open 3
                        return Integer.MIN_VALUE;
                    } else {
                        //we have a blocked 3
                    }
                } else if (columnLine.size() == 2) {
                    Position up = immediateUpPostion(columnLine.getUpperEnd());
                    Position down = immediateDownPostion(columnLine.getLowerEnd());
                    Position upUp = immediateUpPostion(up);
                    Position downDown = immediateDownPostion(down);

                    //two up and one down
                    if (
                            (isValidPosition(up) && isEmpty(up)) &&
                                    (isValidPosition(upUp) && isEmpty(upUp)) &&
                                    (isValidPosition(down) && isEmpty(down))
                            ) {
                        oScore+=1000;
                    }

                    //two down, one up
                    if (
                            (isValidPosition(up) && isEmpty(up)) &&
                                    (isValidPosition(down) && isEmpty(down)) &&
                                    (isValidPosition(downDown) && isEmpty(downDown))
                            ) {
                        oScore+=1000;
                    }

                    //jump in the middle
                    if ((isValidPosition(down) && isEmpty(down)) &&
                            isValidPosition(downDown) && !isEmpty(downDown) && getPiece(downDown).getSide() == Constants.XChar) {
                        oScore+=1000;
                    }

                    //jump in the middle
                    if ((isValidPosition(up) && isEmpty(up)) &&
                            isValidPosition(upUp) && !isEmpty(upUp) && getPiece(upUp).getSide() == Constants.XChar) {
                        oScore+=1000;
                    }

                } else { //rowline.size is 1
                    //somewhat arbitrary heuristic
                    Position up = immediateUpPostion(columnLine.getUpperEnd());
                    Position down = immediateDownPostion(columnLine.getLowerEnd());
                    if (isValidPosition(up) && isEmpty(up) &&
                            isValidPosition(down) && isEmpty(down)) {
                        oScore += 2;
                    } else if ((isValidPosition(up) && isEmpty(up)) ||
                            (isValidPosition(down) && isEmpty(down))) {
                        oScore += 1;
                    } else {
                        //don't add anything
                    }
                }
            } else {
                throw new RuntimeException("don't know what to do");
                //TODO
                //don't know yet
            }
        }
        return xScore-oScore;
    }


}
