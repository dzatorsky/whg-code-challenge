package com.whg.chess.view.cmd;

import com.whg.chess.engine.GameEngine;
import com.whg.chess.engine.factory.BoardFactory;
import com.whg.chess.engine.rule.exceptions.ChessRuleException;
import com.whg.chess.engine.rule.helper.KingUtils;
import com.whg.chess.input.MoveReaderAdapter;
import com.whg.chess.input.exception.ReaderException;
import com.whg.chess.model.Board;
import com.whg.chess.model.Move;
import com.whg.chess.model.enums.Color;
import com.whg.chess.view.BoardRenderer;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class ConsoleRunner implements CommandLineRunner {

    private final KingUtils kingUtils;
    private final GameEngine gameEngine;
    private final BoardFactory boardFactory;
    private final BoardRenderer boardRenderer;
    private final MoveReaderAdapter moveReaderAdapter;

    @Override
    public void run(String... args) {

        if (args.length == 0 || StringUtils.isBlank(args[0])) {
            System.out.println("Make sure the path is provided as a parameter!");
            System.exit(1);
        }

        List<Move> moves = readMoves(args);

        Board board = boardFactory.getStartingBoard();

        boardRenderer.renderBoard(board);

        performMoves(moves, board);
    }

    private List<Move> readMoves(String[] args) {
        try {
            return moveReaderAdapter.readMoves(args[0]);
        } catch (ReaderException exception) {
            System.out.println("Could not find file with moves via path: " + args[0]);
            System.exit(1);
            return null;
        }
    }

    private void performMoves(List<Move> moves, Board board) {
        for (Move move : moves) {
            board = performMove(board, move);

            System.out.println(move);
            boardRenderer.renderBoard(board);

            warnIfKingInCheck(board, move);
        }
    }

    private Board performMove(Board board, Move move) {
        try {
            board = gameEngine.performMove(board, move);
        } catch (ChessRuleException e) {
            System.out.println("Illegal move: " + move);
            System.out.println(e.getMessage());
            System.exit(0);
        }
        return board;
    }

    private void warnIfKingInCheck(Board board, Move move) {
        Color opponentColor = move.getColor().getOpposite();
        if (kingUtils.isKingUnderAttack(board, opponentColor)) {
            System.out.println();
            System.out.println("Warning: " + opponentColor + " King is in check!");
            System.out.println();
        }
    }

}
