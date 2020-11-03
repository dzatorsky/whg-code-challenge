package com.whg.chess.engine.validator.move.impl;

import com.whg.chess.engine.validator.model.PositionDiff;
import com.whg.chess.engine.validator.move.MoveValidator;
import com.whg.chess.engine.validator.utils.PositionUtils;
import com.whg.chess.model.*;
import com.whg.chess.model.enums.PieceName;
import com.whg.chess.model.enums.ValidationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RookMoveValidator implements MoveValidator {

    private final PositionUtils positionUtils;

    @Override
    public Boolean canValidate(Board board, Move move) {
        Square fromSquare = board.getSquare(move.getFrom());

        return Optional.of(fromSquare)
                .map(Square::getPiece)
                .map(Piece::getName)
                .filter(name -> name == PieceName.ROOK)
                .isPresent();
    }

    @Override
    public ValidationResult validate(Board board, Move move) {

        Coordinates from = move.getFrom();
        Coordinates to = move.getTo();

        PositionDiff positionDiff = new PositionDiff(from, to);

        if (positionDiff.isTargetOnSameColumn() || positionDiff.isTargetOnSameRow()) {
            return positionUtils.validatePathIsNotBlocked(board, from, to);
        } else {
            return new ValidationResult(ValidationStatus.FAILED, "Rook at " + from + " can't reach " + to + " since it's not on the same horizontal/vertical");
        }

    }

}
