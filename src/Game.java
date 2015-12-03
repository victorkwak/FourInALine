import java.util.List;
import java.util.Scanner;

/**
 * X is computer
 * O is player
 */
public class Game {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Board board = new Board(Constants.BOARD_DIMENSION);
        int secondsAllowedForAIMove;


        System.out.print("How many seconds is the AI allowed to have per move? ");
        secondsAllowedForAIMove = Integer.parseInt(scanner.nextLine());

        System.out.print("Is computer going first? (y/n) ");
        String response = scanner.nextLine().toLowerCase();
        System.out.println();
        System.out.println(board);


        if (response.equals("n")) {
            Position userPosition = getUserMove();
            board.placeO(userPosition);
            System.out.println(board);
        } else if (!response.equals("y")) {
            throw new RuntimeException("Bad user input");
        }

        Board board1 = new Board(board);
        System.out.println("THIS IS BOARD 1: ");
        System.out.println(board1);

        //computer move
        while (!board.gameIsOver()) {
            board.placeO(getUserMove());
            System.out.println(board);

            System.out.println("THIS IS BOARD 1: ");
            System.out.println(board1);
        }
        System.out.println("game over");
    }

    public static Board minimax(Board initialBoard, int depth) {
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        return max(initialBoard, alpha, beta, depth);
    }

    //TODO make sure false and true is correct

    /**
     * returns the minimum child of the input board's children
     *
     * @param board
     */
    public static Board min(Board board, int alpha, int beta, int depth) {
        if (depth == 0 || board.gameIsOver()) {
            board.setUtility(board.getStaticEvaluation(false));
            return board;
        }
        board.setUtility(Integer.MAX_VALUE);
        List<Board> children = board.generateChildren();
        for (Board child : children) {
            Board max = max(child, alpha, beta, depth - 1);
            if (max.getUtility() < board.getUtility()) {
                board = max;
            }
            if (board.getUtility() <= alpha) {
                return board;
            }
            beta = Math.min(beta, board.getUtility());
        }
        return board;
    }

    /**
     * returns the maximum child of the input board's children
     * when this method is called, it is the ai's turn to move
     *
     * @param board
     */
    public static Board max(Board board, int alpha, int beta, int depth) {
        if (depth == 0 || board.gameIsOver()) {
            board.setUtility(board.getStaticEvaluation(true));
            return board;
        }
        board.setUtility(Integer.MIN_VALUE);
        List<Board> children = board.generateChildren();
        for (Board child : children) {
            Board min = min(child, alpha, beta, depth - 1);
            if (min.getUtility() > board.getUtility()) {
                board = min;
            }
            if (board.getUtility() >= beta) {
                return board;
            }
            alpha = Math.max(alpha, board.getUtility());
        }
        return board;
    }

    public static Position getUserMove() {
        Position toReturn = null;
        while (toReturn == null) {
            System.out.print("Enter your move: ");
            String input = scanner.nextLine();
            if (!input.matches("[a-zA-Z][1-" + Constants.BOARD_DIMENSION + "]")) {
                System.out.println("Wrong number of characters");
                continue;
            }
            char inputLetter = Character.toLowerCase(input.charAt(0));
            char inputNum = input.charAt(1);

            System.out.println(inputLetter);
            System.out.println(inputNum);
            int row = rowNumber(inputLetter);
            int column = inputNum - 48 - 1; //-1 more because we are zero indexed

            System.out.println(row + " " + column);
            if (!isValidCoordinates(row, column)) {
                System.out.println("Invalid coordinates");
                continue;
            }
            toReturn = new Position(row, column);
        }
        return toReturn;
    }

    private static boolean isValidCoordinates(int x, int y) {
        return x >= 0 && x < Constants.BOARD_DIMENSION && y >= 0 && y < Constants.BOARD_DIMENSION;
    }

    private static char rowNumber(char input) {
        return (char) (input - 97);
    }

}
