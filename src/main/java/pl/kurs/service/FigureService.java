package pl.kurs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kurs.entity.Figure;
import pl.kurs.parser.FigureParser;
import pl.kurs.repository.FigureRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FigureService {
    private final FigureRepository figureRepository;
    private final FigureParser figureParser;

    public void uploadFigures(File file) throws IOException {
        List<Figure> figureList = figureParser.parseFiguresFromFile(file);

        figureRepository.saveAll(figureList);
    }

    public Figure findFigureWithLargestArea() {
        return figureRepository.findFiguresWithLargestArea();
    }
}
