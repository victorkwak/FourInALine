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

    private RowLine[][] rowLines;
    private ColumnLine[][] columnLines;


    public Board(int dimension) {
        this.dimension = dimension;
        board = new Piece[dimension][dimension];
        rowLines = new RowLine[dimension][dimension];
        columnLines = new ColumnLine[dimension][dimension];
    }


    public Board(Board board) {
        this.dimension = board.dimension;
        this.board = new Piece[this.dimension][this.dimension];
        this.rowLines = new RowLine[dimension][dimension];
        this.columnLines = new ColumnLine[dimension][dimension];

        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                if (board.board[i][j] == null) {
                    this.board[i][j] = null;
                } else {
                    place(board.board[i][j]);
                }

            }
        }

        this.lastPlacedPosition = board.lastPlacedPosition;
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
        if (piece == null) {
            throw new RuntimeException("Adding a null piece!");
//            return false;
        }
        Position piecePosition = piece.getPosition();
        int row = piecePosition.getRow();
        int column = piecePosition.getColumn();
        if (isValidPosition(row, column) && isEmpty(row, column)) {
            board[row][column] = piece;

            rowLines[row][column] = new RowLine(new Position(row, column));
            columnLines[row][column] = new ColumnLine(new Position(row, column));


            lastPlacedPosition = piecePosition;
            merge(piece);
            return true;
        } else {
            throw new RuntimeException("didn't work wtf");
//            return false;
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
            mergeRow(piece, getPiece(left));
        }

        Position right = immediateRightPostion(piecePosition);
        if (isMergeable(piece, right)) {
            mergeRow(piece, getPiece(right));
        }

        Position up = immediateUpPostion(piecePosition);
        if (isMergeable(piece, up)) {
            mergeColumn(piece, getPiece(up));
        }

        Position down = immediateDownPostion(piecePosition);
        if (isMergeable(piece, down)) {
            mergeColumn(piece, getPiece(down));
        }
    }

    public void mergeRow(Piece firstPiece, Piece secondPiece) {
        Position firstPosition = firstPiece.getPosition();
        RowLine firstRowLine = rowLines[firstPosition.getRow()][firstPosition.getColumn()];

        Position secondPosition = secondPiece.getPosition();
        RowLine secondRowLine = rowLines[secondPosition.getRow()][secondPosition.getColumn()];

        firstRowLine.merge(secondRowLine);
        rowLines[secondPosition.getRow()][secondPosition.getColumn()] = firstRowLine;
    }

    public void mergeColumn(Piece firstPiece, Piece secondPiece) {
        Position firstPosition = firstPiece.getPosition();
        ColumnLine firstColumnLine = columnLines[firstPosition.getRow()][firstPosition.getColumn()];

        Position secondPosition = secondPiece.getPosition();
        ColumnLine secondColumnLine = columnLines[secondPosition.getRow()][secondPosition.getColumn()];

        firstColumnLine.merge(secondColumnLine);
        columnLines[secondPosition.getRow()][secondPosition.getColumn()] = firstColumnLine;
    }


    public int getUtility() {
        return utility;
    }

    public void setUtility(int utility) {
        this.utility = utility;
    }


    public boolean gameIsOver() {
        int row = lastPlacedPosition.getRow();
        int column = lastPlacedPosition.getColumn();
        return rowLines[row][column].size() >= 4
                || columnLines[row][column].size() >= 4;
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
     *
     * @return
     */
    public List<Board> generateChildren(boolean computerPlayer) {
        List<Board> children = new ArrayList<>();

        List<Position> possiblePositions = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (isEmpty(i, j)) {
                    possiblePositions.add(new Position(i, j));
                }
            }
        }

        Collections.sort(possiblePositions, new Comparator<Position>() {
            @Override
            public int compare(Position o1, Position o2) {
                return o1.distanceBetween(lastPlacedPosition) - o2.distanceBetween(lastPlacedPosition);
            }
        });

        if (possiblePositions.size() > Constants.NUM_CHILDREN) {
            possiblePositions = possiblePositions.subList(0, Constants.NUM_CHILDREN);
        }

        for (Position position : possiblePositions) {
            Board child = new Board(this);
            if (computerPlayer) {
                child.placeX(position);
            } else {
                child.placeO(position);
            }
            children.add(child);
        }

        return children;
    }

    /**
     * returns the utility of being in this board state
     *
     * @return
     */
    public int getStaticEvaluation(boolean computerTurn) {
        if (gameIsOver()) {
            Piece piece = getPiece(lastPlacedPosition);
            if (piece.getSide() == Constants.OChar) {
                return Integer.MIN_VALUE;
            } else {
                return Integer.MAX_VALUE;
            }
        }

        Set<RowLine> xRows = new LinkedHashSet<>();
        Set<RowLine> oRows = new LinkedHashSet<>();
        Set<ColumnLine> xColumns = new LinkedHashSet<>();
        Set<ColumnLine> oColumns = new LinkedHashSet<>();
        for (int i = 0; i < board.length; i++) {
            Piece[] pieceArray = board[i];
            for (int j = 0; j < pieceArray.length; j++) {
                Piece piece = pieceArray[j];
                if (piece == null) {
                    continue;
                }
                RowLine currentRowLine = rowLines[i][j];
                ColumnLine currentColumnLine = columnLines[i][j];
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
                        xScore+= 1000 * Constants.UP_MULTIPLIER;
                    } else {
                        //we have a blocked 3
                        //no points
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
                        xScore += 1000 * Constants.UP_MULTIPLIER;
                    }

                    //two on the right, one on the left
                    if (
                            (isValidPosition(left) && isEmpty(left)) &&
                                    (isValidPosition(right) && isEmpty(right)) &&
                                    (isValidPosition(rightRight) && isEmpty(rightRight))
                            ) {
                        xScore += 1000 * Constants.UP_MULTIPLIER;
                    }

                    //jump in the middle
                    if ((isValidPosition(right) && isEmpty(right)) &&
                            isValidPosition(rightRight) && !isEmpty(rightRight) && getPiece(rightRight).getSide() == Constants.XChar) {
                        xScore += 1000 * Constants.UP_MULTIPLIER;
                    }

                    //jump in the middle
                    if ((isValidPosition(left) && isEmpty(left)) &&
                            isValidPosition(leftLeft) && !isEmpty(leftLeft) && getPiece(leftLeft).getSide() == Constants.XChar) {
                        xScore += 1000 * Constants.UP_MULTIPLIER;
                    }

                } else { //rowline.size is 1


                }

            } else {
                //human player
                    if (rowLine.size() >= 4) {
                        //we already won
                        return Integer.MAX_VALUE;
                    } else if (rowLine.size() == 3) {
                        Position left = immediateLeftPostion(rowLine.getLeftEnd());
                        Position right = immediateRightPostion(rowLine.getRightEnd());
                        if ((isValidPosition(left) && isEmpty(left))
                                && (isValidPosition(right) && isEmpty(right))) {
                            //if we have an open 3
                            xScore+= 1000;
                        } else if ((isValidPosition(left) && isEmpty(left))
                                || (isValidPosition(right) && isEmpty(right))) {
                            //if we have an open 3
                            xScore +=250;
                        } else {
                            //we have a blocked 3
                            //no points
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
                            xScore += 500;
                        }

                        //two on the right, one on the left
                        if (
                                (isValidPosition(left) && isEmpty(left)) &&
                                        (isValidPosition(right) && isEmpty(right)) &&
                                        (isValidPosition(rightRight) && isEmpty(rightRight))
                                ) {
                            xScore += 500;
                        }

                        //jump in the middle
                        if ((isValidPosition(right) && isEmpty(right)) &&
                                isValidPosition(rightRight) && !isEmpty(rightRight) && getPiece(rightRight).getSide() == Constants.XChar) {
                            xScore += 500;
                        }

                        //jump in the middle
                        if ((isValidPosition(left) && isEmpty(left)) &&
                                isValidPosition(leftLeft) && !isEmpty(leftLeft) && getPiece(leftLeft).getSide() == Constants.XChar) {
                            xScore += 500;
                        }

                    } else { //rowline.size is 1

                    }


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
                        xScore += 1000 * Constants.UP_MULTIPLIER;
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
                        xScore+=1000 * Constants.UP_MULTIPLIER;
                    }

                    //two down, one up
                    if (
                            (isValidPosition(up) && isEmpty(up)) &&
                                    (isValidPosition(down) && isEmpty(down)) &&
                                    (isValidPosition(downDown) && isEmpty(downDown))
                            ) {
                        xScore+=1000 * Constants.UP_MULTIPLIER;
                    }

                    //jump in the middle
                    if ((isValidPosition(down) && isEmpty(down)) &&
                            isValidPosition(downDown) && !isEmpty(downDown) && getPiece(downDown).getSide() == Constants.XChar) {
                        xScore+=1000 * Constants.UP_MULTIPLIER;
                    }

                    //jump in the middle
                    if ((isValidPosition(up) && isEmpty(up)) &&
                            isValidPosition(upUp) && !isEmpty(upUp) && getPiece(upUp).getSide() == Constants.XChar) {
                        xScore+=1000 * Constants.UP_MULTIPLIER;
                    }

                } else { //rowline.size is 1
                    //somewhat arbitrary heuristic

                }

            } else {
                //human turn
                if (columnLine.size() >= 4) {
                    //we already won
                    return Integer.MAX_VALUE;
                } else if (columnLine.size() == 3) {
                    Position up = immediateUpPostion(columnLine.getUpperEnd());
                    Position down = immediateDownPostion(columnLine.getLowerEnd());
                    if ((isValidPosition(up) && isEmpty(up))
                            && (isValidPosition(down) && isEmpty(down))) {
                        //if we have an open 3
                        xScore += 1000;
                    }else if ((isValidPosition(up) && isEmpty(up))
                            || (isValidPosition(down) && isEmpty(down))) {
                        //if we have an open 3
                        xScore += 500;
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
                        xScore+=500;
                    }

                    //two down, one up
                    if (
                            (isValidPosition(up) && isEmpty(up)) &&
                                    (isValidPosition(down) && isEmpty(down)) &&
                                    (isValidPosition(downDown) && isEmpty(downDown))
                            ) {
                        xScore+=500;
                    }

                    //jump in the middle
                    if ((isValidPosition(down) && isEmpty(down)) &&
                            isValidPosition(downDown) && !isEmpty(downDown) && getPiece(downDown).getSide() == Constants.XChar) {
                        xScore+=500;
                    }

                    //jump in the middle
                    if ((isValidPosition(up) && isEmpty(up)) &&
                            isValidPosition(upUp) && !isEmpty(upUp) && getPiece(upUp).getSide() == Constants.XChar) {
                        xScore+=500;
                    }

                } else { //rowline.size is 1

                }


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
                            && (isValidPosition(right) && isEmpty(right))) {
                        //if we have an open 3
                        oScore+=1000;
                    } else if ((isValidPosition(left) && isEmpty(left))
                            || (isValidPosition(right) && isEmpty(right))) {
                        //if we have an open 3
                        oScore+=1000;
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
                        oScore += 500;
                    }

                    //two on the right, one on the left
                    if (
                            (isValidPosition(left) && isEmpty(left)) &&
                                    (isValidPosition(right) && isEmpty(right)) &&
                                    (isValidPosition(rightRight) && isEmpty(rightRight))
                            ) {
                        oScore += 500;
                    }

                    //jump in the middle
                    if ((isValidPosition(right) && isEmpty(right)) &&
                            isValidPosition(rightRight) && !isEmpty(rightRight) && getPiece(rightRight).getSide() == Constants.OChar) {
                        oScore += 500;
                    }

                    //jump in the middle
                    if ((isValidPosition(left) && isEmpty(left)) &&
                            isValidPosition(leftLeft) && !isEmpty(leftLeft) && getPiece(leftLeft).getSide() == Constants.OChar) {
                        oScore += 500;
                    }

                } else { //rowline.size is 1

                }

            } else {
                //if a human
                if (rowLine.size() >= 4) {
                    //we lost - this should never happen
                    return Integer.MIN_VALUE;
                } else if (rowLine.size() == 3) {
                    Position left = immediateLeftPostion(rowLine.getLeftEnd());
                    Position right = immediateRightPostion(rowLine.getRightEnd());

                    if ((isValidPosition(left) && isEmpty(left))
                            && (isValidPosition(right) && isEmpty(right))) {
                        //if we have an open 3
                        oScore+=1000 * Constants.UP_MULTIPLIER;
                    } else if ((isValidPosition(left) && isEmpty(left))
                            || (isValidPosition(right) && isEmpty(right))) {
                        //if we have an open 3
                        oScore+=1000 * Constants.UP_MULTIPLIER;
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
                        oScore += 1000;
                    }

                    //two on the right, one on the left
                    if (
                            (isValidPosition(left) && isEmpty(left)) &&
                                    (isValidPosition(right) && isEmpty(right)) &&
                                    (isValidPosition(rightRight) && isEmpty(rightRight))
                            ) {
                        oScore += 1000;
                    }

                    //jump in the middle
                    if ((isValidPosition(right) && isEmpty(right)) &&
                            isValidPosition(rightRight) && !isEmpty(rightRight) && getPiece(rightRight).getSide() == Constants.OChar) {
                        oScore += 1000;
                    }

                    //jump in the middle
                    if ((isValidPosition(left) && isEmpty(left)) &&
                            isValidPosition(leftLeft) && !isEmpty(leftLeft) && getPiece(leftLeft).getSide() == Constants.OChar) {
                        oScore += 1000;
                    }

                } else { //rowline.size is 1

                }

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
                            && (isValidPosition(down) && isEmpty(down))) {
                        //if we have an open 3
                        oScore += 1000;
                    }else if ((isValidPosition(up) && isEmpty(up))
                            || (isValidPosition(down) && isEmpty(down))) {
                        //if we have an open 3
                        oScore+=500;
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
                        oScore += 500;
                    }

                    //two down, one up
                    if (
                            (isValidPosition(up) && isEmpty(up)) &&
                                    (isValidPosition(down) && isEmpty(down)) &&
                                    (isValidPosition(downDown) && isEmpty(downDown))
                            ) {
                        oScore += 500;
                    }

                    //jump in the middle
                    if ((isValidPosition(down) && isEmpty(down)) &&
                            isValidPosition(downDown) && !isEmpty(downDown) && getPiece(downDown).getSide() == Constants.XChar) {
                        oScore += 500;
                    }

                    //jump in the middle
                    if ((isValidPosition(up) && isEmpty(up)) &&
                            isValidPosition(upUp) && !isEmpty(upUp) && getPiece(upUp).getSide() == Constants.XChar) {
                        oScore += 500;
                    }

                } else { //rowline.size is 1

                }
            } else {

                //human turn
                if (columnLine.size() >= 4) {
                    //we already won
                    return Integer.MIN_VALUE;
                } else if (columnLine.size() == 3) {
                    Position up = immediateUpPostion(columnLine.getUpperEnd());
                    Position down = immediateDownPostion(columnLine.getLowerEnd());
                    if ((isValidPosition(up) && isEmpty(up))
                            && (isValidPosition(down) && isEmpty(down))) {
                        //if we have an open 3
                        oScore += 1000;
                    }else if ((isValidPosition(up) && isEmpty(up))
                            || (isValidPosition(down) && isEmpty(down))) {
                        //if we have an open 3
                        oScore+=1000;
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
                        oScore += 1000;
                    }

                    //two down, one up
                    if (
                            (isValidPosition(up) && isEmpty(up)) &&
                                    (isValidPosition(down) && isEmpty(down)) &&
                                    (isValidPosition(downDown) && isEmpty(downDown))
                            ) {
                        oScore += 1000;
                    }

                    //jump in the middle
                    if ((isValidPosition(down) && isEmpty(down)) &&
                            isValidPosition(downDown) && !isEmpty(downDown) && getPiece(downDown).getSide() == Constants.XChar) {
                        oScore += 1000;
                    }

                    //jump in the middle
                    if ((isValidPosition(up) && isEmpty(up)) &&
                            isValidPosition(upUp) && !isEmpty(upUp) && getPiece(upUp).getSide() == Constants.XChar) {
                        oScore += 1000;
                    }

                } else { //rowline.size is 1

                }

            }
        }
        return xScore - oScore;
    }


}
