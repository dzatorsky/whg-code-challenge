package com.whg.chess.model;

import lombok.Data;

@Data
public class Tuple<L, R> {
    public final L left;
    public final R right;
} 