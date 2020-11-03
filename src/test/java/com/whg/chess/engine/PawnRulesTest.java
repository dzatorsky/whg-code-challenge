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
@DisplayName("Requirement #5.6, #5.7, #5.8: Pawn piece rules")
class PawnRulesTest {

    @Autowired
    private GameEngine engine;

    @Autowired
    private BoardFactory boardFactory;

    @ParameterizedTest
    @CsvSource({
            "WHITE,E4,F5, White pawn capturing a piece on north-east",
            "WHITE,E4,D5, White pawn capturing a piece on north-west",
            "BLACK,E4,F3, Black pawn capturing a piece on south-east",
            "BLACK,E4,D3, Black pawn capturing a piece on south-west",
    })
    @DisplayName("Requirement #5.8: The pawn can piece one square forward diagonally if taking an opponent piece")
    void testValidCaptures(Color color, String from, String to, String comment) {
        Board board = boardFactory.getClearBoard();

        setPawn(board, from, color);
        setOpponentsPiece(board, to, color);

        Board afterMove = engine.performMove(board, new Move(color, Coordinates.of(from), Coordinates.of(to)));

        validatePieceCaptured(to, afterMove, color);
    }

    @ParameterizedTest
    @CsvSource({
            "BLACK,E4,F5, Black pawn capturing a piece on north-east (wrong direction)",
            "BLACK,E4,D5, Black pawn capturing a piece on north-west (wrong direction)",
            "WHITE,E4,F3, White pawn capturing a piece on south-east (wrong direction)",
            "WHITE,E4,D3, White pawn capturing a piece on south-west (wrong direction)",
            "WHITE,E4,F6, White pawn capturing a piece which is too far horizontally",
            "WHITE,E4,G5, White pawn capturing a piece which is too far vertically",
            "WHITE,E4,G6, White pawn capturing a piece which is too far vertically and horizontally but on the same diagonal",
    })
    @DisplayName("Test invalid pawn captures")
    void testInvalidCaptures(Color color, String from, String to, String comment) {
        Board board = boardFactory.getClearBoard();

        setPawn(board, from, color);
        setOpponentsPiece(board, to, color);

        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> engine.performMove(board, new Move(color, Coordinates.of(from), Coordinates.of(to)))
        );

        assertThat(thrown.getMessage(), containsString("Pawn located at " + from + " can't capture a piece at " + to + " because it's not located 1 square diagonally to the pawn's direction"));
    }

    @ParameterizedTest
    @CsvSource({
            "WHITE,E2,E3, Normal 1 square piece ahead",
            "WHITE,E2,E4, 2 square piece ahead from starting postmove",
            "BLACK,E7,E6, Normal 1 square piece ahead",
            "BLACK,E7,E5, 2 square piece ahead from starting postmove",
    })
    @DisplayName("Requirement #5.6: The pawn can piece one or two squares forward on its first piece (when not taking an opponent piece)")
    void testValidMoves(Color color, String from, String to, String comment) {
        Board board = boardFactory.getClearBoard();

        setPawn(board, from, color);

        Board afterMove = engine.performMove(board, new Move(color, Coordinates.of(from), Coordinates.of(to)));

        validatePieceCaptured(to, afterMove, color);
    }

    @ParameterizedTest
    @CsvSource({
            "WHITE,E4,E3, WHITE pawn on E4 is not allowed to piece to E3",
            "WHITE,E4,E6, WHITE pawn on E4 is not allowed to piece to E6",
            "WHITE,E4,E2, WHITE pawn on E4 is not allowed to piece to E2",
            "WHITE,E4,D4, Pawn at E4 can be moved vertically only.",
            "WHITE,E4,F4, Pawn at E4 can be moved vertically only.",
            "WHITE,E4,F5, Pawn at E4 can be moved vertically only.",
            "WHITE,E4,D5, Pawn at E4 can be moved vertically only.",
            "WHITE,E4,D3, Pawn at E4 can be moved vertically only.",
            "WHITE,E4,F3, Pawn at E4 can be moved vertically only.",

            // Same for Black

            "BLACK,E4,E5, BLACK pawn on E4 is not allowed to piece to E5",
            "BLACK,E4,E2, BLACK pawn on E4 is not allowed to piece to E2",
            "BLACK,E4,E6, BLACK pawn on E4 is not allowed to piece to E6",
            "BLACK,E4,D4, Pawn at E4 can be moved vertically only.",
            "BLACK,E4,F4, Pawn at E4 can be moved vertically only.",
            "BLACK,E4,F5, Pawn at E4 can be moved vertically only.",
            "BLACK,E4,D5, Pawn at E4 can be moved vertically only.",
            "BLACK,E4,D3, Pawn at E4 can be moved vertically only.",
            "BLACK,E4,F3, Pawn at E4 can be moved vertically only.",
    })
    @DisplayName("Test invalid pawn moves")
    void testInvalidMoves(Color color, String from, String to, String expectedError) {
        Board board = boardFactory.getClearBoard();

        setPawn(board, from, color);

        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> engine.performMove(board, new Move(color, Coordinates.of(from), Coordinates.of(to)))
        );

        assertThat(thrown.getMessage(), containsString(expectedError));
    }

    @ParameterizedTest
    @CsvSource({
            "WHITE,E2,E4,E3, White pawn moving 2 squares ahead when blocked",
            "BLACK,E7,E5,E6, Black pawn moving 2 squares ahead when blocked"
    })
    @DisplayName("Test 2 square piece from initial postmove when blocked")
    void test2SquareMoveWhenBlocked(Color color, String from, String to, String blockingPieceLocation, String comment) {
        Board board = boardFactory.getClearBoard();

        setPawn(board, from, color);
        setOpponentsPiece(board, blockingPieceLocation, color);

        ValidationException thrown = assertThrows(
                ValidationException.class,
                () -> engine.performMove(board, new Move(color, Coordinates.of(from), Coordinates.of(to)))
        );

        assertThat(thrown.getMessage(), containsString(to + " can't be reached since there is a piece at " + blockingPieceLocation + " on the path"));
    }

    private void validatePieceCaptured(String to, Board afterMove, Color color) {
        assertEquals(PieceName.PAWN, afterMove.getSquare(Coordinates.of(to)).getPiece().getName());

        List<Square> whitePieces = afterMove.getSquaresWithPieces(color);
        assertThat(whitePieces, hasSize(1));

        List<Square> blackPieces = afterMove.getSquaresWithPieces(color.getOpposite());
        assertThat(blackPieces, hasSize(0));
    }

    private void setOpponentsPiece(Board board, String coordinates, Color color) {
        Square squareWithBlackKnight = board.getSquare(Coordinates.of(coordinates));
        squareWithBlackKnight.setPiece(new Piece(PieceName.KNIGHT, color.getOpposite()));
    }

    private void setPawn(Board board, String coordinates, Color color) {
        Square squareWithWhiteBishop = board.getSquare(Coordinates.of(coordinates));
        squareWithWhiteBishop.setPiece(new Piece(PieceName.PAWN, color));
    }

}
