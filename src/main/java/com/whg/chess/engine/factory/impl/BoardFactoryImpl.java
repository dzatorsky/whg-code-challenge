package com.whg.chess.engine.factory.impl;

import com.whg.chess.config.AppConfig;
import com.whg.chess.engine.factory.BoardFactory;
import com.whg.chess.model.Board;
import com.whg.chess.model.Coordinates;
import com.whg.chess.model.Piece;
import com.whg.chess.model.Square;
import com.whg.chess.model.enums.Color;
import com.whg.chess.model.enums.PieceName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BoardFactoryImpl implements BoardFactory {

    private final AppConfig appConfig;

    @Override
    public Board getClearBoard() {
        return new Board(appConfig.getBoardSize());
    }

    @Override
    public Board getStartingBoard() {

        Map<String, Piece> piecesLocations = new HashMap<>();

        piecesLocations.put("a1", new Piece(PieceName.ROOK, Color.WHITE));
        piecesLocations.put("b1", new Piece(PieceName.KNIGHT, Color.WHITE));
        piecesLocations.put("c1", new Piece(PieceName.BISHOP, Color.WHITE));
        piecesLocations.put("d1", new Piece(PieceName.QUEEN, Color.WHITE));
        piecesLocations.put("e1", new Piece(PieceName.KING, Color.WHITE));
        piecesLocations.put("f1", new Piece(PieceName.BISHOP, Color.WHITE));
        piecesLocations.put("g1", new Piece(PieceName.KNIGHT, Color.WHITE));
        piecesLocations.put("h1", new Piece(PieceName.ROOK, Color.WHITE));
        piecesLocations.put("a2", new Piece(PieceName.PAWN, Color.WHITE));
        piecesLocations.put("b2", new Piece(PieceName.PAWN, Color.WHITE));
        piecesLocations.put("c2", new Piece(PieceName.PAWN, Color.WHITE));
        piecesLocations.put("d2", new Piece(PieceName.PAWN, Color.WHITE));
        piecesLocations.put("e2", new Piece(PieceName.PAWN, Color.WHITE));
        piecesLocations.put("f2", new Piece(PieceName.PAWN, Color.WHITE));
        piecesLocations.put("g2", new Piece(PieceName.PAWN, Color.WHITE));
        piecesLocations.put("h2", new Piece(PieceName.PAWN, Color.WHITE));
        piecesLocations.put("a8", new Piece(PieceName.ROOK, Color.BLACK));
        piecesLocations.put("b8", new Piece(PieceName.KNIGHT, Color.BLACK));
        piecesLocations.put("c8", new Piece(PieceName.BISHOP, Color.BLACK));
        piecesLocations.put("d8", new Piece(PieceName.QUEEN, Color.BLACK));
        piecesLocations.put("e8", new Piece(PieceName.KING, Color.BLACK));
        piecesLocations.put("f8", new Piece(PieceName.BISHOP, Color.BLACK));
        piecesLocations.put("g8", new Piece(PieceName.KNIGHT, Color.BLACK));
        piecesLocations.put("h8", new Piece(PieceName.ROOK, Color.BLACK));
        piecesLocations.put("a7", new Piece(PieceName.PAWN, Color.BLACK));
        piecesLocations.put("b7", new Piece(PieceName.PAWN, Color.BLACK));
        piecesLocations.put("c7", new Piece(PieceName.PAWN, Color.BLACK));
        piecesLocations.put("d7", new Piece(PieceName.PAWN, Color.BLACK));
        piecesLocations.put("e7", new Piece(PieceName.PAWN, Color.BLACK));
        piecesLocations.put("f7", new Piece(PieceName.PAWN, Color.BLACK));
        piecesLocations.put("g7", new Piece(PieceName.PAWN, Color.BLACK));
        piecesLocations.put("h7", new Piece(PieceName.PAWN, Color.BLACK));

        Board board = this.getClearBoard();

        piecesLocations.forEach((key, value) -> {
            Square square = board.getSquare(Coordinates.of(key));
            square.setPiece(value);
        });

        return board;
    }

}
