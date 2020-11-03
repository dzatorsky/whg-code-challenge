package com.whg.chess.model.enums;

public enum Color {
    WHITE, BLACK;

    public Color getOpposite() {
        return this == Color.WHITE ? Color.BLACK : Color.WHITE;
    }
}
