package com.whg.chess.engine.validator.move.impl;

import com.whg.chess.engine.validator.move.MoveValidator;
import com.whg.chess.model.*;
import com.whg.chess.model.enums.PieceName;
import com.whg.chess.model.enums.ValidationStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Component
public class RookMoveValidator implements MoveValidator {

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

        if (targetOnSameRow(from, to)) {
            return validateHorizontal(board, move);
        } else if (targetOnSameColumn(from, to)) {
            return validateVertical(board, move);
        } else {
            return new ValidationResult(ValidationStatus.FAILED, "Target square " + to + " can't be reached by the rook at " + from);
        }

    }

    private ValidationResult validateVertical(Board board, Move move) {
        Coordinates from = move.getFrom();
        Coordinates to = move.getTo();

        List<Square> targetColumn = board.getColumn(from.getColumn());

        if (targetIsAbove(from, to)) {
            return validatePath(from.getRow(), to.getRow(), targetColumn);
        } else {
            return validatePath(to.getRow(), from.getRow(), targetColumn);
        }
    }

    private ValidationResult validateHorizontal(Board board, Move move) {
        Coordinates from = move.getFrom();
        Coordinates to = move.getTo();

        List<Square> targetRow = board.getRow(from.getRow());

        if (targetOnLeft(from, to)) {
            return validatePath(to.getColumn(), from.getColumn(), targetRow);
        } else {
            return validatePath(from.getColumn(), to.getColumn(), targetRow);
        }
    }

    private ValidationResult validatePath(Integer from, Integer to, List<Square> path) {
        int excludedFrom = from + 1;

        return IntStream.range(excludedFrom, to)
                .boxed()
                .map(i -> validateNoPiece(path.get(i)))
                .filter(ValidationResult::isFailed)
                .findFirst()
                .orElse(new ValidationResult(ValidationStatus.PASSED));
    }

    private ValidationResult validateNoPiece(Square square) {
        if (square.getPiece() != null) {
            return new ValidationResult(ValidationStatus.FAILED, "There is a piece standing on the rook path at " + square.getCoordinates());
        } else {
            return new ValidationResult(ValidationStatus.PASSED);
        }
    }

    private boolean targetIsAbove(Coordinates from, Coordinates to) {
        return from.getRow() < to.getRow();
    }

    private boolean targetOnLeft(Coordinates from, Coordinates to) {
        return from.getColumn() > to.getColumn();
    }

    private boolean targetOnSameRow(Coordinates from, Coordinates to) {
        return from.getRow().equals(to.getRow());
    }

    private boolean targetOnSameColumn(Coordinates from, Coordinates to) {
        return from.getColumn().equals(to.getColumn());
    }

}
