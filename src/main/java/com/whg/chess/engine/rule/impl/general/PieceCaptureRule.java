package com.whg.chess.engine.rule.impl.general;

import com.whg.chess.engine.rule.Rule;
import com.whg.chess.model.*;
import com.whg.chess.model.enums.PieceName;
import com.whg.chess.model.enums.ValidationStatus;
import org.springframework.stereotype.Component;

@Component
@GeneralRule
public class PieceCaptureRule implements Rule {

    @Override
    public Boolean canValidate(Board board, Move move) {
        Square toSquare = board.getSquare(move.getTo());
        return toSquare.getPiece() != null;
    }

    @Override
    public ValidationResult validate(Board board, Move move) {
        Square toSquare = board.getSquare(move.getTo());

        Piece pieceToCapture = toSquare.getPiece();
        if (pieceToCapture.getColor() == move.getColor()) {
            return new ValidationResult(ValidationStatus.FAILED, move.getColor() + " color player is trying to capture their own piece at " + toSquare.getCoordinates());
        }

        if (pieceToCapture.getName() == PieceName.KING) {
            return new ValidationResult(ValidationStatus.FAILED, move.getColor() + " color player is trying to capture an opponent's king at " + toSquare.getCoordinates());
        }

        return new ValidationResult(ValidationStatus.PASSED);
    }

}
