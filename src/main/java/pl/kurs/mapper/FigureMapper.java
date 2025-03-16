package pl.kurs.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kurs.dto.CircleDto;
import pl.kurs.dto.FigureDto;
import pl.kurs.dto.RectangleDto;
import pl.kurs.dto.SquareDto;
import pl.kurs.entity.Circle;
import pl.kurs.entity.Figure;
import pl.kurs.entity.Rectangle;
import pl.kurs.entity.Square;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FigureMapper {

    public List<Figure> toEntities(List<FigureDto> figureDtoList) {
        return figureDtoList.stream()
                .map(this::toEntity)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Figure toEntity(FigureDto dto) {
        switch (dto.getName().toUpperCase()) {
            case "CIRCLE":
                CircleDto circleDto = (CircleDto) dto;
                return new Circle(circleDto.getRadius());
            case "SQUARE":
                SquareDto squareDto = (SquareDto) dto;
                return new Square(squareDto.getSide());
            case "RECTANGLE":
                RectangleDto rectangleDto = (RectangleDto) dto;
                return new Rectangle(rectangleDto.getWidth(), rectangleDto.getHeight());
        }
        System.err.println("Unsupported figure type: " + dto.getName());
        return null;
    }

    public FigureDto toDto(Figure figure) {
        switch (figure.getName().toUpperCase()) {
            case "CIRCLE":
                Circle circle = (Circle) figure;
                return new CircleDto(circle.getRadius());
            case "SQUARE":
                Square square = (Square) figure;
                return new SquareDto(square.getSide());
            case "RECTANGLE":
                Rectangle rectangle = (Rectangle) figure;
                return new RectangleDto(rectangle.getWidth(), rectangle.getHeight());
        }
        System.err.println("Unsupported figure type: " + figure.getName());
        return null;
    }
}
