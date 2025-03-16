package pl.kurs.dto;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
public class SquareDto extends FigureDto {
    private double side;

    public SquareDto(double side) {
        super("SQUARE");
        this.side = side;
    }
}
