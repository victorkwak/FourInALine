import java.util.List;

/**
 * Created by brianzhao on 12/2/15.
 */
public class Threat {
    List<Position> costSquares;
    Position gainSquare;
    Threat dependentOn;
    List<Position> restSquares;

    public Threat(Position gainSquare, List<Position> costSquares, List<Position> restSquares) {
        this.costSquares = costSquares;
        this.gainSquare = gainSquare;
        this.restSquares = restSquares;
    }

    public List<Position> getCostSquares() {
        return costSquares;
    }

    public void setCostSquares(List<Position> costSquares) {
        this.costSquares = costSquares;
    }

    public Threat getDependentOn() {
        return dependentOn;
    }

    public void setDependentOn(Threat dependentOn) {
        this.dependentOn = dependentOn;
    }

    public Position getGainSquare() {
        return gainSquare;
    }

    public void setGainSquare(Position gainSquare) {
        this.gainSquare = gainSquare;
    }

    public List<Position> getRestSquares() {
        return restSquares;
    }

    public void setRestSquares(List<Position> restSquares) {
        this.restSquares = restSquares;
    }
}
