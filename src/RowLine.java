import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Victor Kwak on 12/1/15.
 */
public class RowLine {
    private List<Position> includedPositions = new LinkedList<>();
    private Position leftEnd;
    private Position rightEnd;

    public RowLine(Position initialPosition){
        includedPositions.add(initialPosition);
        leftEnd = initialPosition;
        rightEnd = initialPosition;
    }

    public void add(Position position){
        includedPositions.add(position);
        if (position.getColumn() == rightEnd.getColumn() +1) {
            rightEnd = position;
        } else if (position.getColumn() == leftEnd.getColumn() - 1) {
            leftEnd = position;
        } else {
            throw new RuntimeException("Wrong add");
        }
    }


    public void merge(RowLine rowLine){
        if (rowLine.rightEnd.getRow() != this.rightEnd.getRow()) {
            throw new RuntimeException("Wrong merge!!!!!!!");
        }
        if (rowLine == this) {
            throw new RuntimeException("Haven't taken into account merging with self");
        }


        this.includedPositions.addAll(rowLine.includedPositions);
        if (leftEnd.getColumn() == rowLine.rightEnd.getColumn()+1) {
            leftEnd= rowLine.leftEnd;
        } else if (rightEnd.getColumn() == rowLine.leftEnd.getColumn() - 1) {
            rightEnd = rowLine.rightEnd;
        } else {
            throw new RuntimeException("incorrect merge");
        }
    }

}
