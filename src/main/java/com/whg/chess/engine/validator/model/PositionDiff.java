package com.whg.chess.engine.validator.model;

import com.whg.chess.model.Coordinates;
import com.whg.chess.model.Move;
import lombok.Data;

@Data
public class PositionDiff {
    private final int rowDiff;
    private final int colDiff;

    public PositionDiff(Move move) {
        this(move.getFrom(), move.getTo());
    }

    public PositionDiff(Coordinates from, Coordinates to) {
        this.rowDiff = from.getRow() - to.getRow();
        this.colDiff = from.getColumn() - to.getColumn();
    }

    public int getAbsRowDiff() {
        return Math.abs(rowDiff);
    }

    public int getAbsColDiff() {
        return Math.abs(colDiff);
    }

    public boolean isTargetAbove() {
        return rowDiff < 0;
    }

    public boolean isTargetOnLeft() {
        return colDiff > 0;
    }

    public boolean isTargetOnSameRow() {
        return rowDiff == 0;
    }

    public boolean isTargetOnSameColumn() {
        return colDiff == 0;
    }

    public boolean isTargetOnDiagonal() {
        return Math.abs(colDiff) == Math.abs(rowDiff);
    }

    public RelativePosition getRelativeLocation() {
        int rowIncrementer = rowDiff == 0 ? 0 : rowDiff / Math.abs(rowDiff);
        int colIncrementer = colDiff == 0 ? 0 : colDiff / Math.abs(colDiff);
        return RelativePosition.of(rowIncrementer * -1, colIncrementer * -1);
    }
}
