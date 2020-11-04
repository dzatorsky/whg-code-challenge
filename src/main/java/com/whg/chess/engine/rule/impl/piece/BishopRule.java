package com.whg.chess.engine.rule.impl.piece;

import com.whg.chess.engine.rule.Rule;
import com.whg.chess.engine.rule.helper.PositionDiff;
import com.whg.chess.engine.rule.helper.PositionUtils;
import com.whg.chess.model.*;
import com.whg.chess.model.enums.PieceName;
import com.whg.chess.model.enums.ValidationStatus;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Data
@Component
@PieceRule
public class BishopRule implements Rule {

    private final PositionUtils positionUtils;

    @Override
    public Boolean canValidate(Board board, Move move) {
        Square fromSquare = board.getSquare(move.getFrom());

        return Optional.of(fromSquare)
                .map(Square::getPiece)
                .map(Piece::getName)
                .filter(name -> name == PieceName.BISHOP)
                .isPresent();
    }

    @Override
    public ValidationResult validate(Board board, Move move) {
        Coordinates from = move.getFrom();
        Coordinates to = move.getTo();

        PositionDiff positionDiff = new PositionDiff(from, to);

        if (positionDiff.isTargetOnDiagonal()) {
            return positionUtils.validatePathIsNotBlocked(board, from, to);
        } else {
            return new ValidationResult(ValidationStatus.FAILED, "Bishop at " + from + " can't reach " + to + " since it's not on bishop's diagonal");
        }

    }

}
