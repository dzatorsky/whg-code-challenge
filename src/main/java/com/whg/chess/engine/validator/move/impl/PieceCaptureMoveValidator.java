package com.whg.chess.engine.validator.move.impl;

import com.whg.chess.model.ValidationResult;
import com.whg.chess.model.enums.ValidationStatus;
import com.whg.chess.engine.validator.move.MoveValidator;
import com.whg.chess.model.Board;
import com.whg.chess.model.Move;
import com.whg.chess.model.Piece;
import com.whg.chess.model.Square;
import com.whg.chess.model.enums.PieceName;
import org.springframework.stereotype.Component;

@Component
public class PieceCaptureMoveValidator implements MoveValidator {

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
