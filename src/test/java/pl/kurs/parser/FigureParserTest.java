package pl.kurs.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.dto.CircleDto;
import pl.kurs.dto.FigureDto;
import pl.kurs.dto.RectangleDto;
import pl.kurs.dto.SquareDto;
import pl.kurs.validator.FigureValidator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
        MultipartFile file = new MockMultipartFile("file", "test.txt",
                "text/plain", fileContent.getBytes(StandardCharsets.UTF_8));

        when(figureValidatorMock.isValid(anyString(), anyInt())).thenReturn(true);

        //when
        List<FigureDto> result = figureParser.parseFiguresFromFile(file);

        //then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isInstanceOf(CircleDto.class);
        assertThat(result.getFirst().getName().toUpperCase()).isEqualTo("CIRCLE");
        assertThat(((CircleDto) result.getFirst()).getRadius()).isEqualTo(7);
    }


    @Test
    void shouldParseSquareFromFile() throws IOException {
        //given

        String fileContent = "SQUARE;10";
        MultipartFile file = new MockMultipartFile("file", "test.txt",
                "text/plain", fileContent.getBytes(StandardCharsets.UTF_8));

        when(figureValidatorMock.isValid(anyString(), anyInt())).thenReturn(true);

        //when
        List<FigureDto> result = figureParser.parseFiguresFromFile(file);

        //then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isInstanceOf(SquareDto.class);
        assertThat(result.getFirst().getName()).isEqualToIgnoringCase("SQUARE");
        assertThat(((SquareDto) result.getFirst()).getSide()).isEqualTo(10);
    }

    @Test
    void shouldParseRectangleFromFile() throws IOException {
        //given
        String fileContent = "RECTANGLE;15;20";
        MultipartFile file = new MockMultipartFile("file", "test.txt",
                "text/plain", fileContent.getBytes(StandardCharsets.UTF_8));

        when(figureValidatorMock.isValid(anyString(), anyInt())).thenReturn(true);

        //when
        List<FigureDto> result = figureParser.parseFiguresFromFile(file);

        //then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isInstanceOf(RectangleDto.class);
        assertThat(result.getFirst().getName()).isEqualToIgnoringCase("RECTANGLE");
        assertThat(((RectangleDto) result.getFirst()).getWidth()).isEqualTo(15);
        assertThat(((RectangleDto) result.getFirst()).getHeight()).isEqualTo(20);
    }

    @Test
    void shouldParseMultipleFiguresFromFile() throws IOException {
        //given
        String fileContent = "SQUARE;10.0\nRECTANGLE;15.0;20.0\nCIRCLE;7.5";
        MultipartFile file = new MockMultipartFile("file", "test.txt",
                "text/plain", fileContent.getBytes(StandardCharsets.UTF_8));

        when(figureValidatorMock.isValid(anyString(), anyInt())).thenReturn(true);

        //when
        List<FigureDto> result = figureParser.parseFiguresFromFile(file);

        //then
        assertThat(result).hasSize(3);
        assertThat(result.get(0)).isInstanceOf(SquareDto.class);
        assertThat(result.get(1)).isInstanceOf(RectangleDto.class);
        assertThat(result.get(2)).isInstanceOf(CircleDto.class);
    }

    @Test
    void shouldSkipInvalidFiguresFromFile() throws IOException {
        //given
        String fileContent = "SQUARE;10\nINVALID;15\nCIRCLE;7";
        MultipartFile file = new MockMultipartFile("file", "test.txt",
                "text/plain", fileContent.getBytes(StandardCharsets.UTF_8));

        when(figureValidatorMock.isValid("SQUARE;10", 1)).thenReturn(true);
        when(figureValidatorMock.isValid("INVALID;15", 2)).thenReturn(false);
        when(figureValidatorMock.isValid("CIRCLE;7", 3)).thenReturn(true);

        //when
        List<FigureDto> result = figureParser.parseFiguresFromFile(file);

        //then
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isInstanceOf(SquareDto.class);
        assertThat(result.get(1)).isInstanceOf(CircleDto.class);
    }

    @Test
    void shouldSkipEmptyLinesFromFile() throws IOException {
        //given
        String fileContent = "SQUARE;10\n\nCIRCLE;7";
        MultipartFile file = new MockMultipartFile("file", "test.txt",
                "text/plain", fileContent.getBytes(StandardCharsets.UTF_8));

        when(figureValidatorMock.isValid(anyString(), anyInt())).thenReturn(true);

        //when
        List<FigureDto> result = figureParser.parseFiguresFromFile(file);

        //then
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isInstanceOf(SquareDto.class);
        assertThat(result.get(1)).isInstanceOf(CircleDto.class);
    }

    @Test
    void shouldHandleEmptyFile() throws IOException {
        //given
        String fileContent = "";
        MultipartFile file = new MockMultipartFile("file", "test.txt",
                "text/plain", fileContent.getBytes(StandardCharsets.UTF_8));

        //when
        List<FigureDto> result = figureParser.parseFiguresFromFile(file);

        //then
        assertThat(result).isEmpty();
        verify(figureValidatorMock, never()).isValid(anyString(), anyInt());
    }

    @Test
    void shouldSkipLineWithInvalidValidator() throws IOException {
        //given
        String fileContent = "SQUARE;10";
        MultipartFile file = new MockMultipartFile("file", "test.txt",
                "text/plain", fileContent.getBytes(StandardCharsets.UTF_8));

        when(figureValidatorMock.isValid(anyString(), anyInt())).thenReturn(false);

        //when
        List<FigureDto> result = figureParser.parseFiguresFromFile(file);

        //then
        assertThat(result).isEmpty();
        verify(figureValidatorMock, times(1)).isValid(anyString(), anyInt());
    }

    @Test
    void shouldReturnNullForUnknownFigureType() throws IOException {
        //given
        String fileContent = "UNKNOWN;10";
        MultipartFile file = new MockMultipartFile("file", "test.txt",
                "text/plain", fileContent.getBytes(StandardCharsets.UTF_8));

        when(figureValidatorMock.isValid(anyString(), anyInt())).thenReturn(true);

        //when
        List<FigureDto> result = figureParser.parseFiguresFromFile(file);

        //then
        assertThat(result).isEmpty();
    }

}
