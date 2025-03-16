package pl.kurs.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.dto.FigureDto;
import pl.kurs.service.FigureService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/figures")
public class FigureController {
    private final FigureService figureService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Integer> uploadFigures(@RequestPart("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(figureService.uploadFigures(file));
    }

    @GetMapping(value = "/largest")
    public ResponseEntity<FigureDto> getLargestFigure() {
        return ResponseEntity.ok(figureService.findFigureWithLargestArea());
    }
}
