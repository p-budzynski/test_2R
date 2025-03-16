package pl.kurs.config;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class DatabaseInitializerTest  {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void shouldCreateFigureMaxAreaView() {
        //when
        List<String> views = entityManager.createNativeQuery(
                "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = 'PUBLIC'"
        ).getResultList();

        //then
        assertTrue(views.contains("FIGURE_MAX_AREA"), "View FIGURE_MAX_AREA should exist");
    }

    @Test
    @Transactional
    public void shouldReturnCorrectMaxAreaFigureWhenCircleHasMaxArea() {
        //given
        entityManager.createNativeQuery("INSERT INTO circle (name, radius) VALUES ('circle', 5)").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO square (name, side) VALUES ('square', 8)").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO rectangle (name, width, height) VALUES ('rectangle', 4, 5)").executeUpdate();

        //when
        List<Map<String, Object>> result = jdbcTemplate.queryForList("SELECT * FROM figure_max_area");

        //then
        assertEquals(1, result.size());
        Map<String, Object> row = result.getFirst();
        assertEquals("circle", row.get("typ"));

        double expectedArea = Math.PI * 5 * 5;

        double actualArea;
        if (row.get("area") instanceof BigDecimal) {
            actualArea = ((BigDecimal) row.get("area")).doubleValue();
        } else {
            actualArea = ((Number) row.get("area")).doubleValue();
        }
        assertEquals(expectedArea, actualArea, 0.01);
    }

    @Test
    @Transactional
    public void shouldReturnCorrectMaxAreaFigureWhenSquareHasMaxArea() {
        //given
        entityManager.createNativeQuery("INSERT INTO circle (name, radius) VALUES ('circle', 3)").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO square (name, side) VALUES ('square', 10)").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO rectangle (name, width, height) VALUES ('rectangle', 4, 5)").executeUpdate();

        //when
        List<Map<String, Object>> result = jdbcTemplate.queryForList("SELECT * FROM figure_max_area");

        //then
        assertEquals(1, result.size());
        Map<String, Object> row = result.getFirst();
        assertEquals("square", row.get("typ"));

        double expectedArea = 10 * 10;

        double actualArea;
        if (row.get("area") instanceof BigDecimal) {
            actualArea = ((BigDecimal) row.get("area")).doubleValue();
        } else {
            actualArea = ((Number) row.get("area")).doubleValue();
        }
        assertEquals(expectedArea, actualArea, 0.01);

    }

    @Test
    @Transactional
    public void shouldReturnCorrectMaxAreaFigureWhenRectangleHasMaxArea() {
        //given
        entityManager.createNativeQuery("INSERT INTO circle (name, radius) VALUES ('circle', 3)").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO square (name, side) VALUES ('square', 7)").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO rectangle (name, width, height) VALUES ('rectangle', 12, 15)").executeUpdate();

        //when
        List<Map<String, Object>> result = jdbcTemplate.queryForList("SELECT * FROM figure_max_area");

        //then
        assertEquals(1, result.size());
        Map<String, Object> row = result.getFirst();
        assertEquals("rectangle", row.get("typ"));

        double expectedArea = 12 * 15;

        double actualArea;
        if (row.get("area") instanceof BigDecimal) {
            actualArea = ((BigDecimal) row.get("area")).doubleValue();
        } else {
            actualArea = ((Number) row.get("area")).doubleValue();
        }
        assertEquals(expectedArea, actualArea, 0.01);
    }

}
