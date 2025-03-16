package pl.kurs.validator;

import org.springframework.stereotype.Component;

@Component
public class FigureValidator {
    private static final String REGEX = ";";

    public boolean isValid(String line, int lineNumber) {
        if (line == null || line.trim().isEmpty()) {
            System.err.println("Line number: '" + lineNumber + "' is empty.");
            return false;
        }

        String[] parts = line.split(REGEX);
        if (parts.length < 2) {
            System.err.println("Line number: '" + lineNumber + "' has invalid format.");
            return false;
        }


        String type = parts[0].trim();

        switch (type) {
            case "SQUARE":
                return validateSquare(parts, lineNumber);
            case "RECTANGLE":
                return validateRectangle(parts, lineNumber);
            case "CIRCLE":
                return validateCircle(parts, lineNumber);
            default:
                System.err.println("Line number: '" + lineNumber + "' has unknown figure type: '" + type +"'.");
                return false;
        }
    }

    boolean validateCircle(String[] parts, int lineNumber) {
        if (parts.length != 2) {
            System.err.println("Line number: '" + lineNumber + "' has the wrong number of parameters for circle.");
            return false;
        }
        try {
            double radius = Double.parseDouble(parts[1]);
            if (radius <= 0) {
                System.err.println("Line number: '" + lineNumber + "' - length of circle radius must be positive.");
                return false;
            }
        } catch (NumberFormatException e) {
            System.err.println("Line number: '" + lineNumber + "' has invalid format for circle radius parameter - expected a number.");
            return false;
        }
        return true;
    }

    boolean validateRectangle(String[] parts, int lineNumber) {
        if (parts.length != 3) {
            System.err.println("Line number: '" + lineNumber + "' has the wrong number of parameters for rectangle.");
            return false;
        }
        try {
            double width = Double.parseDouble(parts[1]);
            if (width <= 0) {
                System.err.println("Line number: '" + lineNumber + "' - length of rectangle width must be positive.");
                return false;
            }
        } catch (NumberFormatException e) {
            System.err.println("Line number: '" + lineNumber + "' has invalid format for rectangle width parameter - expected a number.");
            return false;
        }
        try {
            double height = Double.parseDouble(parts[2]);
            if (height <= 0) {
                System.err.println("Line number: '" + lineNumber + "' - length of rectangle height must be positive.");
                return false;
            }
        } catch (NumberFormatException e) {
            System.err.println("Line number: '" + lineNumber + "' has invalid format for rectangle height parameter - expected a number.");
            return false;
        }
        return true;
    }

    boolean validateSquare(String[] parts, int lineNumber) {
        if (parts.length != 2) {
            System.err.println("Line number: '" + lineNumber + "' has the wrong number of parameters for square.");
            return false;
        }
        try {
            double side = Double.parseDouble(parts[1]);
            if (side <= 0) {
                System.err.println("Line number: '" + lineNumber + "' - length of square side must be positive.");
                return false;
            }
        } catch (NumberFormatException e) {
            System.err.println("Line number: '" + lineNumber + "' has invalid format for square side parameter - expected a number.");
            return false;
        }
        return true;
    }

}
