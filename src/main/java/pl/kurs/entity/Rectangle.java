package pl.kurs.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "rectangle")
@SequenceGenerator(name = "rectangle_seq", sequenceName = "rectangle_seq", allocationSize = 1)
public class Rectangle extends Figure {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rectangle_seq")
    private Long id;

    @Column(name = "width", nullable = false)
    private Double width;

    @Column(name = "height", nullable = false)
    private Double height;

    public Rectangle(Double width, Double height) {
        super("PROSTOKAT");
        this.width = width;
        this.height = height;
    }

}
