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
@DisplayName("Requirement #5.2: The bishop can piece any number of squares but only diagonally")
class BishopRulesTest {

    @Autowired
    private GameEngine engine;

    @Autowired
    private BoardFactory boardFactory;

    @ParameterizedTest
    @CsvSource({
            "E4,H7, Long north-east diagonal",
            "E4,H1, Long south-east diagonal",
            "E4,A8, Long north-west diagonal",
            "E4,B1, Long south-west diagonal",
            "E4,D5, Target is right near the piece on north-west diagonal",
            "E4,F5, Target is right near the piece on north-east diagonal",
            "E4,F3, Target is right near the piece on south-east diagonal",
            "E4,D3, Target is right near the piece on south-west diagonal"
    })
    @DisplayName("Bishop can reach target at different positions on diagonal")
    void testValidMoves(String from, String to, String comment) {
        Board board = boardFactory.getClearBoard();

        setBishop(board, from);
        setOpponentsPiece(board, to);

        Board afterMove = engine.performMove(board, new Move(Color.WHITE, Coordinates.of(from), Coordinates.of(to)));

        validatePieceCaptured(to, afterMove);
    }

    @ParameterizedTest
    @CsvSource({
            "E4,H7,F5, The piece on the path on north-east diagonal",
            "E4,H1,G2, The piece on the path on south-east diagonal",
            "E4,A8,C6, The piece on the path on north-west diagonal",
            "E4,B1,C2, The piece on the path on south-west diagonal"
    })
    @DisplayName("Requirement #8. For pieces other than the knight disallow the move if there are any other pieces in the way between the start and end square.")
    void testWithPieceOnPath(String from, String to, String pieceOnPath, String comment) {

        Board board = boardFactory.getClearBoard();

        setBishop(board, from);
        setOpponentsPiece(board, to);
        setOpponentsPiece(board, pieceOnPath);

        ChessRuleException thrown = assertThrows(
                ChessRuleException.class,
                () -> engine.performMove(board, new Move(Color.WHITE, Coordinates.of(from), Coordinates.of(to)))
        );

        assertThat(thrown.getMessage(), containsString(to + " can't be reached since there is a piece at " + pieceOnPath + " on the path"));
    }

    @Test
    @DisplayName("Target is not on Bishop's diagonal")
    void testTargetNotReachable() {
        Board board = boardFactory.getClearBoard();

        Coordinates from = of("E4");
        Coordinates to = of("E3");

        setBishop(board, from.toString());
        setOpponentsPiece(board, to.toString());

        ChessRuleException thrown = assertThrows(
                ChessRuleException.class,
                () -> engine.performMove(board, new Move(Color.WHITE, from, to))
        );

        assertThat(thrown.getMessage(), containsString("Bishop at " + from + " can't reach " + to + " since it's not on bishop's diagonal"));
    }

    private void validatePieceCaptured(String to, Board afterMove) {
        assertEquals(PieceName.BISHOP, afterMove.getSquare(Coordinates.of(to)).getPiece().getName());

        List<Square> whitePieces = afterMove.getSquaresWithPieces(Color.WHITE);
        assertThat(whitePieces, hasSize(1));

        List<Square> blackPieces = afterMove.getSquaresWithPieces(Color.BLACK);
        assertThat(blackPieces, hasSize(0));
    }

    private void setOpponentsPiece(Board board, String coordinates) {
        Square squareWithBlackKnight = board.getSquare(Coordinates.of(coordinates));
        squareWithBlackKnight.setPiece(new Piece(PieceName.KNIGHT, Color.BLACK));
    }

    private void setBishop(Board board, String coordinates) {
        Square squareWithWhiteBishop = board.getSquare(Coordinates.of(coordinates));
        squareWithWhiteBishop.setPiece(new Piece(PieceName.BISHOP, Color.WHITE));
    }

}
