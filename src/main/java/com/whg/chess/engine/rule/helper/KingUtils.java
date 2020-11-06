package com.whg.chess.engine.rule.helper;

import com.whg.chess.engine.rule.Rule;
import com.whg.chess.engine.rule.impl.piece.PieceRule;
import com.whg.chess.model.*;
import com.whg.chess.model.enums.Color;
import com.whg.chess.model.enums.PieceName;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class KingUtils {

    private final List<Rule> pieceRules;

    public KingUtils(@PieceRule @Lazy List<Rule> pieceRules) {
        this.pieceRules = pieceRules;
    }

    public boolean isKingUnderAttack(Board board, Color kingColor) {
        Optional<Square> squAreWithKing = board.getSquaresWithPieces(kingColor)
                .stream()
                .filter(square -> square.getPiece() != null)
                .filter(square -> square.getPiece().getName() == PieceName.KING)
                .findFirst();

        if (squAreWithKing.isPresent()) {
            List<Square> opponentsPieces = board.getSquaresWithPieces(kingColor.getOpposite());

            return opponentsPieces
                    .stream()
                    .anyMatch(attackingPieceSquare -> isKingUnderAttack(board, squAreWithKing.get().getCoordinates(), attackingPieceSquare));
        } else {
            return false;
        }
    }

    public boolean isKingUnderAttack(Board board, Coordinates squareWithKing, Square attackingPieceSquare) {
        Move captureKing = new Move(attackingPieceSquare.getPiece().getColor(), attackingPieceSquare.getCoordinates(), squareWithKing);

        return this.pieceRules
                .stream()
                .filter(rule -> rule.canValidate(board, captureKing))
                .map(rule -> rule.validate(board, captureKing))
                .anyMatch(ValidationResult::isSuccess);
    }
}
