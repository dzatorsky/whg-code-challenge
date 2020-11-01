package com.whg.chess.model;

import com.whg.chess.model.enums.Color;
import lombok.Data;

@Data
public class Move {
    private final Color color;

    private final Coordinates from;
    private final Coordinates to;
}
