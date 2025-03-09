package pl.kurs.parrser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.dto.CircleDto;
import pl.kurs.dto.FigureDto;
import pl.kurs.dto.RectangleDto;
import pl.kurs.dto.SquareDto;
import pl.kurs.validator.FigureValidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FigureParser {
    private final static String REGEX = ";";
    private final FigureValidator figureValidator;

    public List<FigureDto> parseFiguresFromFile(MultipartFile file) throws IOException {
        List<FigureDto> figureDtoList = new ArrayList<>();
        int lineNumber = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (!line.trim().isEmpty()) {
                    boolean isValid = figureValidator.isValid(line, lineNumber);
                    if (isValid) {
                        FigureDto figureDto = parseLine(line);
                        if (figureDto != null) {
                            figureDtoList.add(figureDto);
                        }
                    }
                }
            }
        }
        return figureDtoList;
    }

    private FigureDto parseLine(String line) {
        String[] parts = line.split(REGEX);
        String type = parts[0].trim();

        switch (type.toUpperCase()) {
            case "KWADRAT":
                double side = Double.parseDouble(parts[1]);
                return new SquareDto(side);
            case "PROSTOKAT":
                double width = Double.parseDouble(parts[1]);
                double height = Double.parseDouble(parts[2]);
                return new RectangleDto(width, height);
            case "KOLO":
                double radius = Double.parseDouble(parts[1]);
                return new CircleDto(radius);
            default:
                return null;
        }
    }

}
