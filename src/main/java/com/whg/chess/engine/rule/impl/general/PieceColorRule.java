package com.whg.chess.engine.rule.impl.general;

import com.whg.chess.engine.rule.Rule;
import com.whg.chess.model.Board;
import com.whg.chess.model.Move;
import com.whg.chess.model.Square;
import com.whg.chess.model.ValidationResult;
import com.whg.chess.model.enums.Color;
import com.whg.chess.model.enums.ValidationStatus;
import org.springframework.stereotype.Component;

@Component
@GeneralRule
public class PieceColorRule implements Rule {

    @Override
    public Boolean canValidate(Board board, Move move) {
        Square fromSquare = board.getSquare(move.getFrom());
        return fromSquare.getPiece() != null;
    }

    @Override
    public ValidationResult validate(Board board, Move move) {
        Square fromSquare = board.getSquare(move.getFrom());

        Color pieceColor = fromSquare.getPiece().getColor();

        if (pieceColor != move.getColor()) {
            return new ValidationResult(ValidationStatus.FAILED, move.getColor() + " color player is trying to piece with a " + pieceColor + " piece.");
        } else {
            return new ValidationResult(ValidationStatus.PASSED);
        }
    }

}
