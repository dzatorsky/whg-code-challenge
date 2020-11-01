package com.whg.chess.engine;

import com.whg.chess.engine.factory.BoardFactory;
import com.whg.chess.engine.validator.exceptions.ValidationException;
import com.whg.chess.model.*;
import com.whg.chess.model.enums.Color;
import com.whg.chess.model.enums.PieceName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

import static com.whg.chess.model.Coordinates.of;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayName("Requirement #5.2: The bishop can move any number of squares but only diagonally")
class BishopValidationTest {

    @Autowired
    private GameEngine engine;

    @Autowired
    private BoardFactory boardFactory;

    @ParameterizedTest
    @MethodSource("getValidMoves")
    @DisplayName("Bishop can reach target at different positions on diagonal")
    void testValidMoves(Coordinates from, Coordinates to) {
        Board board = boardFactory.getClearBoard();

        setBishop(board, from);
        setOpponentsPiece(board, to);

        Board afterMove = engine.performMove(board, new Move(Color.WHITE, from, to));

        validatePieceCaptured(to, afterMove);
    }

    private static Stream<Arguments> getValidMoves() {
        return Stream.of(
                Arguments.of(of("e4"), of("h7")),
                Arguments.of(of("e4"), of("h1")),
                Arguments.of(of("e4"), of("a8")),
                Arguments.of(of("e4"), of("b1")),
                Arguments.of(of("e4"), of("d5")),
                Arguments.of(of("e4"), of("f5")),
                Arguments.of(of("e4"), of("f3")),
                Arguments.of(of("e4"), of("d3"))
        );
    }

    @ParameterizedTest
    @MethodSource("getMovesWithPiecesOnPath")
    @DisplayName("Target is on a diagonal but there is a piece on it's path")
    void testWithPieceOnPath(Coordinates from, Coordinates to, Coordinates pieceOnPath) {

        Board board = boardFactory.getClearBoard();

        setBishop(board, from);
        setOpponentsPiece(board, to);
        setOpponentsPiece(board, pieceOnPath);

        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> engine.performMove(board, new Move(Color.WHITE, from, to))
        );

        assertThat(thrown.getMessage(), containsString("Bishop can't reach square at " + to + " since there is a piece in between at " + pieceOnPath));
    }

    /*
     * The data has format:
     * 1. From square
     * 2. To Square
     * 3. Piece on the path
     */
    private static Stream<Arguments> getMovesWithPiecesOnPath() {
        return Stream.of(
                Arguments.of(of("e4"), of("h7"), of("f5")),
                Arguments.of(of("e4"), of("h1"), of("g2")),
                Arguments.of(of("e4"), of("a8"), of("c6")),
                Arguments.of(of("e4"), of("b1"), of("c2"))
        );
    }

    private void validatePieceCaptured(Coordinates to, Board afterMove) {
        assertEquals(PieceName.BISHOP, afterMove.getSquare(to).getPiece().getName());

        List<Square> whitePieces = afterMove.getSquaresWithPieces(Color.WHITE);
        assertThat(whitePieces, hasSize(1));

        List<Square> blackPieces = afterMove.getSquaresWithPieces(Color.BLACK);
        assertThat(blackPieces, hasSize(0));
    }

    private void setOpponentsPiece(Board board, Coordinates coordinates) {
        Square squareWithBlackKnight = board.getSquare(coordinates);
        squareWithBlackKnight.setPiece(new Piece(PieceName.KNIGHT, Color.BLACK));
    }

    private void setBishop(Board board, Coordinates coordinates) {
        Square squareWithWhiteBishop = board.getSquare(coordinates);
        squareWithWhiteBishop.setPiece(new Piece(PieceName.BISHOP, Color.WHITE));
    }

}
