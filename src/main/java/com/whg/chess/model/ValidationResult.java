package com.whg.chess.model;

import com.whg.chess.model.enums.ValidationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ValidationResult {
    private final ValidationStatus validationStatus;
    private String explanation;

    public Boolean isSuccess() {
        return validationStatus == ValidationStatus.PASSED;
    }

    public Boolean isFailed() {
        return validationStatus == ValidationStatus.FAILED;
    }
}
