package com.whg.chess.engine.validator.move.impl;

import com.whg.chess.engine.validator.move.MoveValidator;
import com.whg.chess.model.Board;
import com.whg.chess.model.Move;
import com.whg.chess.model.Square;
import com.whg.chess.model.ValidationResult;
import com.whg.chess.model.enums.ValidationStatus;
import org.springframework.stereotype.Component;

@Component
public class StartSquareMoveValidator implements MoveValidator {

    @Override
    public Boolean canValidate(Board board, Move move) {
        return true;
    }

    @Override
    public ValidationResult validate(Board board, Move move) {
        Square fromSquare = board.getSquare(move.getFrom());

        if (fromSquare.getPiece() == null) {
            return new ValidationResult(ValidationStatus.FAILED, "Piece was not found at: " + move.getFrom());
        } else {
            return new ValidationResult(ValidationStatus.PASSED);
        }
    }

}
