/*
 * project:     COMPUCLICK
 * file:        Equation.java
 * modified:    2023-12-02
 * name:        Ethan Mentzer
 * lab:         CSC 171-13
 */


// Import necessary classes
import java.util.Random;


/**
 * The Equation class stores information about each round's equation
 * that sits at the top of the screen. Each instance is a randomly
 * generated equation of level 0, 1, or 2. Higher levels are more
 * difficult to solve.
 */
public class Equation {

    // Declare instance variables
    public String equation, answerAsString;
    public double answer;
    public boolean hard;

    // Constructor
    public Equation(int level) {
        Random rng = new Random();
        int x, y, z;
        char o1, o2;

        // Hard equations are hard-coded
        String[] hardEqs = {"cos(π/3)", "sin(π)", "tan(π)", "f(x) = x^2, f'(2) = ?", "f(x) = ln(x), f'(1) = ?"};
        double[] hardAns = {0.5, 0.0, 0.0, 4.0, 1.0};

        // Generate equation based on desired difficulty level
        char[] operators = {'+', '-', '*'};
        switch (level) {
            case 0:
                x = rng.nextInt(1, 10);
                y = rng.nextInt(1, 10);
                o1 = operators[rng.nextInt(0, 3)];
                equation = String.format("%s %s %s", x, o1, y);
                answer = calculateAnswer(x, o1, y);
                answerAsString = String.valueOf(answer);
                break;
            case 1:
                x = rng.nextInt(1, 10);
                y = rng.nextInt(1, 10);
                z = rng.nextInt(1, 10);
                o1 = operators[rng.nextInt(0, 3)];
                o2 = operators[rng.nextInt(0, 3)];
                equation = String.format("%s %s %s %s %s", x, o1, y, o2, z);
                answer = calculateAnswer(x, o1, y, o2, z);
                answerAsString = String.valueOf(answer);
                break;
            case 2:
                // 10% chance of very hard problem
                if (rng.nextInt(0, 10) >= -1) {
                    int randIndex = rng.nextInt(0, hardEqs.length);
                    equation = hardEqs[randIndex];
                    answer = hardAns[randIndex];
                    answerAsString = String.valueOf(answer);
                    hard = true;
                    return;
                }
                x = rng.nextInt(1, 10);
                y = rng.nextInt(1, 100);
                o1 = operators[rng.nextInt(0, 3)];
                equation = String.format("%s %s %s", x, o1, y);
                answer = calculateAnswer(x, o1, y);
                answerAsString = String.valueOf(answer);
                break;
            default:
                break;
        }
    }

    /**
     * calculateAnswer calculates the answer for multiple equation levels via overloading.
     */
    private double calculateAnswer(int x, char o, int y) {
        return switch (o) {
            case '+' -> x + y;
            case '-' -> x - y;
            case '*' -> x * y;
            default -> 0;
        };
    }

    // Overloaded method for level 2 equations
    private double calculateAnswer(int x, char o1, int y, char o2, int z) {
        String firstCalc = "xy";
        int result1 = 0;

        // Apply order of operations
        if (o1 == '*') {
            firstCalc = "xy";
            result1 = x * y;
        } else if (o2 == '*') {
            firstCalc = "yz";
            result1 = y * z;
        } else {
            result1 = switch (o1) {
                case '+' -> x + y;
                case '-' -> x - y;
                default -> result1;
            };
        }

        // Return calculated below depending on order of operations
        if (firstCalc.equals("xy")) {
            return switch (o2) {
                case '+' -> result1 + z;
                case '-' -> result1 - z;
                case '*' -> result1 * z;
                default -> 0;
            };
        } else {
            return switch (o1) {
                case '+' -> x + result1;
                case '-' -> x - result1;
                default -> 0;
            };
        }
    }

}
