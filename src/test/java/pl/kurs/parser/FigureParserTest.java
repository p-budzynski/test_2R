package pl.kurs.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kurs.entity.Circle;
import pl.kurs.entity.Figure;
import pl.kurs.entity.Rectangle;
import pl.kurs.entity.Square;
import pl.kurs.validator.FigureValidator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FigureParserTest {

    @Mock
    private FigureValidator figureValidatorMock;

    @InjectMocks
    private FigureParser figureParser;

    @Test
    void shouldParseCircleFromFile() throws IOException {
        //given
        String fileContent = "CIRCLE;7";
        Path tempFile = Files.createTempFile("figures", ".txt");
        Files.write(tempFile, fileContent.getBytes());
        File file = tempFile.toFile();

        when(figureValidatorMock.isValid(anyString(), anyInt())).thenReturn(true);

        //when
        List<Figure> result = figureParser.parseFiguresFromFile(file);

        //then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isInstanceOf(Circle.class);
        assertThat(result.getFirst().getName().toUpperCase()).isEqualTo("CIRCLE");
        assertThat(((Circle) result.getFirst()).getRadius()).isEqualTo(7);
    }


    @Test
    void shouldParseSquareFromFile() throws IOException {
        //given

        String fileContent = "SQUARE;10";
        Path tempFile = Files.createTempFile("figures", ".txt");
        Files.write(tempFile, fileContent.getBytes());
        File file = tempFile.toFile();

        when(figureValidatorMock.isValid(anyString(), anyInt())).thenReturn(true);

        //when
        List<Figure> result = figureParser.parseFiguresFromFile(file);

        //then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isInstanceOf(Square.class);
        assertThat(result.getFirst().getName()).isEqualToIgnoringCase("SQUARE");
        assertThat(((Square) result.getFirst()).getSide()).isEqualTo(10);
    }

    @Test
    void shouldParseRectangleFromFile() throws IOException {
        //given
        String fileContent = "RECTANGLE;15;20";
        Path tempFile = Files.createTempFile("figures", ".txt");
        Files.write(tempFile, fileContent.getBytes());
        File file = tempFile.toFile();

        when(figureValidatorMock.isValid(anyString(), anyInt())).thenReturn(true);

        //when
        List<Figure> result = figureParser.parseFiguresFromFile(file);

        //then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isInstanceOf(Rectangle.class);
        assertThat(result.getFirst().getName()).isEqualToIgnoringCase("RECTANGLE");
        assertThat(((Rectangle) result.getFirst()).getWidth()).isEqualTo(15);
        assertThat(((Rectangle) result.getFirst()).getHeight()).isEqualTo(20);
    }

    @Test
    void shouldParseMultipleFiguresFromFile() throws IOException {
        //given
        String fileContent = "SQUARE;10.0\nRECTANGLE;15.0;20.0\nCIRCLE;7.5";
        Path tempFile = Files.createTempFile("figures", ".txt");
        Files.write(tempFile, fileContent.getBytes());
        File file = tempFile.toFile();

        when(figureValidatorMock.isValid(anyString(), anyInt())).thenReturn(true);

        //when
        List<Figure> result = figureParser.parseFiguresFromFile(file);

        //then
        assertThat(result).hasSize(3);
        assertThat(result.get(0)).isInstanceOf(Square.class);
        assertThat(result.get(1)).isInstanceOf(Rectangle.class);
        assertThat(result.get(2)).isInstanceOf(Circle.class);
    }

    @Test
    void shouldSkipInvalidFiguresFromFile() throws IOException {
        //given
        String fileContent = "SQUARE;10\nINVALID_FIGURE;15\nCIRCLE;7";
        Path tempFile = Files.createTempFile("figures", ".txt");
        Files.write(tempFile, fileContent.getBytes());
        File file = tempFile.toFile();

        when(figureValidatorMock.isValid("SQUARE;10", 1)).thenReturn(true);
        when(figureValidatorMock.isValid("INVALID_FIGURE;15", 2)).thenReturn(false);
        when(figureValidatorMock.isValid("CIRCLE;7", 3)).thenReturn(true);

        //when
        List<Figure> result = figureParser.parseFiguresFromFile(file);

        //then
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isInstanceOf(Square.class);
        assertThat(result.get(1)).isInstanceOf(Circle.class);
    }

    @Test
    void shouldSkipEmptyLinesFromFile() throws IOException {
        //given
        String fileContent = "SQUARE;10\n\nCIRCLE;7";
        Path tempFile = Files.createTempFile("figures", ".txt");
        Files.write(tempFile, fileContent.getBytes());
        File file = tempFile.toFile();

        when(figureValidatorMock.isValid(anyString(), anyInt())).thenReturn(true);

        //when
        List<Figure> result = figureParser.parseFiguresFromFile(file);

        //then
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isInstanceOf(Square.class);
        assertThat(result.get(1)).isInstanceOf(Circle.class);
    }

    @Test
    void shouldHandleEmptyFile() throws IOException {
        //given
        String fileContent = "";
        Path tempFile = Files.createTempFile("figures", ".txt");
        Files.write(tempFile, fileContent.getBytes());
        File file = tempFile.toFile();

        //when
        List<Figure> result = figureParser.parseFiguresFromFile(file);

        //then
        assertThat(result).isEmpty();
        verify(figureValidatorMock, never()).isValid(anyString(), anyInt());
    }

    @Test
    void shouldSkipLineWithInvalidValidator() throws IOException {
        //given
        String fileContent = "SQUARE;10";
        Path tempFile = Files.createTempFile("figures", ".txt");
        Files.write(tempFile, fileContent.getBytes());
        File file = tempFile.toFile();

        when(figureValidatorMock.isValid(anyString(), anyInt())).thenReturn(false);

        //when
        List<Figure> result = figureParser.parseFiguresFromFile(file);

        //then
        assertThat(result).isEmpty();
        verify(figureValidatorMock, times(1)).isValid(anyString(), anyInt());
    }

    @Test
    void shouldReturnNullForUnknownFigureType() throws IOException {
        //given
        String fileContent = "UNKNOWN;10";
        Path tempFile = Files.createTempFile("figures", ".txt");
        Files.write(tempFile, fileContent.getBytes());
        File file = tempFile.toFile();

        when(figureValidatorMock.isValid(anyString(), anyInt())).thenReturn(true);

        //when
        List<Figure> result = figureParser.parseFiguresFromFile(file);

        //then
        assertThat(result).isEmpty();
    }

}
