package com.whg.chess;

import com.whg.chess.engine.GameEngine;
import com.whg.chess.engine.factory.BoardFactory;
import com.whg.chess.view.BoardRenderer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class ChessApplication implements CommandLineRunner {

    private final BoardRenderer boardRenderer;
    private final GameEngine gameEngine;
    private final BoardFactory boardFactory;

    public static void main(String[] args) {
        SpringApplication.run(ChessApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("EXECUTING : command line runner");

//        Board board = positionFactory.getInitialPosition();
//
//        Board afterMove1 = gameEngine.performMove(board, new Move(Color.WHITE, Coordinates.of("e2"), Coordinates.of("e4")));
//
//        Board afterMove2 = gameEngine.performMove(afterMove1, new Move(Color.BLACK, Coordinates.of("e7"), Coordinates.of("e5")));
//
//        boardRenderer.renderBoard(afterMove2);
    }

}
