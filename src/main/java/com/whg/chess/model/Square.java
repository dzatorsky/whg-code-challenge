package com.whg.chess.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Square {
    private Piece piece;
    private final Coordinates coordinates;
}

