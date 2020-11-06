package com.whg.chess.engine;

import com.whg.chess.engine.factory.BoardFactory;
import com.whg.chess.engine.rule.exceptions.ChessRuleException;
import com.whg.chess.model.*;
import com.whg.chess.model.enums.Color;
import com.whg.chess.model.enums.PieceName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.whg.chess.model.Coordinates.of;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Requirement #5.5: The knight can move in an L shape with sides of 2 and 1 squares respectively. " +
        "That is 8 different possible moves.   Unlike other pieces it jumps over other pieces.")
class KnightRulesTest {

    @Autowired
    private GameEngine engine;

    @Autowired
    private BoardFactory boardFactory;

    @ParameterizedTest
    @CsvSource({
            "E4,F6, Knight goes north then east",
            "E4,G5, Knight goes east then north",

            "E4,F2, The knight goes south then east",
            "E4,G3, The knight goes east then south",

            "E4,D6, The knight goes north then west",
            "E4,C5, The knight goes west then north",

            "E4,D2, The knight goes south then west",
            "E4,C3, The knight goes west then south",
    })
    @DisplayName("Knight must be able to go L path")
    void testValidMoves(Coordinates from, Coordinates to, String comment) {
        Board board = boardFactory.getClearBoard();

        setKnight(board, from);
        setOpponentsPiece(board, to);

        Board afterMove = engine.performMove(board, new Move(Color.WHITE, from, to));

        validatePieceCaptured(to, afterMove);
    }

    @Test
    @DisplayName("Target is not on L path")
    void testTargetNotReachable() {
        Board board = boardFactory.getClearBoard();

        Coordinates from = of("e4");
        Coordinates to = of("f3");

        setKnight(board, from);
        setOpponentsPiece(board, to);

        ChessRuleException thrown = assertThrows(
                ChessRuleException.class,
                () -> engine.performMove(board, new Move(Color.WHITE, from, to))
        );

        assertThat(thrown.getMessage(), containsString("The knight at " + from + " can't move to " + to + " since the target is not reachable via L path"));
    }

    private void validatePieceCaptured(Coordinates to, Board afterMove) {
        assertEquals(PieceName.KNIGHT, afterMove.getSquare(to).getPiece().getName());

        List<Square> whitePieces = afterMove.getSquaresWithPieces(Color.WHITE);
        assertThat(whitePieces, hasSize(1));

        List<Square> blackPieces = afterMove.getSquaresWithPieces(Color.BLACK);
        assertThat(blackPieces, hasSize(0));
    }

    private void setOpponentsPiece(Board board, Coordinates coordinates) {
        Square squareWithBlackKnight = board.getSquare(coordinates);
        squareWithBlackKnight.setPiece(new Piece(PieceName.KNIGHT, Color.BLACK));
    }

    private void setKnight(Board board, Coordinates coordinates) {
        Square squareWithWhiteRook = board.getSquare(coordinates);
        squareWithWhiteRook.setPiece(new Piece(PieceName.KNIGHT, Color.WHITE));
    }

}
