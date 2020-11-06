package com.whg.chess.view.cmd;

import com.whg.chess.config.AppConfig;
import com.whg.chess.model.Board;
import com.whg.chess.model.Coordinates;
import com.whg.chess.model.Piece;
import com.whg.chess.model.Square;
import com.whg.chess.model.enums.Color;
import com.whg.chess.view.BoardRenderer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsoleBoardRenderer implements BoardRenderer {

    private final AppConfig appConfig;

    @Override
    public void renderBoard(Board board) {
        StringBuilder builder = new StringBuilder();

        Arrays.stream(board.getSquares())
                .sorted(reverseOrder())
                .forEach(row -> {
                    String rowAsStr = Arrays.stream(row)
                            .map(this::renderSquare)
                            .collect(Collectors.joining(" "));

                    builder.append(row[0].getCoordinates().getRow() + 1).append("| ");
                    builder.append(rowAsStr);
                    builder.append("\n");
                });

        builder.append("   ").append(Coordinates.VALID_COLUMNS.stream().map(letter -> "-").collect(Collectors.joining(" ")));
        builder.append("\n");
        builder.append("   ").append(String.join(" ", Coordinates.VALID_COLUMNS));
        builder.append("\n");

        System.out.println(builder.toString());
    }

    private Comparator<Square[]> reverseOrder() {
        return (row1, row2) -> -1;
    }

    private String renderSquare(Square square) {
//        return "[" + square.getCoordinates().getRow() + "," + square.getCoordinates().getColumn()+"]";

        return Optional.ofNullable(square)
                .map(Square::getPiece)
                .map(this::getPieceName)
                .orElse(".");
    }

    private String getPieceName(Piece piece) {
        String name = appConfig.getNameMappings().get(piece.getName());

        if (piece.getColor() == Color.WHITE) {
            return name.toUpperCase();
        } else {
            return name.toLowerCase();
        }
    }
}
