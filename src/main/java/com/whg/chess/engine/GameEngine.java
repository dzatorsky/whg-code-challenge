package com.whg.chess.engine;

import com.whg.chess.engine.validator.exceptions.ValidationException;
import com.whg.chess.engine.validator.move.MoveValidator;
import com.whg.chess.engine.validator.position.PositionValidator;
import com.whg.chess.model.*;
import com.whg.chess.model.enums.ValidationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GameEngine {
    private final List<MoveValidator> moveValidators;
    private final List<PositionValidator> positionValidators;

    public Board performMove(Board board, Move move) {
        ValidationResult moveValidationResult = validateMove(board, move);

        if (moveValidationResult.isSuccess()) {
            return performNewMove(board, move);
        } else {
            throw new ValidationException(moveValidationResult.getExplanation());
        }
    }

    private Board performNewMove(Board board, Move move) {
        Board newPosition = getNewPosition(board, move);

        ValidationResult positionValidationResult = validatePosition(newPosition);
        if (positionValidationResult.isSuccess()) {
            return newPosition;
        } else {
            throw new ValidationException(positionValidationResult.getExplanation());
        }
    }

    private ValidationResult validatePosition(Board newPosition) {
        return positionValidators
                .stream()
                .map(validator -> validator.validate(newPosition))
                .filter(ValidationResult::isFailed)
                .findFirst()
                .orElse(new ValidationResult(ValidationStatus.PASSED));
    }

    private ValidationResult validateMove(Board board, Move move) {
        return moveValidators
                .stream()
                .filter(validator -> validator.canValidate(board, move))
                .map(validator -> validator.validate(board, move))
                .filter(ValidationResult::isFailed)
                .findFirst()
                .orElse(new ValidationResult(ValidationStatus.PASSED));
    }

    private Board getNewPosition(Board board, Move move) {
        Board actualPosition = new Board(board);

        Square fromSquare = actualPosition.getSquare(move.getFrom());
        Square toSquare = actualPosition.getSquare(move.getTo());

        toSquare.setPiece(fromSquare.getPiece());
        fromSquare.setPiece(null);
        return actualPosition;
    }

}
