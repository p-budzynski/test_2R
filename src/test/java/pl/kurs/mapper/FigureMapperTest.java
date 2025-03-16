package pl.kurs.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kurs.dto.CircleDto;
import pl.kurs.dto.FigureDto;
import pl.kurs.dto.RectangleDto;
import pl.kurs.dto.SquareDto;
import pl.kurs.entity.Circle;
import pl.kurs.entity.Figure;
import pl.kurs.entity.Rectangle;
import pl.kurs.entity.Square;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class FigureMapperTest {

    @InjectMocks
    private FigureMapper figureMapper;

    @Test
    void shouldMapCircleDtoToCircleEntity() {
        //given
        CircleDto circleDto = new CircleDto(5.0);

        //when
        Figure result = figureMapper.toEntities(List.of(circleDto)).getFirst();

        //then
        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(Circle.class);
        assertThat(result.getName().toUpperCase()).isEqualTo("CIRCLE");
        assertThat(((Circle) result).getRadius()).isEqualTo(5.0);
    }

    @Test
    void shouldMapSquareDtoToSquareEntity() {
        //given
        SquareDto squareDto = new SquareDto(10.0);

        //when
        Figure result = figureMapper.toEntities(List.of(squareDto)).getFirst();

        //then
        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(Square.class);
        assertThat(result.getName().toUpperCase()).isEqualTo("SQUARE");
        assertThat(((Square) result).getSide()).isEqualTo(10.0);
    }

    @Test
    void shouldMapRectangleDtoToRectangleEntity() {
        //given
        RectangleDto rectangleDto = new RectangleDto(10.0, 20.0);

        //when
        Figure result = figureMapper.toEntities(List.of(rectangleDto)).getFirst();

        //then
        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(Rectangle.class);
        assertThat(result.getName().toUpperCase()).isEqualTo("RECTANGLE");
        assertThat(((Rectangle) result).getWidth()).isEqualTo(10.0);
        assertThat(((Rectangle) result).getHeight()).isEqualTo(20.0);
    }

    @Test
    void shouldReturnEmptyListWhenEmptyListProvided() {
        //when
        List<Figure> result = figureMapper.toEntities(Collections.emptyList());

        //then
        assertThat(result).isEmpty();
    }

    @Test
    void shouldFilterOutNullsWhenMappingListOfDtosToEntities() {
        //given
        CircleDto circleDto = new CircleDto(5.0);
        FigureDto invalidDto = new FigureDto("INVALID_TYPE") {
        };
        SquareDto squareDto = new SquareDto(10.0);

        //when
        List<Figure> result = figureMapper.toEntities(Arrays.asList(circleDto, invalidDto, squareDto));

        //then
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isInstanceOf(Circle.class);
        assertThat(result.get(1)).isInstanceOf(Square.class);
    }


    @Test
    void shouldMapCircleEntityToCircleDto() {
        //given
        Circle circle = new Circle(5.0);

        //when
        FigureDto result = figureMapper.toDto(circle);

        //then
        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(CircleDto.class);
        assertThat(result.getName().toUpperCase()).isEqualTo("CIRCLE");
        assertThat(((CircleDto) result).getRadius()).isEqualTo(5.0);
    }


    @Test
    void shouldMapSquareEntityToSquareDto() {
        //given
        Square square = new Square(10.0);

        //when
        FigureDto result = figureMapper.toDto(square);

        //then
        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(SquareDto.class);
        assertThat(result.getName().toUpperCase()).isEqualTo("SQUARE");
        assertThat(((SquareDto) result).getSide()).isEqualTo(10.0);
    }

    @Test
    void shouldMapRectangleEntityToRectangleDto() {
        //given
        Rectangle rectangle = new Rectangle(10.0, 20.0);

        //when
        FigureDto result = figureMapper.toDto(rectangle);

        //then
        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(RectangleDto.class);
        assertThat(result.getName().toUpperCase()).isEqualTo("RECTANGLE");
        assertThat(((RectangleDto) result).getWidth()).isEqualTo(10.0);
        assertThat(((RectangleDto) result).getHeight()).isEqualTo(20.0);
    }

    @Test
    void shouldReturnNullWhenInvalidFigureTypeProvided() {
        //given
        Figure invalidFigure = new Figure("INVALID_TYPE") {
            @Override
            public String getName() {
                return "INVALID_TYPE";
            }
        };

        //when
        FigureDto result = figureMapper.toDto(invalidFigure);

        // then
        assertThat(result).isNull();
    }
}

