package com.whg.chess.engine.validator.move;

import com.whg.chess.model.ValidationResult;
import com.whg.chess.model.Board;
import com.whg.chess.model.Move;

public interface MoveValidator {
    Boolean canValidate(Board board, Move move);

    ValidationResult validate(Board board, Move move);
}
