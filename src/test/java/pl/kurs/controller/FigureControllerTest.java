package pl.kurs.controller;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.kurs.service.FigureService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FigureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private FigureService figureService;

    @Test
    void shouldUploadFigures() throws Exception {
        //given
        String content = "CIRCLE;5\nSQUARE;7\nRECTANGLE;10;13";
        MockMultipartFile file = new MockMultipartFile(
                "file", "figures.txt", MediaType.MULTIPART_FORM_DATA_VALUE, content.getBytes());

        //when then
        mockMvc.perform(multipart("/figures/upload")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("3"))
                .andReturn();
    }

    @Test
    @Transactional
    void shouldReturnLargestFigure() throws Exception {
        //given
        entityManager.createNativeQuery("INSERT INTO square (name, side) VALUES ('square', 100)").executeUpdate();

        //when then
        mockMvc.perform(get("/figures/largest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("SQUARE"))
                .andExpect(jsonPath("$.side").value(100.0));
    }
}
