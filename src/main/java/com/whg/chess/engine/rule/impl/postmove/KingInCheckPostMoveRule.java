package com.whg.chess.engine.rule.impl.postmove;

import com.whg.chess.engine.rule.Rule;
import com.whg.chess.engine.rule.impl.piece.PieceRule;
import com.whg.chess.model.*;
import com.whg.chess.model.enums.PieceName;
import com.whg.chess.model.enums.ValidationStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@PostMoveRule
public class KingInCheckPostMoveRule implements Rule {

    private final List<Rule> pieceRules;

    public KingInCheckPostMoveRule(@PieceRule List<Rule> pieceRules) {
        this.pieceRules = pieceRules;
    }

    @Override
    public Boolean canValidate(Board board, Move theLastMove) {
        return true;
    }

    @Override
    public ValidationResult validate(Board board, Move move) {

        Optional<Coordinates> squareWithKingOpt = board.getSquaresWithPieces(move.getColor())
                .stream()
                .filter(square -> square.getPiece().getName() == PieceName.KING)
                .findFirst()
                .map(Square::getCoordinates);

        if (squareWithKingOpt.isPresent()) {
            if (!isKingUnderAttack(board, move, squareWithKingOpt.get())) {
                return new ValidationResult(ValidationStatus.PASSED);
            } else {
                return new ValidationResult(ValidationStatus.FAILED, "The " + move.getColor() + " King is under attack!");
            }
        } else {
            return new ValidationResult(ValidationStatus.PASSED);
        }

    }

    private boolean isKingUnderAttack(Board board, Move move, Coordinates squareWithKing) {
        List<Square> opponentsPieces = board.getSquaresWithPieces(move.getColor().getOpposite());

        return opponentsPieces
                .stream()
                .flatMap(attackingPieceSquare -> isKingUnderAttack(board, squareWithKing, attackingPieceSquare))
                .anyMatch(ValidationResult::isSuccess);
    }

    private Stream<ValidationResult> isKingUnderAttack(Board board, Coordinates squareWithKing, Square attackingPieceSquare) {
        Move captureKing = new Move(attackingPieceSquare.getPiece().getColor(), attackingPieceSquare.getCoordinates(), squareWithKing);

        return this.pieceRules
                .stream()
                .filter(rule -> rule.canValidate(board, captureKing))
                .map(rule -> rule.validate(board, captureKing));
    }

}
