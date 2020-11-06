package com.whg.chess.engine;

import com.whg.chess.engine.factory.BoardFactory;
import com.whg.chess.engine.rule.exceptions.ChessRuleException;
import com.whg.chess.model.*;
import com.whg.chess.model.enums.Color;
import com.whg.chess.model.enums.PieceName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Requirement #4, #7")
class GeneralRulesTest {

    @Autowired
    private GameEngine engine;

    @Autowired
    private BoardFactory boardFactory;

    @Nested
    @DisplayName("Requirement #4: " +
            "All moves must have a piece on the starting square and either an opponent piece or nothing on the destination square. " +
            "Anything else is invalid."
    )
    class TheForthRequirementTests {
        @Test
        @DisplayName("It must not be possible to piece square without piece on it")
        void testStartingSquarePiece() {
            Board clearBoard = boardFactory.getClearBoard();

            ChessRuleException thrown = assertThrows(
                    ChessRuleException.class,
                    () -> engine.performMove(clearBoard, new Move(Color.WHITE, Coordinates.of("e5"), Coordinates.of("e6")))
            );

            assertThat(thrown.getMessage(), containsString("Piece was not found at: E5"));
        }

        @Test
        @DisplayName("It must not be possible to capture own pieces")
        void testDestinationSquareIsTheSameColor() {
            Board startingBoard = boardFactory.getStartingBoard();

            ChessRuleException thrown = assertThrows(
                    ChessRuleException.class,
                    () -> engine.performMove(startingBoard, new Move(Color.WHITE, Coordinates.of("a1"), Coordinates.of("a2")))
            );

            assertThat(thrown.getMessage(), containsString("WHITE color player is trying to capture their own piece at A2"));
        }

        @Test
        @DisplayName("Partially requirement #7: It must not be possible to capture a King")
        void testCapturingTheKing() {
            Board board = boardFactory.getClearBoard();

            Square squareWithWhiteKing = board.getSquare(Coordinates.of("a1"));
            squareWithWhiteKing.setPiece(new Piece(PieceName.KING, Color.WHITE));

            Square squareWithBlackRook = board.getSquare(Coordinates.of("a8"));
            squareWithBlackRook.setPiece(new Piece(PieceName.ROOK, Color.BLACK));

            ChessRuleException thrown = assertThrows(
                    ChessRuleException.class,
                    () -> engine.performMove(board, new Move(Color.BLACK, Coordinates.of("a8"), Coordinates.of("a1")))
            );

            assertThat(thrown.getMessage(), containsString("BLACK color player is trying to capture an opponent's king at A1"));
        }

        @Test
        @DisplayName("It must not be possible to make a piece outside of the borders")
        void testMoveOutsideBorders() {
            Board board = boardFactory.getClearBoard();

            Square squareWithBlackRook = board.getSquare(Coordinates.of("a1"));
            squareWithBlackRook.setPiece(new Piece(PieceName.ROOK, Color.WHITE));

            ChessRuleException thrown = assertThrows(
                    ChessRuleException.class,
                    () -> engine.performMove(board, new Move(Color.WHITE, Coordinates.of("a1"), Coordinates.of("a555")))
            );

            assertThat(thrown.getMessage(), containsString("The piece is illegal since it goes outside the board borders: A555"));
        }
    }

    @Nested
    @DisplayName("Requirement #7. If the destination square contains an opponent piece then that piece is removed from the board." +
            "Unless that piece is a King where rules around check apply (see later)")
    class TheSeventhRequirementTests {
        @Test
        @DisplayName("It must be possible to capture an opponent piece")
        void testCapturingOpponentsPiece() {
            Board board = boardFactory.getClearBoard();

            Square squareWithWhiteKing = board.getSquare(Coordinates.of("a1"));
            squareWithWhiteKing.setPiece(new Piece(PieceName.ROOK, Color.WHITE));

            Square squareWithBlackKnight = board.getSquare(Coordinates.of("a8"));
            squareWithBlackKnight.setPiece(new Piece(PieceName.KNIGHT, Color.BLACK));

            Board afterMove = engine.performMove(board, new Move(Color.WHITE, Coordinates.of("a1"), Coordinates.of("a8")));

            List<Square> whitePieces = afterMove.getSquaresWithPieces(Color.WHITE);
            assertThat(whitePieces, hasSize(1));

            List<Square> blackPieces = afterMove.getSquaresWithPieces(Color.BLACK);
            assertThat(blackPieces, hasSize(0));
        }
    }

}
