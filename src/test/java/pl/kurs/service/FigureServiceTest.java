package pl.kurs.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.kurs.entity.Circle;
import pl.kurs.entity.Figure;
import pl.kurs.entity.Rectangle;
import pl.kurs.entity.Square;
import pl.kurs.parser.FigureParser;
import pl.kurs.repository.FigureRepository;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class FigureServiceTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private FigureService figureService;

    @Autowired
    private FigureRepository figureRepository;

    @Autowired
    private FigureParser figureParser;

    @Test
    @Transactional
    void shouldSaveFiguresFromFile() throws IOException {
        //given
        loadTestData();

        //when then
        Circle circle = entityManager.find(Circle.class, 1L);
        Square square = entityManager.find(Square.class, 1L);
        Rectangle rectangle = entityManager.find(Rectangle.class, 1L);

        assertThat(circle).isNotNull();
        assertThat(circle.getRadius()).isEqualTo(5.0);

        assertThat(square).isNotNull();
        assertThat(square.getSide()).isEqualTo(4.0);

        assertThat(rectangle).isNotNull();
        assertThat(rectangle.getWidth()).isEqualTo(3.0);
        assertThat(rectangle.getHeight()).isEqualTo(6.0);
    }

    @Test
    void shouldFindFigureWithLargestArea() throws IOException {
        //given
        loadTestData();

        //when
        Figure largestFigure = figureService.findFigureWithLargestArea();

        //then
        assertThat(largestFigure).isNotNull();
        assertThat(largestFigure).isInstanceOf(Circle.class);
        Circle circle = (Circle) largestFigure;
        assertThat(circle.getRadius()).isEqualTo(5.0);
    }

    private void loadTestData() throws IOException {
        File file = new File("src/test/resources/test-figures.txt");
        figureService.uploadFigures(file);
    }
}
