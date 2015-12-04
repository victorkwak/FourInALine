import java.util.*;

/**
 * only allow even depth!!!
 */
public class Board {
    private Piece[][] board;
    private int dimension;
    private Position lastPlacedPosition;
    private int utility;

    public RowLine[][] rowLines;
    public ColumnLine[][] columnLines;


    public Board(int dimension) {
        this.dimension = dimension;
        board = new Piece[dimension][dimension];
        rowLines = new RowLine[dimension][dimension];
        columnLines = new ColumnLine[dimension][dimension];
    }

    public Threat Victoria() {
        Set<RowLine> allUniqueRowLines = new LinkedHashSet<>();
        Set<ColumnLine> allUniqueColumnLines = new LinkedHashSet<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (rowLines[i][j] != null && getPiece(new Position(i, j)).getSide() == Constants.XChar) {
                    allUniqueRowLines.add(rowLines[i][j]);
                }
                if (columnLines[i][j] != null && getPiece(new Position(i, j)).getSide() == Constants.XChar) {
                    allUniqueColumnLines.add(columnLines[i][j]);
                }
            }
        }

        List<Threat> allThreats = new ArrayList<>();

        Threat toReturn = null;
        for (RowLine rowLine : allUniqueRowLines) {
            if (rowLine.size() == 3) {
                return threeTogetherThreat(rowLine);
                //otherwise you are blocked on both sides

            } else if (rowLine.size() == 2) {
                toReturn = separatedThreeThreat(rowLine);
                if (toReturn != null) {
                    return toReturn;
                }

                //Two together


            }



        }
        return toReturn;
    }

    public Threat threeTogetherThreat(RowLine rowLine) {
        Position left = immediateLeftPostion(rowLine.getLeftEnd());
        Position right = immediateRightPostion(rowLine.getRightEnd());

        if (isValidPosition(left) && isEmpty(left)) {
            Position gainSquare = left;
            List<Position> restSquares = new ArrayList<>(rowLine.getIncludedPositions());
            return new Threat(gainSquare, restSquares, null);
        }
        if (isValidPosition(right) && isEmpty(right)) {
            Position gainSquare = right;
            List<Position> restSquares = new ArrayList<>(rowLine.getIncludedPositions());
            return new Threat(gainSquare, restSquares, null);
        }
        return null;
    }

    public Threat separatedThreeThreat(RowLine rowLine) {
        Position left = immediateLeftPostion(rowLine.getLeftEnd());
        Position right = immediateRightPostion(rowLine.getRightEnd());
        Position leftLeft = immediateLeftPostion(left);
        Position rightRight = immediateRightPostion(right);

        if (isValidPosition(left) && isEmpty(left) && isValidPosition(leftLeft) && isEmpty(leftLeft)) {
            Position gainSquare = left;
            List<Position> restSquares = new ArrayList<>(rowLine.getIncludedPositions());
            return new Threat(gainSquare, restSquares, null);
        }
        //Separated 3
        if (isValidPosition(right) && isEmpty(right) && isValidPosition(rightRight) && isEmpty(rightRight)) {
            Position gainSquare = right;
            List<Position> restSquares = new ArrayList<>(rowLine.getIncludedPositions());
            return new Threat(gainSquare, restSquares, null);
        }
        return null;
    }

    public Threat twoTogetherThreat(RowLine rowLine) {
        Position left = immediateLeftPostion(rowLine.getLeftEnd());
        Position right = immediateRightPostion(rowLine.getRightEnd());
        Position leftLeft = immediateLeftPostion(left);
        Position rightRight = immediateRightPostion(right);

        if (isValidPosition(left) && isEmpty(left) && isValidPosition(right) && isEmpty(right)) {
            Position gainSquare = left;
            List<Position> restSquares = new ArrayList<>(rowLine.getIncludedPositions());
            List<Position> costSquare = new ArrayList<>();
            costSquare.add(right);
            Threat toReturn = new Threat(gainSquare, restSquares, costSquare);
            if ( != null) {
                return toReturn;
            }
        }

        return null;
    }



    public Board(Board board) {
        this.dimension = board.dimension;
        this.board = new Piece[this.dimension][this.dimension];
        this.rowLines = new RowLine[dimension][dimension];
        this.columnLines = new ColumnLine[dimension][dimension];

        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                if (board.board[i][j] != null) {
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

//            rowLines[row][column] = new RowLine(new Position(row, column));
//            columnLines[row][column] = new ColumnLine(new Position(row, column));

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

    public boolean isEmpty(Position position) {
        return isEmpty(position.getRow(), position.getColumn());
    }

    private void merge(Piece piece) {
        //continue up
        Position piecePosition = piece.getPosition();
        int row = piecePosition.getRow();
        int column = piecePosition.getColumn();


        rowLines[row][column] = new RowLine(new Position(row, column));
        RowLine currentRowLine = rowLines[row][column];
        List<Position> allRowLinePositions = new ArrayList<>();


        Position left = immediateLeftPostion(piecePosition);
        while (isMergeable(piece, left)) {
            allRowLinePositions.add(left);
            left = immediateLeftPostion(left);
        }

        Position right = immediateRightPostion(piecePosition);
        while (isMergeable(piece, right)) {
            allRowLinePositions.add(right);
            right = immediateRightPostion(right);
        }


        for (Position position : allRowLinePositions) {
            currentRowLine.add(position);
        }
        for (Position position : allRowLinePositions) {
            rowLines[position.getRow()][position.getColumn()] = currentRowLine;
        }


        columnLines[row][column] = new ColumnLine(new Position(row, column));
        ColumnLine currentColumnLine = columnLines[row][column];
        List<Position> allColumnLinePositions = new ArrayList<>();


        Position up = immediateUpPostion(piecePosition);
        while (isMergeable(piece, up)) {
            allColumnLinePositions.add(up);
            up = immediateUpPostion(up);
        }

        Position down = immediateDownPostion(piecePosition);
        while (isMergeable(piece, down)) {
            allColumnLinePositions.add(down);
            down = immediateDownPostion(down);
        }


        for (Position position : allColumnLinePositions) {
            currentColumnLine.add(position);
        }
        for (Position position : allColumnLinePositions) {
            columnLines[position.getRow()][position.getColumn()] = currentColumnLine;
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
        for (int i = 1; i < Constants.BOARD_DIMENSION + 1; i++) {
            stringBuilder.append(i).append(" ");
        }
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
    public int getStaticEvaluation(boolean computerTurn, boolean startedFirst) {
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

        //iterate through X rows
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
                        xScore += startedFirst ? Constants.HIGH * Constants.UP_MULTIPLIER : Constants.HIGH;
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
                    if ((isValidPosition(left) && isEmpty(left)) &&
                            (isValidPosition(leftLeft) && isEmpty(leftLeft)) &&
                            (isValidPosition(right) && isEmpty(right))) {
                        xScore += startedFirst ? Constants.HIGH * Constants.UP_MULTIPLIER : Constants.HIGH;
                    }

                    //two on the right, one on the left
                    if ((isValidPosition(left) && isEmpty(left)) &&
                            (isValidPosition(right) && isEmpty(right)) &&
                            (isValidPosition(rightRight) && isEmpty(rightRight))) {
                        xScore += startedFirst ? Constants.HIGH * Constants.UP_MULTIPLIER : Constants.HIGH;
                    }

                    //jump in the middle
                    if ((isValidPosition(right) && isEmpty(right)) &&
                            isValidPosition(rightRight) && !isEmpty(rightRight) &&
                            getPiece(rightRight).getSide() == Constants.XChar) {
                        xScore += startedFirst ? Constants.HIGH * Constants.UP_MULTIPLIER : Constants.HIGH;
                    }

                    //jump in the middle
                    if ((isValidPosition(left) && isEmpty(left)) &&
                            isValidPosition(leftLeft) && !isEmpty(leftLeft) &&
                            getPiece(leftLeft).getSide() == Constants.XChar) {
                        xScore += startedFirst ? Constants.HIGH * Constants.UP_MULTIPLIER : Constants.HIGH;
                    }

                } else {
                    //rowline.size is 1
                    Position left = immediateLeftPostion(rowLine.getLeftEnd());
                    Position right = immediateRightPostion(rowLine.getRightEnd());
                    if (isValidPosition(left) && isEmpty(left) || isValidPosition(right) && isEmpty(right)) {
                        xScore += startedFirst ? Constants.LOW : Constants.LOW * Constants.DOWN_MULTIPLIER;
                    }

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
                        xScore += startedFirst ? Constants.HIGH * Constants.UP_MULTIPLIER : Constants.HIGH;
                    } else if ((isValidPosition(left) && isEmpty(left))
                            || (isValidPosition(right) && isEmpty(right))) {
                        //if we have an open 3
                        xScore += Constants.MID;
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
                    if ((isValidPosition(left) && isEmpty(left)) &&
                            (isValidPosition(leftLeft) && isEmpty(leftLeft)) &&
                            (isValidPosition(right) && isEmpty(right))) {
                        xScore += Constants.MID;
                    }

                    //two on the right, one on the left
                    if ((isValidPosition(left) && isEmpty(left)) &&
                            (isValidPosition(right) && isEmpty(right)) &&
                            (isValidPosition(rightRight) && isEmpty(rightRight))) {
                        xScore += Constants.MID;
                    }

                    //jump in the middle
                    if ((isValidPosition(right) && isEmpty(right)) &&
                            isValidPosition(rightRight) && !isEmpty(rightRight) &&
                            getPiece(rightRight).getSide() == Constants.XChar) {
                        xScore += Constants.MID;
                    }

                    //jump in the middle
                    if ((isValidPosition(left) && isEmpty(left)) &&
                            isValidPosition(leftLeft) && !isEmpty(leftLeft) &&
                            getPiece(leftLeft).getSide() == Constants.XChar) {
                        xScore += Constants.MID;
                    }

                } else {
                    //rowline.size is 1
                    Position left = immediateLeftPostion(rowLine.getLeftEnd());
                    Position right = immediateRightPostion(rowLine.getRightEnd());
                    if (isValidPosition(left) && isEmpty(left) || isValidPosition(right) && isEmpty(right)) {
                        xScore += !startedFirst ? Constants.LOW : Constants.LOW * Constants.DOWN_MULTIPLIER;
                    }
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
                        xScore += startedFirst ? Constants.HIGH * Constants.UP_MULTIPLIER : Constants.HIGH;
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
                        xScore += startedFirst ? Constants.HIGH * Constants.UP_MULTIPLIER : Constants.HIGH;
                    }

                    //two down, one up
                    if ((isValidPosition(up) && isEmpty(up)) &&
                            (isValidPosition(down) && isEmpty(down)) &&
                            (isValidPosition(downDown) && isEmpty(downDown))) {
                        xScore += startedFirst ? Constants.HIGH * Constants.UP_MULTIPLIER : Constants.HIGH;
                    }

                    //jump in the middle
                    if ((isValidPosition(down) && isEmpty(down)) &&
                            isValidPosition(downDown) && !isEmpty(downDown) &&
                            getPiece(downDown).getSide() == Constants.XChar) {
                        xScore += startedFirst ? Constants.HIGH * Constants.UP_MULTIPLIER : Constants.HIGH;
                    }

                    //jump in the middle
                    if ((isValidPosition(up) && isEmpty(up)) &&
                            isValidPosition(upUp) && !isEmpty(upUp) &&
                            getPiece(upUp).getSide() == Constants.XChar) {
                        xScore += startedFirst ? Constants.HIGH * Constants.UP_MULTIPLIER : Constants.HIGH;
                    }

                } else { //rowline.size is 1
                    //somewhat arbitrary heuristic
                    Position up = immediateLeftPostion(columnLine.getUpperEnd());
                    Position down = immediateRightPostion(columnLine.getLowerEnd());
                    if (isValidPosition(up) && isEmpty(up) || isValidPosition(down) && isEmpty(down)) {
                        xScore += startedFirst ? Constants.LOW : Constants.LOW * Constants.DOWN_MULTIPLIER;
                    }
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
                        xScore += startedFirst ? Constants.HIGH * Constants.UP_MULTIPLIER : Constants.HIGH;
                    } else if ((isValidPosition(up) && isEmpty(up))
                            || (isValidPosition(down) && isEmpty(down))) {
                        //if we have an open 3
                        xScore += Constants.MID;
                    } else {
                        //we have a blocked 3
                    }
                } else if (columnLine.size() == 2) {
                    Position up = immediateUpPostion(columnLine.getUpperEnd());
                    Position down = immediateDownPostion(columnLine.getLowerEnd());
                    Position upUp = immediateUpPostion(up);
                    Position downDown = immediateDownPostion(down);

                    //two up and one down
                    if ((isValidPosition(up) && isEmpty(up)) &&
                            (isValidPosition(upUp) && isEmpty(upUp)) &&
                            (isValidPosition(down) && isEmpty(down))) {
                        xScore += Constants.MID;
                    }

                    //two down, one up
                    if ((isValidPosition(up) && isEmpty(up)) &&
                            (isValidPosition(down) && isEmpty(down)) &&
                            (isValidPosition(downDown) && isEmpty(downDown))) {
                        xScore += Constants.MID;
                    }

                    //jump in the middle
                    if ((isValidPosition(down) && isEmpty(down)) &&
                            isValidPosition(downDown) && !isEmpty(downDown) &&
                            getPiece(downDown).getSide() == Constants.XChar) {
                        xScore += Constants.MID;
                    }

                    //jump in the middle
                    if ((isValidPosition(up) && isEmpty(up)) &&
                            isValidPosition(upUp) && !isEmpty(upUp) &&
                            getPiece(upUp).getSide() == Constants.XChar) {
                        xScore += Constants.MID;
                    }

                } else { //rowline.size is 1
                    Position up = immediateLeftPostion(columnLine.getUpperEnd());
                    Position down = immediateRightPostion(columnLine.getLowerEnd());
                    if (isValidPosition(up) && isEmpty(up) || isValidPosition(down) && isEmpty(down)) {
                        xScore += !startedFirst ? Constants.LOW : Constants.LOW * Constants.DOWN_MULTIPLIER;
                    }
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
                        oScore += Constants.HIGH * Constants.UP_MULTIPLIER;
                    } else if ((isValidPosition(left) && isEmpty(left))
                            || (isValidPosition(right) && isEmpty(right))) {
                        //if we have an open 3
                        oScore += Constants.HIGH * Constants.UP_MULTIPLIER;
                    } else {
                        //we have a blocked 3
                    }
                } else if (rowLine.size() == 2) {
                    Position left = immediateLeftPostion(rowLine.getLeftEnd());
                    Position right = immediateRightPostion(rowLine.getRightEnd());
                    Position leftLeft = immediateLeftPostion(left);
                    Position rightRight = immediateRightPostion(right);

                    //two on the left and one on the right
                    if ((isValidPosition(left) && isEmpty(left)) &&
                            (isValidPosition(leftLeft) && isEmpty(leftLeft)) &&
                            (isValidPosition(right) && isEmpty(right))) {
                        oScore += !startedFirst ? Constants.HIGH : Constants.MID;
                    }

                    //two on the right, one on the left
                    if ((isValidPosition(left) && isEmpty(left)) &&
                            (isValidPosition(right) && isEmpty(right)) &&
                            (isValidPosition(rightRight) && isEmpty(rightRight))) {
                        oScore += !startedFirst ? Constants.HIGH : Constants.MID;
                    }

                    //jump in the middle
                    if ((isValidPosition(right) && isEmpty(right)) &&
                            isValidPosition(rightRight) && !isEmpty(rightRight) &&
                            getPiece(rightRight).getSide() == Constants.OChar) {
                        oScore += !startedFirst ? Constants.HIGH : Constants.MID;
                    }

                    //jump in the middle
                    if ((isValidPosition(left) && isEmpty(left)) &&
                            isValidPosition(leftLeft) && !isEmpty(leftLeft) &&
                            getPiece(leftLeft).getSide() == Constants.OChar) {
                        oScore += !startedFirst ? Constants.HIGH : Constants.MID;
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
                        oScore += !startedFirst ? Constants.HIGH * Constants.UP_MULTIPLIER : Constants.HIGH;
                    } else if ((isValidPosition(left) && isEmpty(left))
                            || (isValidPosition(right) && isEmpty(right))) {
                        //if we have an open 3
                        oScore += !startedFirst ? Constants.HIGH * Constants.UP_MULTIPLIER : Constants.HIGH;
                    } else {
                        //we have a blocked 3
                    }
                } else if (rowLine.size() == 2) {
                    Position left = immediateLeftPostion(rowLine.getLeftEnd());
                    Position right = immediateRightPostion(rowLine.getRightEnd());
                    Position leftLeft = immediateLeftPostion(left);
                    Position rightRight = immediateRightPostion(right);

                    //two on the left and one on the right
                    if ((isValidPosition(left) && isEmpty(left)) &&
                            (isValidPosition(leftLeft) && isEmpty(leftLeft)) &&
                            (isValidPosition(right) && isEmpty(right))) {
                        oScore += Constants.HIGH;
                    }

                    //two on the right, one on the left
                    if ((isValidPosition(left) && isEmpty(left)) &&
                            (isValidPosition(right) && isEmpty(right)) &&
                            (isValidPosition(rightRight) && isEmpty(rightRight))) {
                        oScore += Constants.HIGH;
                    }

                    //jump in the middle
                    if ((isValidPosition(right) && isEmpty(right)) &&
                            isValidPosition(rightRight) && !isEmpty(rightRight) &&
                            getPiece(rightRight).getSide() == Constants.OChar) {
                        oScore += Constants.HIGH;
                    }

                    //jump in the middle
                    if ((isValidPosition(left) && isEmpty(left)) &&
                            isValidPosition(leftLeft) && !isEmpty(leftLeft) &&
                            getPiece(leftLeft).getSide() == Constants.OChar) {
                        oScore += Constants.HIGH;
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
                        oScore += Constants.HIGH;
                    } else if ((isValidPosition(up) && isEmpty(up))
                            || (isValidPosition(down) && isEmpty(down))) {
                        //if we have an open 3
                        oScore += Constants.MID;
                    } else {
                        //we have a blocked 3
                    }
                } else if (columnLine.size() == 2) {
                    Position up = immediateUpPostion(columnLine.getUpperEnd());
                    Position down = immediateDownPostion(columnLine.getLowerEnd());
                    Position upUp = immediateUpPostion(up);
                    Position downDown = immediateDownPostion(down);

                    //two up and one down
                    if ((isValidPosition(up) && isEmpty(up)) &&
                            (isValidPosition(upUp) && isEmpty(upUp)) &&
                            (isValidPosition(down) && isEmpty(down))) {
                        oScore += Constants.MID;
                    }

                    //two down, one up
                    if ((isValidPosition(up) && isEmpty(up)) &&
                            (isValidPosition(down) && isEmpty(down)) &&
                            (isValidPosition(downDown) && isEmpty(downDown))) {
                        oScore += Constants.MID;
                    }

                    //jump in the middle
                    if ((isValidPosition(down) && isEmpty(down)) &&
                            isValidPosition(downDown) && !isEmpty(downDown) &&
                            getPiece(downDown).getSide() == Constants.XChar) {
                        oScore += Constants.MID;
                    }

                    //jump in the middle
                    if ((isValidPosition(up) && isEmpty(up)) &&
                            isValidPosition(upUp) && !isEmpty(upUp) &&
                            getPiece(upUp).getSide() == Constants.XChar) {
                        oScore += Constants.MID;
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
                        oScore += Constants.HIGH;
                    } else if ((isValidPosition(up) && isEmpty(up))
                            || (isValidPosition(down) && isEmpty(down))) {
                        //if we have an open 3
                        oScore += Constants.HIGH;
                    } else {
                        //we have a blocked 3
                    }
                } else if (columnLine.size() == 2) {
                    Position up = immediateUpPostion(columnLine.getUpperEnd());
                    Position down = immediateDownPostion(columnLine.getLowerEnd());
                    Position upUp = immediateUpPostion(up);
                    Position downDown = immediateDownPostion(down);

                    //two up and one down
                    if ((isValidPosition(up) && isEmpty(up)) &&
                            (isValidPosition(upUp) && isEmpty(upUp)) &&
                            (isValidPosition(down) && isEmpty(down))) {
                        oScore += Constants.HIGH;
                    }

                    //two down, one up
                    if ((isValidPosition(up) && isEmpty(up)) &&
                            (isValidPosition(down) && isEmpty(down)) &&
                            (isValidPosition(downDown) && isEmpty(downDown))) {
                        oScore += Constants.HIGH;
                    }

                    //jump in the middle
                    if ((isValidPosition(down) && isEmpty(down)) &&
                            isValidPosition(downDown) && !isEmpty(downDown) &&
                            getPiece(downDown).getSide() == Constants.XChar) {
                        oScore += Constants.HIGH;
                    }

                    //jump in the middle
                    if ((isValidPosition(up) && isEmpty(up)) &&
                            isValidPosition(upUp) && !isEmpty(upUp) &&
                            getPiece(upUp).getSide() == Constants.XChar) {
                        oScore += Constants.HIGH;
                    }

                } else { //rowline.size is 1

                }

            }
        }
        return xScore - oScore;
    }
}
