package pl.kurs.repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.kurs.entity.Circle;
import pl.kurs.entity.Figure;
import pl.kurs.entity.Rectangle;
import pl.kurs.entity.Square;

import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class FigureRepository {
    private final EntityManager entityManager;

    public void saveAll(List<Figure> figureList) {
        for (Figure figure : figureList) {
            entityManager.persist(figure);
        }
    }

    public Figure findFiguresWithLargestArea() {
        @SuppressWarnings("unchecked")
        List<Object[]> results = entityManager.createNativeQuery(
                """
                        SELECT typ, id FROM figure_max_area
                        ORDER BY area DESC
                        LIMIT 1;"""
        ).getResultList();

        if (results.isEmpty()) {
            System.err.println("No figures in database.");
            return null;
        }

        Object[] result = results.get(0);
        String typ = (String) result[0];
        Long id = ((Number) result[1]).longValue();

        return switch (typ.toLowerCase()) {
            case "circle" -> entityManager.find(Circle.class, id);
            case "square" -> entityManager.find(Square.class, id);
            case "rectangle" -> entityManager.find(Rectangle.class, id);
            default -> throw new IllegalStateException("Unknown figure type: " + typ);
        };
    }

}
