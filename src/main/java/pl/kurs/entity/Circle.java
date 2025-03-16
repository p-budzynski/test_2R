package pl.kurs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "circle")
@NoArgsConstructor
@Getter
public class Circle extends Figure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "radius", nullable = false)
    private Double radius;

    public Circle(Double radius) {
        super("CIRCLE");
        this.radius = radius;
    }
}
