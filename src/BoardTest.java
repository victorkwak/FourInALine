import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by brianzhao on 12/3/15.
 */
public class BoardTest {
    @Test
    public void columnInsertionTest() {
        Board board = new Board(Constants.BOARD_DIMENSION);
        board.placeO(new Position(7, 2));
        assertTrue(board.columnLines[7][2].size() ==1);


        board.placeO(new Position(6, 2));
        assertTrue(board.columnLines[6][2].size() ==2);
        assertTrue(board.columnLines[7][2].size() ==2);
        assertTrue(board.columnLines[7][2]==board.columnLines[6][2]);

        board.placeO(new Position(5, 2));

        assertTrue(board.columnLines[6][2].size() ==3);
        assertTrue(board.columnLines[5][2]==board.columnLines[6][2]);

        assertTrue(board.columnLines[7][2]==board.columnLines[6][2]);
        assertTrue(board.columnLines[7][2].size() ==3);
        assertTrue(board.columnLines[7][2]==board.columnLines[6][2]);

        assertTrue(board.columnLines[5][2].size() ==3);
    }

    @Test
    public void mergeRight(){
        Board board = new Board(Constants.BOARD_DIMENSION);
        board.placeO(new Position(7, 0));
        assertTrue(board.rowLines[7][0].size() ==1);


        board.placeO(new Position(7,1));
        assertTrue(board.rowLines[7][0].size() ==2);
        assertTrue(board.rowLines[7][1].size() ==2);
        assertTrue(board.rowLines[7][1]==board.rowLines[7][0]);


        board.placeO(new Position(7,2));
        assertTrue(board.rowLines[7][1].size() ==3);
        assertTrue(board.rowLines[7][2].size() == 3);
        assertTrue(board.rowLines[7][0].size() ==3);
        assertTrue(board.rowLines[7][1]==board.rowLines[7][0]);
        assertTrue(board.rowLines[7][1]==board.rowLines[7][2]);

    }
}