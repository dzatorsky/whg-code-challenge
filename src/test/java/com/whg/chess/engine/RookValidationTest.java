package com.whg.chess.engine;

import com.whg.chess.engine.factory.BoardFactory;
import com.whg.chess.engine.validator.exceptions.ValidationException;
import com.whg.chess.model.*;
import com.whg.chess.model.enums.Color;
import com.whg.chess.model.enums.PieceName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
@DisplayName("Requirement #5.3: The rook can move any number of squares but only horizontally or vertically")
class RookValidationTest {

    @Autowired
    private GameEngine engine;

    @Autowired
    private BoardFactory boardFactory;

    @ParameterizedTest
    @MethodSource("getValidMoves")
    @DisplayName("Rook must reach targets on verticals on diagonals")
    void testValidMoves(Coordinates from, Coordinates to) {
        Board board = boardFactory.getClearBoard();

        setRook(board, from);
        setOpponentsPiece(board, to);

        Board afterMove = engine.performMove(board, new Move(Color.WHITE, from, to));

        validatePieceCaptured(to, afterMove);
    }

    private static Stream<Arguments> getValidMoves() {
        return Stream.of(
                Arguments.of(of("e4"), of("e8")),
                Arguments.of(of("e4"), of("e1")),
                Arguments.of(of("e4"), of("a4")),
                Arguments.of(of("e4"), of("h4")),
                Arguments.of(of("e4"), of("e5")),
                Arguments.of(of("e4"), of("e3")),
                Arguments.of(of("e4"), of("f4")),
                Arguments.of(of("e4"), of("d4"))
        );
    }

    @ParameterizedTest
    @MethodSource("getMovesWithPiecesOnPath")
    @DisplayName("Target is at the left side but there is a piece between rook and target")
    void testPieceOnThePath(Coordinates from, Coordinates to, Coordinates pieceOnPath) {
        Board board = boardFactory.getClearBoard();

        setRook(board, from);
        setOpponentsPiece(board, to);
        setOpponentsPiece(board, pieceOnPath);

        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> engine.performMove(board, new Move(Color.WHITE, from, to))
        );

        assertThat(thrown.getMessage(), containsString("There is a piece standing on the rook path at " + pieceOnPath));
    }

    /*
     * The data has format:
     * 1. From square
     * 2. To Square
     * 3. Piece on the path
     */
    private static Stream<Arguments> getMovesWithPiecesOnPath() {
        return Stream.of(
                Arguments.of(of("e4"), of("e8"), of("e5")),
                Arguments.of(of("e4"), of("e1"), of("e2")),
                Arguments.of(of("e4"), of("a4"), of("c4")),
                Arguments.of(of("e4"), of("h4"), of("g4"))
        );
    }

    @Test
    @DisplayName("Target is neither on horizonal nor it is on vertical")
    void testTargetNotReachable() {
        Board board = boardFactory.getClearBoard();

        Coordinates from = of("e4");
        Coordinates to = of("g2");

        setRook(board, from);
        setOpponentsPiece(board, to);

        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> engine.performMove(board, new Move(Color.WHITE, from, to))
        );

        assertThat(thrown.getMessage(), containsString("Target square " + to + " can't be reached by the rook at " + from));
    }

    private void validatePieceCaptured(Coordinates to, Board afterMove) {
        assertEquals(PieceName.ROOK, afterMove.getSquare(to).getPiece().getName());

        List<Square> whitePieces = afterMove.getSquaresWithPieces(Color.WHITE);
        assertThat(whitePieces, hasSize(1));

        List<Square> blackPieces = afterMove.getSquaresWithPieces(Color.BLACK);
        assertThat(blackPieces, hasSize(0));
    }

    private void setOpponentsPiece(Board board, Coordinates coordinates) {
        Square squareWithBlackKnight = board.getSquare(coordinates);
        squareWithBlackKnight.setPiece(new Piece(PieceName.KNIGHT, Color.BLACK));
    }

    private void setRook(Board board, Coordinates coordinates) {
        Square squareWithWhiteRook = board.getSquare(coordinates);
        squareWithWhiteRook.setPiece(new Piece(PieceName.ROOK, Color.WHITE));
    }

}
