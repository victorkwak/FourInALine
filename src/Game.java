/**
 * Created by Victor Kwak on 11/30/15.
 */
public class Game {
    public static void main(String[] args) {
        Board board = new Board();
        System.out.println(board);
        board.place(new X(0,0));
        System.out.println(board);
    }
}
