package pl.kurs.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.kurs.entity.Figure;

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

    public List<Object[]> findFiguresWithLargestArea() {
        String sql = """
                WITH figures AS (
                SELECT name, radius::double precision AS param1, NULL::double precision AS param2, (PI() * radius * radius) AS area FROM circle
                UNION ALL
                SELECT name, side::double precision AS param1, NULL::double precision AS param2, (side * side) AS area FROM square
                UNION ALL
                SELECT name, width::double precision AS param1, height::double precision AS param2, (width * height) AS area FROM rectangle
                ), max_area AS (
                SELECT MAX(area) AS max_value FROM figures
                )
                SELECT f.name, f.param1, f.param2
                FROM figures f, max_area m
                WHERE f.area = m.max_value
                ORDER BY f.name;
                """;

        Query query = entityManager.createNativeQuery(sql);

        @SuppressWarnings("unchecked")
        List<Object[]> result = query.getResultList();

        return result;
    }

}
