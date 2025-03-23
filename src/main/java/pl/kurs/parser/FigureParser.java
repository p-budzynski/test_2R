package pl.kurs.parser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kurs.entity.Circle;
import pl.kurs.entity.Figure;
import pl.kurs.entity.Rectangle;
import pl.kurs.entity.Square;
import pl.kurs.validator.FigureValidator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FigureParser {
    private final static String REGEX = ";";
    private final FigureValidator figureValidator;

    public List<Figure> parseFiguresFromFile(File file) throws IOException {
        List<Figure> figureList = new ArrayList<>();
        int lineNumber = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (!line.trim().isEmpty()) {
                    boolean isValid = figureValidator.isValid(line, lineNumber);
                    if (isValid) {
                        Figure figure = parseLine(line);
                        if (figure != null) {
                            figureList.add(figure);
                        }
                    }
                }
            }
        }
        return figureList;
    }

    private Figure parseLine(String line) {
        String[] parts = line.split(REGEX);
        String type = parts[0].trim();

        switch (type.toUpperCase()) {
            case "SQUARE":
                double side = Double.parseDouble(parts[1]);
                return new Square(side);
            case "RECTANGLE":
                double width = Double.parseDouble(parts[1]);
                double height = Double.parseDouble(parts[2]);
                return new Rectangle(width, height);
            case "CIRCLE":
                double radius = Double.parseDouble(parts[1]);
                return new Circle(radius);
            default:
                return null;
        }
    }

}
