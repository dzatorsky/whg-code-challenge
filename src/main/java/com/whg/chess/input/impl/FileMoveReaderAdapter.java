package com.whg.chess.input.impl;

import com.google.common.collect.Streams;
import com.whg.chess.config.AppConfig;
import com.whg.chess.input.MoveReaderAdapter;
import com.whg.chess.input.exception.ReaderException;
import com.whg.chess.model.Coordinates;
import com.whg.chess.model.Move;
import com.whg.chess.model.enums.Color;
import com.whitehatgaming.UserInput;
import com.whitehatgaming.UserInputFile;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class FileMoveReaderAdapter implements MoveReaderAdapter {

    private final AppConfig appConfig;

    @Override
    @SneakyThrows
    public List<Move> readMoves(String location) {

        try {
            UserInput userInput = new UserInputFile(location);

            Stream<int[]> streamOfMoves = Stream
                    .generate(() -> getNextMove(userInput))
                    .takeWhile(Objects::nonNull);

            return Streams
                    .mapWithIndex(streamOfMoves, SimpleImmutableEntry::new)
                    .map(this::convertMove)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new ReaderException("The move could not be read.", e);
        }
    }

    private Move convertMove(SimpleImmutableEntry<int[], Long> entry) {
        int[] line = entry.getKey();

        Coordinates from = new Coordinates(convertRow(line[1]), line[0]);
        Coordinates to = new Coordinates(convertRow(line[3]), line[2]);

        return new Move(getColor(entry.getValue()), from, to);
    }

    private int convertRow(int i) {
        return getLastElementIndex() - i;
    }

    private int getLastElementIndex() {
        return appConfig.getBoardSize() - 1;
    }

    private Color getColor(Long index) {
        if (index % 2 == 0) {
            return Color.WHITE;
        } else {
            return Color.BLACK;
        }
    }

    @SneakyThrows
    private int[] getNextMove(UserInput userInput) {
        return userInput.nextMove();
    }

}
