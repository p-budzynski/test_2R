package pl.kurs.dto;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
public class RectangleDto extends FigureDto {
    private double width;
    private double height;

    public RectangleDto(double width, double height) {
        super("RECTANGLE");
        this.width = width;
        this.height = height;
    }

}
