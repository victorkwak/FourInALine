import java.util.LinkedList;
import java.util.List;

/**
 * Created by Victor Kwak on 12/1/15.
 */
public class ColumnLine {
    private List<Position> includedPositions = new LinkedList<>();
    private Position upperEnd;
    private Position lowerEnd;

    public ColumnLine(Position initialPosition){
        includedPositions.add(initialPosition);
        upperEnd = initialPosition;
        lowerEnd = initialPosition;
    }

    /**
     * should only add continguous positions!!!!!!
     * @param position
     */
    public void add(Position position){
        includedPositions.add(position);
        if (position.getRow() == upperEnd.getRow() - 1) {
            upperEnd= position;
        } else if (position.getRow() == lowerEnd.getRow() + 1) {
            lowerEnd = position;
        } else {
            throw new RuntimeException("Incorrect addition!!!");
        }
    }


    public void merge(ColumnLine columnLine){
        if (columnLine.lowerEnd.getColumn() != this.lowerEnd.getColumn()) {
            throw new RuntimeException("Wrong merge!!!!!!!");
        }
        if (columnLine == this) {
            throw new RuntimeException("Haven't taken into account merging with self");
        }

        this.includedPositions.addAll(columnLine.includedPositions);
        if (upperEnd.getRow() == columnLine.lowerEnd.getRow() +1) {
            upperEnd= columnLine.upperEnd;
        } else if (lowerEnd.getRow() == columnLine.upperEnd.getRow() - 1) {
            lowerEnd = columnLine.lowerEnd;
        } else {
            throw new RuntimeException("Incorrect merge");
        }
    }

    public Position getLowerEnd() {
        return lowerEnd;
    }

    public Position getUpperEnd() {
        return upperEnd;
    }

    public int size() {
        return this.includedPositions.size();
    }

    public List<Position> getIncludedPositions() {
        return includedPositions;
    }
}
