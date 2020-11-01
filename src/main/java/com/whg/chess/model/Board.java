package com.whg.chess.model;

import com.whg.chess.model.enums.Color;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Board {

    private final Square[][] squares;

    public Board(Integer boardSize) {
        this.squares = new Square[boardSize][boardSize];

        initBoard(null);
    }

    public Board(Board board) {
        this.squares = new Square[board.getSize()][board.getSize()];

        initBoard(board);
    }

    public Integer getSize() {
        return squares.length;
    }

    public Square getSquare(Coordinates coordinates) {
        return squares[coordinates.getRow()][coordinates.getColumn()];
    }

    public void setSquare(Square square) {
        Coordinates coordinates = square.getCoordinates();

        squares[coordinates.getRow()][coordinates.getColumn()] = square;
    }

    public List<Square> getSquaresWithPieces(Color color) {
        return Arrays.stream(squares)
                .flatMap(Arrays::stream)
                .filter(square -> square.getPiece() != null)
                .filter(square -> square.getPiece().getColor() == color)
                .collect(Collectors.toList());
    }

    public List<Square> getRow(Integer row) {
        return List.of(getSquares()[row]);
    }

    public List<Square> getColumn(Integer column) {
        return Arrays.stream(squares)
                .flatMap(Arrays::stream)
                .filter(square -> column.equals(square.getCoordinates().getColumn()))
                .collect(Collectors.toList());
    }

    private void initBoard(Board board) {
        for (int row = 0; row < getSize(); row++) {
            for (int column = 0; column < getSize(); column++) {

                if (this.squares[row][column] == null) {
                    this.squares[row][column] = new Square(null, new Coordinates(row, column));
                }

                if (board != null) {
                    Square squareFromBoard = board.getSquare(new Coordinates(row, column));
                    this.squares[row][column].setPiece(squareFromBoard.getPiece());
                }

            }
        }
    }
}
