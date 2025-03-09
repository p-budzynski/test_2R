package pl.kurs.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "square")
@SequenceGenerator(name = "square_seq", sequenceName = "square_seq", allocationSize = 1)
public class Square extends Figure {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "square_seq")
    private Long id;

    @Column(name = "side", nullable = false)
    private Double side;

    public Square(Double side) {
        super("KWADRAT");
        this.side = side;
    }
}
