package com.whg.chess.engine.validator.position;

import com.whg.chess.model.ValidationResult;
import com.whg.chess.model.Board;

public interface PositionValidator {
    ValidationResult validate(Board board);
}
