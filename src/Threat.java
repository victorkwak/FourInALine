import java.util.List;

/**
 * Created by brianzhao on 12/2/15.
 */
public interface Threat {

    List<Position> getCostSquares();

    Threat getDependentOn(); //TODO Dafuq?

    Position getGainSquare();

    List<Position> getRestSquares();
}
