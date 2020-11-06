package com.whg.chess.engine.rule.impl.piece;

import com.whg.chess.engine.rule.Rule;
import com.whg.chess.engine.rule.helper.PositionDiff;
import com.whg.chess.engine.rule.helper.PathUtils;
import com.whg.chess.model.*;
import com.whg.chess.model.enums.PieceName;
import com.whg.chess.model.enums.ValidationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@PieceRule
@RequiredArgsConstructor
public class QueenRule implements Rule {

    private final PathUtils pathUtils;

    @Override
    public Boolean canValidate(Board board, Move move) {
        Square fromSquare = board.getSquare(move.getFrom());

        return Optional.of(fromSquare)
                .map(Square::getPiece)
                .map(Piece::getName)
                .filter(name -> name == PieceName.QUEEN)
                .isPresent();
    }

    @Override
    public ValidationResult validate(Board board, Move move) {

        PositionDiff diff = new PositionDiff(move);

        if (diff.isTargetOnDiagonal() || diff.isTargetOnLine()) {
            return pathUtils.validatePathIsNotBlocked(board, move.getFrom(), move.getTo());
        } else {
            return new ValidationResult(ValidationStatus.FAILED, "The queen at " + move.getFrom() + " can't move to " + move.getTo()
                    + " since the target is neither on the same diagonal nor on the same line");
        }

    }

}
