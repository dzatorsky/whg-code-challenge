package com.whg.chess.engine.rule.impl.postmove;

import com.whg.chess.engine.rule.Rule;
import com.whg.chess.engine.rule.helper.KingUtils;
import com.whg.chess.model.Board;
import com.whg.chess.model.Move;
import com.whg.chess.model.ValidationResult;
import com.whg.chess.model.enums.ValidationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@PostMoveRule
@RequiredArgsConstructor
public class KingInCheckPostMoveRule implements Rule {

    private final KingUtils kingUtils;

    @Override
    public Boolean canValidate(Board board, Move theLastMove) {
        return true;
    }

    @Override
    public ValidationResult validate(Board board, Move move) {
        if (kingUtils.isKingUnderAttack(board, move.getColor())) {
            return new ValidationResult(ValidationStatus.FAILED, "The " + move.getColor() + " King is under attack!");
        } else {
            return new ValidationResult(ValidationStatus.PASSED);
        }
    }

}
