package com.whg.chess.engine.validator.impl.piece;

import com.whg.chess.engine.validator.impl.Rule;
import com.whg.chess.engine.validator.model.PositionDiff;
import com.whg.chess.model.*;
import com.whg.chess.model.enums.PieceName;
import com.whg.chess.model.enums.ValidationStatus;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@PieceRule
public class KingRule implements Rule {

    public static final int KING_MOVE_DISTANCE = 1;

    private final List<Rule> pieceRules;

    public KingRule(@PieceRule List<Rule> pieceRules) {
        this.pieceRules = pieceRules;
    }

    @PostConstruct
    private void init() {
        // Strange Spring behavior. It won't inject KindRule in the list even though it implements RuleInterface and
        // it can be injected into any other class except this one.
        // Looks like Spring self injection doesn't work good still.
        pieceRules.add(this);
    }

    @Override
    public Boolean canValidate(Board board, Move move) {
        Square fromSquare = board.getSquare(move.getFrom());

        return Optional.of(fromSquare)
                .map(Square::getPiece)
                .map(Piece::getName)
                .filter(name -> name == PieceName.KING)
                .isPresent();
    }

    @Override
    public ValidationResult validate(Board board, Move move) {

        PositionDiff diff = new PositionDiff(move);

        if (diff.getAbsRowDiff() != KING_MOVE_DISTANCE && diff.getAbsColDiff() != KING_MOVE_DISTANCE) {
            return new ValidationResult(ValidationStatus.FAILED, "The king at " + move.getFrom() + " can't capture a piece at " + move.getTo() + " since it's too far away");
        } else {

            if (validateSquareIsUnderAttack(board, move)) {
                return new ValidationResult(ValidationStatus.FAILED, "The king at " + move.getFrom() + " can't move to " + move.getTo() + " since it will be under attack");
            } else {
                return new ValidationResult(ValidationStatus.PASSED);
            }
        }

    }

    private boolean validateSquareIsUnderAttack(Board board, Move move) {
        List<Square> opponentsPieces = board.getSquaresWithPieces(move.getColor().getOpposite());

        return opponentsPieces
                .stream()
                .filter(attackingPieceSquare -> isNotPieceToBeCapturedByKing(move, attackingPieceSquare))
                .flatMap(attackingPieceSquare -> validateSquareIsUnderAttack(board, move, attackingPieceSquare))
                .anyMatch(ValidationResult::isSuccess);
    }

    private Stream<ValidationResult> validateSquareIsUnderAttack(Board board, Move move, Square attackingPieceSquare) {
        Move captureKing = new Move(attackingPieceSquare.getPiece().getColor(), attackingPieceSquare.getCoordinates(), move.getTo());

        return this.pieceRules
                .stream()
                .filter(rule -> rule.canValidate(board, captureKing))
                .map(rule -> rule.validate(board, captureKing));
    }

    private boolean isNotPieceToBeCapturedByKing(Move move, Square square) {
        return !square.getCoordinates().equals(move.getTo());
    }

}
