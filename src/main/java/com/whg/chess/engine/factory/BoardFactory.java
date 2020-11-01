package com.whg.chess.engine.factory;

import com.whg.chess.model.Board;

public interface BoardFactory {
    Board getClearBoard();
    Board getStartingBoard();
}
