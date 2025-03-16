package pl.kurs.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class FigureValidatorTest {
    private FigureValidator figureValidator;
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @BeforeEach
    void setup() {
        figureValidator = new FigureValidator();
        System.setErr(new PrintStream(errContent));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldReturnFalseForNullOrEmptyLines(String input){
        //when
        boolean result = figureValidator.isValid(input, 1);

        //then
        assertThat(result).isFalse();
        assertThat(errContent.toString()).contains("Line number: '1' is empty.");
    }

    @Test
    void shouldReturnFalseForLinesWithInvalidFormat() {
        //given
        String line = "SQUARE";

        //when
        boolean result = figureValidator.isValid(line, 1);

        //then
        assertThat(result).isFalse();
        assertThat(errContent.toString()).contains("Line number: '1' has invalid format.");
    }

    @Test
    void shouldReturnFalseForUnknownFigureType() {
        //given
        String line = "TRIANGLE;10";

        //when
        boolean result = figureValidator.isValid(line, 1);

        //then
        assertThat(result).isFalse();
        assertThat(errContent.toString()).contains("Line number: '1' has unknown figure type: 'TRIANGLE'.");
    }

    @ParameterizedTest
    @MethodSource("squareTestCases")
    void shouldValidateSquareCorrectly(String line, int lineNumber, boolean expectedResult, String expectedErrorMessage) {
        //given
        errContent.reset();

        //when
        boolean result = figureValidator.isValid(line, lineNumber);

        //then
        assertThat(result).isEqualTo(expectedResult);
        if (!expectedErrorMessage.isEmpty()) {
            assertThat(errContent.toString()).contains(expectedErrorMessage);
        }
    }

    @ParameterizedTest
    @MethodSource("rectangleTestCases")
    void shouldValidateRectangleCorrectly(String line, int lineNumber, boolean expectedResult, String expectedErrorMessage) {
        //given
        errContent.reset();

        //when
        boolean result = figureValidator.isValid(line, lineNumber);

        //then
        assertThat(result).isEqualTo(expectedResult);
        if (!expectedErrorMessage.isEmpty()) {
            assertThat(errContent.toString()).contains(expectedErrorMessage);
        }
    }

    @ParameterizedTest
    @MethodSource("circleTestCases")
    void shouldValidateCircleCorrectly(String line, int lineNumber, boolean expectedResult, String expectedErrorMessage) {
        //given
        errContent.reset();

        //when
        boolean result = figureValidator.isValid(line, lineNumber);

        //then
        assertThat(result).isEqualTo(expectedResult);
        if (!expectedErrorMessage.isEmpty()) {
            assertThat(errContent.toString()).contains(expectedErrorMessage);
        }
    }

    static Stream<Arguments> squareTestCases() {
        return Stream.of(
                arguments("SQUARE;10", 1, true, ""),
                arguments("SQUARE;0", 2, false, "Line number: '2' - length of square side must be positive."),
                arguments("SQUARE;-5", 3, false, "Line number: '3' - length of square side must be positive."),
                arguments("SQUARE;abc", 4, false, "Line number: '4' has invalid format for square side parameter - expected a number."),
                arguments("SQUARE;10;20", 5, false, "Line number: '5' has the wrong number of parameters for square.")
        );
    }

    static Stream<Arguments> rectangleTestCases() {
        return Stream.of(
                arguments("RECTANGLE;10;20", 1, true, ""),
                arguments("RECTANGLE;0;20", 2, false, "Line number: '2' - length of rectangle width must be positive."),
                arguments("RECTANGLE;10;0", 3, false, "Line number: '3' - length of rectangle height must be positive."),
                arguments("RECTANGLE;abc;20", 4, false, "Line number: '4' has invalid format for rectangle width parameter - expected a number."),
                arguments("RECTANGLE;10;abc", 5, false, "Line number: '5' has invalid format for rectangle height parameter - expected a number."),
                arguments("RECTANGLE;10", 6, false, "Line number: '6' has the wrong number of parameters for rectangle."),
                arguments("RECTANGLE;10;20;30", 7, false, "Line number: '7' has the wrong number of parameters for rectangle.")
        );
    }

    static Stream<Arguments> circleTestCases() {
        return Stream.of(
                arguments("CIRCLE;10", 1, true, ""),
                arguments("CIRCLE;0", 2, false, "Line number: '2' - length of circle radius must be positive."),
                arguments("CIRCLE;-5", 3, false, "Line number: '3' - length of circle radius must be positive."),
                arguments("CIRCLE;abc", 4, false, "Line number: '4' has invalid format for circle radius parameter - expected a number."),
                arguments("CIRCLE;10;20", 5, false, "Line number: '5' has the wrong number of parameters for circle.")
        );
    }

}


