package com.whg.chess.model;

import lombok.Data;

import java.util.List;

@Data
public class Coordinates {
    public static final List<String> VALID_COLUMNS = List.of("A", "B", "C", "D", "E", "F", "G", "H");

    private final Integer row;
    private final Integer column;

    public static Coordinates of(String notation) {
        String column = notation.substring(0, 1);
        String row = notation.substring(1);

        return new Coordinates(Integer.parseInt(row) - 1, VALID_COLUMNS.indexOf(column.toUpperCase()));
    }

    @Override
    public String toString() {
        return VALID_COLUMNS.get(column) + (row + 1);
    }
}
