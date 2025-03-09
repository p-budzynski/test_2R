package pl.kurs.dto;

import lombok.*;

@Getter
@Setter
public class CircleDto extends FigureDto {
    private double radius;

    public CircleDto(double radius) {
        super("KOLO");
        this.radius = radius;
    }
}
