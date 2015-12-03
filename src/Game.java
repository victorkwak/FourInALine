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

        //computer move
        while (!board.gameIsOver()) {
            board.placeO(getUserMove());
            System.out.println(board);
        }
        System.out.println("game over");
    }

    public static void Minimax(Board initialBoard) {

    }

    public static void min(Board initialBoard) {

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
            int column = inputNum - 48 -1; //-1 more because we are zero indexed

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
