package com.whg.chess.engine;

import com.whg.chess.engine.rule.Rule;
import com.whg.chess.engine.rule.exceptions.ChessRuleException;
import com.whg.chess.engine.rule.impl.general.GeneralRule;
import com.whg.chess.engine.rule.impl.piece.PieceRule;
import com.whg.chess.engine.rule.impl.postmove.PostMoveRule;
import com.whg.chess.model.Board;
import com.whg.chess.model.Move;
import com.whg.chess.model.Square;
import com.whg.chess.model.ValidationResult;
import com.whg.chess.model.enums.ValidationStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GameEngine {

    private final List<Rule> generalRules;
    private final List<Rule> pieceRules;
    private final List<Rule> postMoveRules;

    public GameEngine(
            @GeneralRule List<Rule> generalRules,
            @PieceRule List<Rule> pieceRules,
            @PostMoveRule List<Rule> postMoveRules
    ) {
        this.generalRules = generalRules;
        this.pieceRules = pieceRules;
        this.postMoveRules = postMoveRules;
    }

    public Board performMove(Board board, Move move) {

        validateAgainstRules(generalRules, board, move);
        validateAgainstRules(pieceRules, board, move);

        return performNewMove(board, move);
    }

    private void validateAgainstRules(List<Rule> rules, Board board, Move move) {
        ValidationResult validationResult = validateRules(rules, board, move);
        if (validationResult.isFailed()) {
            throw new ChessRuleException(validationResult.getExplanation());
        }
    }

    private Board performNewMove(Board board, Move move) {
        Board newPosition = getNewPosition(board, move);

        ValidationResult positionValidationResult = validateRules(postMoveRules, newPosition, move);
        if (positionValidationResult.isSuccess()) {
            return newPosition;
        } else {
            throw new ChessRuleException(positionValidationResult.getExplanation());
        }
    }

    private ValidationResult validateRules(List<Rule> rules, Board board, Move move) {
        return rules
                .stream()
                .filter(rule -> rule.canValidate(board, move))
                .map(rule -> rule.validate(board, move))
                .filter(ValidationResult::isFailed)
                .findFirst()
                .orElse(new ValidationResult(ValidationStatus.PASSED));
    }

    private Board getNewPosition(Board board, Move move) {
        Board actualPosition = new Board(board);

        Square fromSquare = actualPosition.getSquare(move.getFrom());
        Square toSquare = actualPosition.getSquare(move.getTo());

        toSquare.setPiece(fromSquare.getPiece());
        fromSquare.setPiece(null);
        return actualPosition;
    }

}
