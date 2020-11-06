package com.whg.chess.engine.rule.helper;

import com.whg.chess.engine.rule.Rule;
import com.whg.chess.engine.rule.impl.piece.PieceRule;
import com.whg.chess.model.*;
import com.whg.chess.model.enums.Color;
import com.whg.chess.model.enums.PieceName;
import com.whg.chess.model.enums.ValidationStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class PositionUtils {

    public ValidationResult validatePathIsNotBlocked(Board board, Coordinates from, Coordinates to) {
        PositionDiff diff = new PositionDiff(from, to);

        return validatePathRecursively(diff.getRelativeLocation(), board, from, to);
    }

    private ValidationResult validatePathRecursively(RelativePosition relativePosition, Board board, Coordinates from, Coordinates to) {
        int newRow = from.getRow() + relativePosition.getRowIncrementer();
        int newColumn = from.getColumn() + relativePosition.getColumnIncrementer();

        if (newRow == to.getRow() && newColumn == to.getColumn()) {
            return new ValidationResult(ValidationStatus.PASSED);
        } else {
            Coordinates newCoordinates = new Coordinates(newRow, newColumn);
            Square square = board.getSquare(newCoordinates);

            if (square.getPiece() != null) {
                return new ValidationResult(ValidationStatus.FAILED, to + " can't be reached since there is a piece at " + newCoordinates + " on the path");
            } else {
                return validatePathRecursively(relativePosition, board, newCoordinates, to);
            }
        }
    }
}
