package com.whg.chess.input;

import com.whg.chess.model.Move;

import java.util.List;

public interface MoveReaderAdapter {
    List<Move> readMoves(String location);
}
