package SecondPartialExercisesAndExams.QuizProcessor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class CalculationCanNotBeProcessedException extends Exception {
    public CalculationCanNotBeProcessedException() {
        super("A quiz must have same number of correct and selected answers\n");
    }
}

class Quiz {
    private final String studentId;
    private final List<String> correctAnswers;
    private final List<String> studentAnswers;
    private double studentPoints;

    public Quiz(String studentId, List<String> correctAnswers, List<String> studentAnswers) throws CalculationCanNotBeProcessedException {
        if (correctAnswers.size() != studentAnswers.size())
            throw new CalculationCanNotBeProcessedException();

        this.studentId = studentId;
        this.correctAnswers = correctAnswers;
        this.studentAnswers = studentAnswers;

        calculateStudentPoints(correctAnswers, studentAnswers);
    }

    public static Quiz createQuiz(String line) throws CalculationCanNotBeProcessedException {
        String[] parts = line.split(";");
        String studentId = parts[0];
        List<String> correctAnswers = new ArrayList<>(Arrays.asList(parts[1].split(",")));
        List<String> studentAnswers = new ArrayList<>(Arrays.asList(parts[2].split(",")));
        return new Quiz(studentId, correctAnswers, studentAnswers);
    }

    private void calculateStudentPoints(List<String> correctAnswers, List<String> studentAnswers) {
        IntStream.range(0, correctAnswers.size())
                .forEach(i -> {
                    if (correctAnswers.get(i).equals(studentAnswers.get(i))) {
                        studentPoints++;
                    } else
                        studentPoints -= 0.25;
                });
    }

    public String getStudentId() {
        return studentId;
    }

    public List<String> getCorrectAnswers() {
        return correctAnswers;
    }

    public List<String> getStudentAnswers() {
        return studentAnswers;
    }

    public double getStudentPoints() {
        return studentPoints;
    }
}

class QuizProcessor {

    public static Map<String, Double> processAnswers(InputStream in) {
        //200000;C, D, D, D, A, C, B, D, D;C, D, D, D, D, B, C, D, A
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        return br.lines()
                .map(line -> {
                    try {
                        return Quiz.createQuiz(line);
                    } catch (CalculationCanNotBeProcessedException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                }).filter(Objects::nonNull)
                .collect(Collectors.toMap(Quiz::getStudentId, Quiz::getStudentPoints, Double::sum, TreeMap::new));
    }
}

public class QuizProcessorTest {
    public static void main(String[] args) {
        QuizProcessor.processAnswers(System.in).forEach((k, v) -> System.out.printf("%s -> %.2f%n", k, v));
    }
}