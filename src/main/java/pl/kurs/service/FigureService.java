package pl.kurs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.dto.FigureDto;
import pl.kurs.entity.Figure;
import pl.kurs.mapper.FigureMapper;
import pl.kurs.parser.FigureParser;
import pl.kurs.repository.FigureRepository;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FigureService {
    private final FigureRepository figureRepository;
    private final FigureMapper figureMapper;
    private final FigureParser figureParser;

    public Integer uploadFigures(MultipartFile file) throws IOException {
        List<FigureDto> figureDtoList = figureParser.parseFiguresFromFile(file);

        List<Figure> figureList = figureMapper.toEntities(figureDtoList);

        figureRepository.saveAll(figureList);

        return figureList.size();
    }

    public FigureDto findFigureWithLargestArea() {
        Figure databaseResult = figureRepository.findFiguresWithLargestArea();
        return figureMapper.toDto(databaseResult);
    }
}
