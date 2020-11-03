package com.whg.chess.engine;

import com.whg.chess.engine.validator.exceptions.ValidationException;
import com.whg.chess.engine.validator.impl.Rule;
import com.whg.chess.engine.validator.impl.piece.PieceRule;
import com.whg.chess.engine.validator.impl.postmove.PostMoveRule;
import com.whg.chess.engine.validator.impl.general.GeneralRule;
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
    private final List<Rule> postMoveValidators;

    public GameEngine(
            @GeneralRule List<Rule> generalRules,
            @PieceRule List<Rule> pieceRules,
            @PostMoveRule List<Rule> postMoveValidators
    ) {
        this.generalRules = generalRules;
        this.pieceRules = pieceRules;
        this.postMoveValidators = postMoveValidators;
    }

    public Board performMove(Board board, Move move) {

        validateAgainstRules(generalRules, board, move);
        validateAgainstRules(pieceRules, board, move);

        Board newBoard = performNewMove(board, move);

        validateAgainstRules(postMoveValidators, board, move);

        return newBoard;
    }

    private void validateAgainstRules(List<Rule> rules, Board board, Move move) {
        ValidationResult validationResult = validateRules(rules, board, move);
        if (validationResult.isFailed()) {
            throw new ValidationException(validationResult.getExplanation());
        }
    }

    private Board performNewMove(Board board, Move move) {
        Board newPosition = getNewPosition(board, move);

        ValidationResult positionValidationResult = validatePosition(newPosition, move);
        if (positionValidationResult.isSuccess()) {
            return newPosition;
        } else {
            throw new ValidationException(positionValidationResult.getExplanation());
        }
    }

    private ValidationResult validatePosition(Board newPosition, Move move) {
        return postMoveValidators
                .stream()
                .map(validator -> validator.validate(newPosition, move))
                .filter(ValidationResult::isFailed)
                .findFirst()
                .orElse(new ValidationResult(ValidationStatus.PASSED));
    }

    private ValidationResult validateRules(List<Rule> rules, Board board, Move move) {
        return rules
                .stream()
                .filter(validator -> validator.canValidate(board, move))
                .map(validator -> validator.validate(board, move))
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
