package pl.kurs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import pl.kurs.service.FigureService;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class Application {
    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class);

        FigureService figureService = ctx.getBean(FigureService.class);

        figureService.uploadFigures(new File("src/main/resources/test-figures.txt"));

        System.out.println(figureService.findFigureWithLargestArea());

    }
}
