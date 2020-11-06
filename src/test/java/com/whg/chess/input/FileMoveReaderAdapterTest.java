package com.whg.chess.input;

import com.whg.chess.input.exception.ReaderException;
import com.whg.chess.input.impl.FileMoveReaderAdapter;
import com.whg.chess.model.Move;
import com.whg.chess.model.enums.Color;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import static com.whg.chess.model.Coordinates.of;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Tests for the file reader")
class FileMoveReaderAdapterTest {

    @TempDir
    Path tempDir;

    @Autowired
    FileMoveReaderAdapter moveReaderAdapter;

    @Test
    @DisplayName("Test reading the file")
    void testFileInputAdapter() throws Exception {
        String fileName = "sample-moves.txt";

        String absolutePath = copyTestFileToTmpDir(fileName);

        List<Move> moves = moveReaderAdapter.readMoves(absolutePath);

        Assertions.assertThat(moves)
                .containsExactly(
                        new Move(Color.WHITE, of("e2"), of("e4")),
                        new Move(Color.BLACK, of("e7"), of("e5"))
                );
    }

    @Test
    @DisplayName("Test reading the file when file not found")
    void testFileInputAdapterWhenFileNotFound() throws Exception {
        assertThrows(ReaderException.class, () -> moveReaderAdapter.readMoves("non-existent-file-path"));
    }

    private String copyTestFileToTmpDir(String fileName) throws IOException {
        String path = tempDir.toString() + "/" + fileName;

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
             FileOutputStream output = new FileOutputStream(path)) {
            IOUtils.copy(inputStream, output);
        }

        return path;
    }


}
