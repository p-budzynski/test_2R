package pl.kurs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rectangle") @NoArgsConstructor
@Getter
public class Rectangle extends Figure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "width", nullable = false)
    private Double width;

    @Column(name = "height", nullable = false)
    private Double height;

    public Rectangle(Double width, Double height) {
        super("RECTANGLE");
        this.width = width;
        this.height = height;
    }

}
