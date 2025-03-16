package pl.kurs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "square")
@NoArgsConstructor
@Getter
public class Square extends Figure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "side", nullable = false)
    private Double side;

    public Square(Double side) {
        super("SQUARE");
        this.side = side;
    }
}
