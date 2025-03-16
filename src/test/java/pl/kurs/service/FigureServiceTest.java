package pl.kurs.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.dto.CircleDto;
import pl.kurs.dto.FigureDto;
import pl.kurs.dto.RectangleDto;
import pl.kurs.dto.SquareDto;
import pl.kurs.entity.Circle;
import pl.kurs.entity.Figure;
import pl.kurs.entity.Rectangle;
import pl.kurs.entity.Square;
import pl.kurs.mapper.FigureMapper;
import pl.kurs.parser.FigureParser;
import pl.kurs.repository.FigureRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FigureServiceTest {

    @Mock
    private FigureRepository figureRepositoryMock;

    @Mock
    private FigureMapper figureMapperMock;

    @Mock
    private FigureParser figureParserMock;

    @InjectMocks
    private FigureService figureService;

    @Test
    void shouldParseFileAndSaveFiguresForUploadFigures() throws IOException {
        //given
        MultipartFile file = mock(MultipartFile.class);
        List<FigureDto> figureDtos = List.of(
                new CircleDto(7),
                new SquareDto( 4),
                new RectangleDto(3, 5)
        );

        List<Figure> figures = Arrays.asList(
                new Circle(7.0),
                new Square(4.0),
                new Rectangle(3.0,5.0)
        );

        when(figureParserMock.parseFiguresFromFile(file)).thenReturn(figureDtos);
        when(figureMapperMock.toEntities(figureDtos)).thenReturn(figures);

        //when
        Integer result = figureService.uploadFigures(file);

        //then
        assertThat(result).isEqualTo(3);
        verify(figureParserMock).parseFiguresFromFile(file);
        verify(figureMapperMock).toEntities(figureDtos);
        verify(figureRepositoryMock).saveAll(figures);
    }

    @Test
    void shouldReturnFigureWithLargestAreaAsDto() {
        //given
        Figure largestFigure = new Circle(5.0);
        FigureDto largestFigureDto = new CircleDto(5);

        when(figureRepositoryMock.findFiguresWithLargestArea()).thenReturn(largestFigure);
        when(figureMapperMock.toDto(largestFigure)).thenReturn(largestFigureDto);

        //when
        FigureDto result = figureService.findFigureWithLargestArea();

        //then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(largestFigureDto);
        verify(figureRepositoryMock).findFiguresWithLargestArea();
        verify(figureMapperMock).toDto(largestFigure);
    }
}
