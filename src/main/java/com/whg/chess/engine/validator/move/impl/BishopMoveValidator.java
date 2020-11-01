package com.whg.chess.engine.validator.move.impl;

import com.whg.chess.engine.validator.move.MoveValidator;
import com.whg.chess.model.*;
import com.whg.chess.model.enums.PieceName;
import com.whg.chess.model.enums.ValidationStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BishopMoveValidator implements MoveValidator {

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

        if (isTargetOnSameDiagonal(from, to)) {
            int rowIncrementer = getRowIncrementer(from, to);
            int columnIncrementer = getColumnIncrementer(from, to);

            return validateDiagonal(rowIncrementer, columnIncrementer, board, from, to);
        } else {
            return new ValidationResult(ValidationStatus.FAILED, "Target at " + to + " is not reachable by Bishop at " + from);
        }

    }

    private ValidationResult validateDiagonal(int rowIncrementer, int columnIncrementer, Board board, Coordinates from, Coordinates to) {
        int newRow = from.getRow() + rowIncrementer;
        int newColumn = from.getColumn() + columnIncrementer;

        if (newRow == to.getRow() && newColumn == to.getColumn()) {
            return new ValidationResult(ValidationStatus.PASSED);
        } else {
            Coordinates newCoordinates = new Coordinates(newRow, newColumn);
            Square square = board.getSquare(newCoordinates);

            if (square.getPiece() != null) {
                return new ValidationResult(ValidationStatus.FAILED, "Bishop can't reach square at " + to + " since there is a piece in between at " + newCoordinates);
            } else {
                return validateDiagonal(rowIncrementer, columnIncrementer, board, newCoordinates, to);
            }
        }
    }

    private int getRowIncrementer(Coordinates from, Coordinates to) {
        if (targetIsAbove(from, to)) {
            return +1;
        } else {
            return -1;
        }
    }

    private int getColumnIncrementer(Coordinates from, Coordinates to) {
        if (targetOnRight(from, to)) {
            return 1;
        } else {
            return -1;
        }
    }

    private boolean isTargetOnSameDiagonal(Coordinates from, Coordinates to) {
        return Math.abs(from.getColumn() - to.getColumn()) == Math.abs(from.getRow() - to.getRow());
    }

    private boolean targetIsAbove(Coordinates from, Coordinates to) {
        return from.getRow() < to.getRow();
    }

    private boolean targetOnRight(Coordinates from, Coordinates to) {
        return from.getColumn() < to.getColumn();
    }


}
