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

import java.util.ArrayList;
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
            case "KOLO":
                CircleDto circleDto = (CircleDto) dto;
                return new Circle(circleDto.getRadius());
            case "KWADRAT":
                SquareDto squareDto = (SquareDto) dto;
                return new Square(squareDto.getSide());
            case "PROSTOKAT":
                RectangleDto rectangleDto = (RectangleDto) dto;
                return new Rectangle(rectangleDto.getWidth(), rectangleDto.getHeight());
        }
        System.err.println("Unsupported figure type: " + dto.getName());
        return null;
    }

    public List<FigureDto> toDto(List<Object[]> databaseResults) {
        List<FigureDto> figureDtoList = new ArrayList<>();

        for (Object[] row : databaseResults) {
            if (row == null || row.length < 2) {
                continue;
            }

            String type = (String) row[0];

            switch (type.toUpperCase()) {
                case "KOLO":
                    if (row[1] != null && row[1] instanceof Double && (Double) row[1] > 0) {
                        figureDtoList.add(new CircleDto((Double) row[1]));
                    }
                    break;
                case "KWADRAT":
                    if (row[1] != null && row[1] instanceof Double && (Double) row[1] > 0) {
                        figureDtoList.add(new SquareDto((Double) row[1]));
                    }
                    break;
                case "PROSTOKAT":
                    if (row[1] != null && row[1] instanceof Double && (Double) row[1] > 0 &&
                            row[2] != null && row[2] instanceof Double && (Double) row[2] > 0) {
                        figureDtoList.add(new RectangleDto((Double) row[1], (Double) row[2]));
                    }
                    break;
            }
        }

        return figureDtoList;
    }
}
