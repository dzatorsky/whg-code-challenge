package com.whg.chess.view.cmd;

import com.whg.chess.engine.GameEngine;
import com.whg.chess.engine.factory.BoardFactory;
import com.whg.chess.engine.rule.helper.KingUtils;
import com.whg.chess.input.MoveReaderAdapter;
import com.whg.chess.model.Board;
import com.whg.chess.model.Move;
import com.whg.chess.model.enums.Color;
import com.whg.chess.view.BoardRenderer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

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
        System.out.println("Please enter the path to read chess moves from: ");

        try (Scanner scanIn = new Scanner(System.in)) {
            String filePath = scanIn.nextLine();

            List<Move> moves = moveReaderAdapter.readMoves(filePath);

            Board board = boardFactory.getStartingBoard();

            boardRenderer.renderBoard(board);

            performMoves(moves, board);
        }

    }

    private void performMoves(List<Move> moves, Board board) {
        for (Move move : moves) {
            try {
                board = gameEngine.performMove(board, move);
            } catch (Exception e) {
                System.out.println("Illegal move: " + move);
                System.out.println(e.getMessage());
                break;
            }

            System.out.println(move);
            boardRenderer.renderBoard(board);

            warnIfKingInCheck(board, move);
        }
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
