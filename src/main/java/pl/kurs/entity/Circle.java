package pl.kurs.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "circle")
@SequenceGenerator(name = "circle_seq", sequenceName = "circle_seq", allocationSize = 1)
public class Circle extends Figure {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "circle_seq")
    private Long id;

    @Column(name = "radius", nullable = false)
    private Double radius;

    public Circle(Double radius) {
        super("KOLO");
        this.radius = radius;
    }
}
