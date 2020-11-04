package com.whg.chess.engine.rule.impl.piece;

import com.whg.chess.engine.rule.Rule;
import com.whg.chess.engine.rule.helper.PositionDiff;
import com.whg.chess.model.*;
import com.whg.chess.model.enums.PieceName;
import com.whg.chess.model.enums.ValidationStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@PieceRule
@Component
public class KnightRule implements Rule {

    public static final int ONE_SQUARE_MOVE = 1;
    public static final int TWO_SQUARES_MOVE = 2;

    @Override
    public Boolean canValidate(Board board, Move move) {
        Square fromSquare = board.getSquare(move.getFrom());

        return Optional.of(fromSquare)
                .map(Square::getPiece)
                .map(Piece::getName)
                .filter(name -> name == PieceName.KNIGHT)
                .isPresent();
    }

    @Override
    public ValidationResult validate(Board board, Move move) {

        PositionDiff diff = new PositionDiff(move);

        if (isLPath(diff)) {
            return new ValidationResult(ValidationStatus.PASSED);
        } else {
            return new ValidationResult(ValidationStatus.FAILED, "The knight at " + move.getFrom() + " can't move to " + move.getTo()
                    + " since the target is not reachable via L path");
        }

    }

    private boolean isLPath(PositionDiff diff) {
        return diff.getAbsRowDiff() == ONE_SQUARE_MOVE && diff.getAbsColDiff() == TWO_SQUARES_MOVE
                || diff.getAbsRowDiff() == TWO_SQUARES_MOVE && diff.getAbsColDiff() == ONE_SQUARE_MOVE;
    }

}
