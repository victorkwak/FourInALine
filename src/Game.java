import java.util.List;
import java.util.Scanner;

/**
 * X is computer
 * O is player
 */
public class Game {
    private static boolean startedFirst;

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Board board = new Board(Constants.BOARD_DIMENSION);
//        int secondsAllowedForAIMove;
//
//
//        System.out.print("How many seconds is the AI allowed to have per move? ");
//        secondsAllowedForAIMove = Integer.parseInt(scanner.nextLine());

        System.out.print("Is computer going first? (y/n) ");
        String response = scanner.nextLine().toLowerCase();

        System.out.println("Initial board: ");
        System.out.println(board);


        if (response.equals("n")) {
            startedFirst = false;
            Position userPosition = getUserMove(board);
            board.placeO(userPosition);
            System.out.println(board);
        } else if (response.equals("y")) {
            startedFirst = true;
            board.placeX(3, 3);
            System.out.println("Computer move: \n" + board);
            board.placeO(getUserMove(board));
            System.out.println(board);
        } else {
            throw new RuntimeException("Bad user input");
        }



        //computer move
        while (!board.gameIsOver()) {
            board = minimax(board, Constants.DEPTH);
            System.out.println(board);
            if (board.gameIsOver()) {
                break;
            }
            board.placeO(getUserMove(board));
            System.out.println(board);

        }
        System.out.println("game over");
    }

    public static Board minimax(Board initialBoard, int depth) {
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        IntBoard utilityOfBestChild = max(initialBoard, alpha, beta, depth);
        return utilityOfBestChild.board;
    }

    /**
     * returns the maximum child of the input board's children
     * when this method is called, it is the ai's turn to move
     *
     * @param board
     */
    public static IntBoard max(Board board, int alpha, int beta, int depth) {
        if (depth == 0 || board.gameIsOver()) {
            board.setUtility(board.getStaticEvaluation(true, startedFirst));
            return new IntBoard(board, board.getUtility());
        }
        List<Board> children = board.generateChildren(true);
        int utility = Integer.MIN_VALUE;
        Board resultBoard = board;
        for (Board child : children) {
            int minResult = min(child, alpha, beta, depth - 1);
            if (minResult > utility) {
                utility = minResult;
                resultBoard = child;
            }
//            utility = Math.max(utility, minResult);
            if (utility >= beta) {
                return new IntBoard(child, utility);
            }
            alpha = Math.max(alpha, utility);
        }
        return new IntBoard(resultBoard, utility);
    }

    //TODO make sure false and true is correct

    /**
     * returns the minimum child of the input board's children
     *
     * @param board
     */
    public static int min(Board board, int alpha, int beta, int depth) {
        if (depth == 0 || board.gameIsOver()) {
            board.setUtility(board.getStaticEvaluation(false, startedFirst));
            return board.getUtility();
        }
        List<Board> children = board.generateChildren(false);
        int utility = Integer.MAX_VALUE;
        for (Board child : children) {
            int maxResult= max(child, alpha, beta, depth - 1).integerValue;
            utility = Math.min(utility, maxResult);
            if (utility <= alpha) {
                return utility;
            }
            beta = Math.min(beta, utility);
        }
        return utility;
    }

    public static Position getUserMove(Board board) {
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

            int row = rowNumber(inputLetter);
            int column = inputNum - 48 - 1; //-1 more because we are zero indexed

            System.out.println(row + " " + column);
            if (!isValidCoordinates(row, column)) {
                System.out.println("Invalid coordinates");
                continue;
            }
            if (!board.isEmpty(new Position(row, column))) {
                System.out.println("Position not empty");
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
