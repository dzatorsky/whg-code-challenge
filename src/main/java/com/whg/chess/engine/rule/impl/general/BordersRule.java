package com.whg.chess.engine.rule.impl.general;

import com.whg.chess.engine.rule.Rule;
import com.whg.chess.model.Board;
import com.whg.chess.model.Coordinates;
import com.whg.chess.model.Move;
import com.whg.chess.model.ValidationResult;
import com.whg.chess.model.enums.ValidationStatus;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@GeneralRule
public class BordersRule implements Rule {

    @Override
    public Boolean canValidate(Board board, Move move) {
        return true;
    }

    @Override
    public ValidationResult validate(Board board, Move move) {
        return Stream.of(validateBorders(board, move.getFrom()), validateBorders(board, move.getTo()))
                .filter(result -> result.getValidationStatus() == ValidationStatus.FAILED)
                .findFirst()
                .orElse(new ValidationResult(ValidationStatus.PASSED));
    }

    private ValidationResult validateBorders(Board board, Coordinates coordinates) {
        if (coordinates.getRow() > board.getSize() || coordinates.getColumn() > board.getSize()) {
            return new ValidationResult(ValidationStatus.FAILED, "The piece is illegal since it goes outside the board borders: " + coordinates);
        } else {
            return new ValidationResult(ValidationStatus.PASSED);
        }
    }

}
