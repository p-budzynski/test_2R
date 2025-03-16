package pl.kurs.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kurs.entity.Circle;
import pl.kurs.entity.Figure;
import pl.kurs.entity.Rectangle;
import pl.kurs.entity.Square;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FigureRepositoryTest {

    @Mock
    private EntityManager entityManagerMock;

    @Mock
    private Query queryMock;

    @InjectMocks
    private FigureRepository figureRepository;

    @Captor
    private ArgumentCaptor<Figure> figureCaptor;

    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @BeforeEach
    void setup() {
        System.setErr(new PrintStream(errContent));
    }

    @Test
    void shouldSaveAllFigures() {
        //given
        List<Figure> figures = Arrays.asList(
                new Circle(5.0),
                new Square(8.0),
                new Rectangle(4.0, 10.0));

        //when
        figureRepository.saveAll(figures);

        //then
        verify(entityManagerMock, times(3)).persist(figureCaptor.capture());
        List<Figure> capturedFigures = figureCaptor.getAllValues();
        assertThat(capturedFigures).hasSize(3);
        assertThat(capturedFigures).containsExactlyInAnyOrderElementsOf(figures);
    }

    @Test
    void shouldReturnCircleWhenCircleHasLargestArea() {
        //given
        Circle expectedCircle = new Circle();
        List<Object[]> results = new ArrayList<>();
        results.add(new Object[]{"circle", 1L});

        when(entityManagerMock.createNativeQuery("SELECT typ, id FROM figure_max_area"))
                .thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(results);
        when(entityManagerMock.find(Circle.class, 1L)).thenReturn(expectedCircle);

        //when
        Figure result = figureRepository.findFiguresWithLargestArea();

        //then
        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(Circle.class);
        assertThat(result).isSameAs(expectedCircle);
        verify(entityManagerMock).find(Circle.class, 1L);
    }

    @Test
    void shouldReturnSquareWhenSquareHasLargestArea() {
        //given
        Square expectedSquare = new Square();
        List<Object[]> results = new ArrayList<>();
        results.add(new Object[]{"square", 2L});

        when(entityManagerMock.createNativeQuery("SELECT typ, id FROM figure_max_area"))
                .thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(results);
        when(entityManagerMock.find(Square.class, 2L)).thenReturn(expectedSquare);

        //when
        Figure result = figureRepository.findFiguresWithLargestArea();

        //then
        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(Square.class);
        assertThat(result).isSameAs(expectedSquare);
        verify(entityManagerMock).find(Square.class, 2L);
    }

    @Test
    void shouldReturnRectangleWhenRectangleHasLargestArea() {
        //given
        Rectangle expectedRectangle = new Rectangle();
        List<Object[]> results = new ArrayList<>();
        results.add(new Object[]{"rectangle", 3L});

        when(entityManagerMock.createNativeQuery("SELECT typ, id FROM figure_max_area")).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(results);
        when(entityManagerMock.find(Rectangle.class, 3L)).thenReturn(expectedRectangle);

        //when
        Figure result = figureRepository.findFiguresWithLargestArea();

        //then
        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(Rectangle.class);
        assertThat(result).isSameAs(expectedRectangle);
        verify(entityManagerMock).find(Rectangle.class, 3L);
    }

    @Test
    void shouldThrowExceptionForUnknownTypeForFindFiguresWithLargestArea() {
        //given
        List<Object[]> results = new ArrayList<>();
        results.add(new Object[]{"unknown", 4L});

        when(entityManagerMock.createNativeQuery("SELECT typ, id FROM figure_max_area")).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(results);

        //when then
        assertThatThrownBy(() -> figureRepository.findFiguresWithLargestArea())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Unknown figure type: unknown");
    }

    @Test
    void shouldReturnNullWhenNoFigures() {
        //given
        when(entityManagerMock.createNativeQuery("SELECT typ, id FROM figure_max_area")).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(Collections.emptyList());

        //when
        Figure result = figureRepository.findFiguresWithLargestArea();

        //then
        assertThat(result).isNull();
        assertThat(errContent.toString()).contains("No figures in database.");
    }

}
