package com.whg.chess.engine.validator.impl.postmove;

import com.whg.chess.engine.validator.impl.Rule;
import com.whg.chess.engine.validator.utils.PositionUtils;
import com.whg.chess.model.Board;
import com.whg.chess.model.Move;
import com.whg.chess.model.ValidationResult;
import com.whg.chess.model.enums.ValidationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@PostMoveRule
@RequiredArgsConstructor
public class KingInCheckPositionRule implements Rule {

    private final PositionUtils positionUtils;

    @Override
    public Boolean canValidate(Board board, Move move) {
        return true;
    }

    @Override
    public ValidationResult validate(Board board, Move move) {
        return new ValidationResult(ValidationStatus.PASSED);
    }

}
