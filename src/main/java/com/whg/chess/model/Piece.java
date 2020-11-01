package com.whg.chess.model;

import com.whg.chess.model.enums.Color;
import com.whg.chess.model.enums.PieceName;
import lombok.Data;

@Data
public class Piece {
    private final PieceName name;
    private final Color color;
}
