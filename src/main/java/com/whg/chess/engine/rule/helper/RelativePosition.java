package com.whg.chess.engine.rule.helper;

import com.whg.chess.model.Coordinates;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum RelativePosition {

    NORTH_WEST(-1, +1),
    NORTH_EAST(+1, +1),
    SOUTH_WEST(-1, -1),
    SOUTH_EAST(+1, -1),
    EAST(+1, 0),
    WEST(-1, 0),
    NORTH(0, +1),
    SOUTH(0, -1);

    @Getter
    private final int rowIncrementer;

    @Getter
    private final int columnIncrementer;

    public static RelativePosition of(Integer rowIncrementer, Integer columnIncrementer) {
        return Arrays.stream(RelativePosition.values())
                .filter(value -> value.getRowIncrementer() == rowIncrementer)
                .filter(value -> value.getColumnIncrementer() == columnIncrementer)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No relative position found with rowIncrementer " + rowIncrementer + " and columnIncrementer " + columnIncrementer));
    }

    public static RelativePosition of(Coordinates from, Coordinates to) {
        int rowDiff = from.getRow() - to.getRow();
        int columnDiff = from.getColumn() - to.getColumn();

        int rowIncrementer = rowDiff / Math.abs(rowDiff);
        int colIncrementer = columnDiff / Math.abs(columnDiff);

        return RelativePosition.of(rowIncrementer, colIncrementer);
    }
}
