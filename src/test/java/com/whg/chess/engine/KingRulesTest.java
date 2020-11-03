package com.whg.chess.engine;

import com.whg.chess.engine.factory.BoardFactory;
import com.whg.chess.engine.validator.exceptions.ValidationException;
import com.whg.chess.model.*;
import com.whg.chess.model.enums.Color;
import com.whg.chess.model.enums.PieceName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayName("Requirement #5.1. The king can move only 1 square but in any direction")
class KingRulesTest {

    @Autowired
    private GameEngine engine;

    @Autowired
    private BoardFactory boardFactory;

    @ParameterizedTest
    @CsvSource({
            "E4,E5, King capturing a piece on north",
            "E4,F4, King capturing a piece on east",
            "E4,D4, King capturing a piece on west",
            "E4,E3, King capturing a piece on south",
            "E4,F5, King capturing a piece on north-east",
            "E4,D5, King capturing a piece on north-west",
            "E4,F3, King capturing a piece on south-east",
            "E4,D3, King capturing a piece on south-west",
    })
    @DisplayName("Test valid King captures")
    void testValidCaptures(Coordinates from, Coordinates to, String comment) {
        Board board = boardFactory.getClearBoard();

        setKing(board, from);
        setOpponentsPiece(board, to);

        Board afterMove = engine.performMove(board, new Move(Color.WHITE, from, to));

        validatePieceCaptured(to, afterMove);
    }

    @ParameterizedTest
    @CsvSource({
            "E4,F5, PAWN,G6, Capturing a piece defended by a pawn",
            "E4,F5, KNIGHT,G7, Capturing a piece defended by a knight",
            "E4,F5, BISHOP,C8, Capturing a piece defended by a bishop",
            "E4,F5, ROOK,F8, Capturing a piece defended by a rook",
            "E4,F5, QUEEN,H5, Capturing a piece defended by a queen",
            "E4,F5, KING,E6, Capturing a piece defended by a king"
    })
    @DisplayName("Test capturing pieces defended by pieces")
    void testInvalidCaptures(Coordinates from, Coordinates to, PieceName defender, Coordinates defenderPosition, String comment) {
        Board board = boardFactory.getClearBoard();

        setKing(board, from);
        setOpponentsPiece(board, to);
        setOpponentsPiece(board, defenderPosition, defender);

        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> engine.performMove(board, new Move(Color.WHITE, from, to))
        );

        assertThat(thrown.getMessage(), containsString("The king at " + from + " can't move to " + to + " since it will be under attack"));
    }

    @ParameterizedTest
    @CsvSource({
            "E4,E6, King moving to far to north",
            "E4,G4, King moving to far to east",
            "E4,C4, King moving to far to west",
            "E4,E2, King moving to far to south",
            "E4,G6, King moving to far to north-east",
            "E4,C6, King moving to far to north-west",
            "E4,G2, King moving to far to south-east",
            "E4,C2, King moving to far to south-west",
    })
    @DisplayName("Test invalid moves")
    void testInvalidMoves(Coordinates from, Coordinates to, String comment) {
        Board board = boardFactory.getClearBoard();

        setKing(board, from);
        setOpponentsPiece(board, to);

        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> engine.performMove(board, new Move(Color.WHITE, from, to))
        );

        assertThat(thrown.getMessage(), containsString("The king at " + from + " can't capture a piece at " + to + " since it's too far away"));
    }

    private void validatePieceCaptured(Coordinates to, Board afterMove) {
        assertEquals(PieceName.KING, afterMove.getSquare(to).getPiece().getName());

        List<Square> whitePieces = afterMove.getSquaresWithPieces(Color.WHITE);
        assertThat(whitePieces, hasSize(1));

        List<Square> blackPieces = afterMove.getSquaresWithPieces(Color.BLACK);
        assertThat(blackPieces, hasSize(0));
    }

    private void setOpponentsPiece(Board board, Coordinates coordinates) {
        setOpponentsPiece(board, coordinates, PieceName.BISHOP);
    }

    private void setOpponentsPiece(Board board, Coordinates coordinates, PieceName pieceName) {
        Square opponentsPieceSquare = board.getSquare(coordinates);
        opponentsPieceSquare.setPiece(new Piece(pieceName, Color.BLACK));
    }

    private void setKing(Board board, Coordinates coordinates) {
        Square squareWithWhiteBishop = board.getSquare(coordinates);
        squareWithWhiteBishop.setPiece(new Piece(PieceName.KING, Color.WHITE));
    }

}
