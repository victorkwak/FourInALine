import java.util.List;

/**
 * Created by brianzhao on 12/2/15.
 */
public interface Threat {

    public List<Position> getCostSquares();

    public Thread getDependentOn();

    public Position getGainSquare();

    public List<Position> getRestSquares();
}
