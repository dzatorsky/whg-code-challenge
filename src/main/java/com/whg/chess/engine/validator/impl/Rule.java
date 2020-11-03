package com.whg.chess.engine.validator.impl;

import com.whg.chess.model.ValidationResult;
import com.whg.chess.model.Board;
import com.whg.chess.model.Move;

public interface Rule {
    Boolean canValidate(Board board, Move move);

    ValidationResult validate(Board board, Move move);
}
