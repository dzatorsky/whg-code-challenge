package com.whg.chess.engine.rule.impl.piece;

import com.whg.chess.engine.rule.Rule;
import com.whg.chess.engine.rule.helper.PositionDiff;
import com.whg.chess.engine.rule.helper.PathUtils;
import com.whg.chess.model.*;
import com.whg.chess.model.enums.Color;
import com.whg.chess.model.enums.PieceName;
import com.whg.chess.model.enums.ValidationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@PieceRule
@RequiredArgsConstructor
public class PawnRule implements Rule {

    public static final int NORMAL_MOVE_DISTANCE = 1;
    public static final int PAWN_CAPTURING_COLUMN_DISTANCE = 1;
    public static final int MAX_DISTANCE_FROM_START_POSITION = 2;

    private final PathUtils pathUtils;

    @Override
    public Boolean canValidate(Board board, Move move) {
        Square fromSquare = board.getSquare(move.getFrom());

        return Optional.of(fromSquare)
                .map(Square::getPiece)
                .map(Piece::getName)
                .filter(name -> name == PieceName.PAWN)
                .isPresent();
    }

    @Override
    public ValidationResult validate(Board board, Move move) {

        Square toSquare = board.getSquare(move.getTo());

        if (toSquare.getPiece() != null) {
            return validatePieceCapture(move);
        } else {
            return validateMove(board, move);
        }

    }

    private ValidationResult validatePieceCapture(Move move) {
        PositionDiff positionDiff = new PositionDiff(move);

        if (positionDiff.getAbsColDiff() != PAWN_CAPTURING_COLUMN_DISTANCE || positionDiff.getRowDiff() != getAllowedRowDiff(move.getColor())) {
            return new ValidationResult(ValidationStatus.FAILED, "Pawn located at " + move.getFrom() + " can't capture a piece at " + move.getTo() + " because it's not located 1 square diagonally to the pawn's direction");
        } else {
            return new ValidationResult(ValidationStatus.PASSED);
        }
    }

    private ValidationResult validateMove(Board board, Move move) {
        PositionDiff positionDiff = new PositionDiff(move);

        if (positionDiff.isTargetOnSameColumn()) {
            return validateMoveAhead(board, move, positionDiff);
        } else {
            return new ValidationResult(ValidationStatus.FAILED, "Pawn at " + move.getFrom() + " can be moved vertically only.");
        }
    }

    private ValidationResult validateMoveAhead(Board board, Move move, PositionDiff positionDiff) {
        if (isAllowedToMoveAhead1Square(move.getColor(), positionDiff) || isAllowedToMoveAhead2Squares(move.getColor(), positionDiff, move, board)) {
            return this.pathUtils.validatePathIsNotBlocked(board, move.getFrom(), move.getTo());
        } else {
            return new ValidationResult(ValidationStatus.FAILED, move.getColor() + " pawn on " + move.getFrom() + " is not allowed to piece to " + move.getTo());
        }
    }

    private boolean isAllowedToMoveAhead1Square(Color color, PositionDiff positionDiff) {
        return positionDiff.getRowDiff() == getAllowedRowDiff(color);
    }

    private boolean isAllowedToMoveAhead2Squares(Color color, PositionDiff positionDiff, Move move, Board board) {
        return is2SquareMove(color, positionDiff) && isOnStartingPosition(color, move, board);
    }

    private boolean isOnStartingPosition(Color color, Move move, Board board) {
        return move.getFrom().getRow() == getPawnStartingRowIndex(color, board);
    }

    private boolean is2SquareMove(Color color, PositionDiff positionDiff) {
        return positionDiff.getRowDiff() == getAllowedRowDiff(color) * MAX_DISTANCE_FROM_START_POSITION;
    }

    private int getAllowedRowDiff(Color color) {
        if (color == Color.WHITE) {
            return -1;
        } else {
            return +1;
        }
    }

    private int getPawnStartingRowIndex(Color color, Board board) {
        if (color == Color.WHITE) {
            return 1;
        } else {
            return board.getSize() - 2;
        }
    }

}
