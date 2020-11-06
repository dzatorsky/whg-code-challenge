package com.whg.chess.engine.rule;

import com.whg.chess.model.Board;
import com.whg.chess.model.Move;
import com.whg.chess.model.ValidationResult;

public interface Rule {
    Boolean canValidate(Board board, Move move);
    ValidationResult validate(Board board, Move move);
}
