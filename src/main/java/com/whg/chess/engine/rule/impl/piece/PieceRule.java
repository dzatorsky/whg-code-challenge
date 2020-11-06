package com.whg.chess.engine.rule.impl.piece;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This qualifier marks beans that validate moves against piece move rules
 * e.g. knight must move in L shape, bishop can't capture a piece with another piece on its path, etc.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface PieceRule {
}