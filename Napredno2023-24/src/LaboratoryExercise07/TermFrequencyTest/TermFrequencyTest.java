package LaboratoryExercise07.TermFrequencyTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

class TermFrequency {
    Map<String, Integer> words;
    int total;

    public TermFrequency(InputStream inputStream, String[] stopWords) {
        words = new TreeMap<>();
        total = 0;

        Scanner scanner = new Scanner(inputStream);
        List<String> stop = Arrays.asList(stopWords);

        while (scanner.hasNext()) {
            String word = scanner.next();
            word = word.toLowerCase().replace(',', '\0').replace('.', '\0').trim();
            if (word.isEmpty() || stop.contains(word)) continue;

            int v = words.computeIfAbsent(word, x -> 0);
            words.put(word, ++v);
            total++;
        }
    }

    public int countTotal() {
        return total;
    }

    public int countDistinct() {
        return words.size();
    }

    public List<String> mostOften(int k) {
        return words.keySet().stream()
                .sorted(Comparator.comparing(x -> words.get(x)).reversed())
                .limit(k)
                .collect(Collectors.toList());
    }
}

public class TermFrequencyTest {
    public static void main(String[] args) throws FileNotFoundException {
        String[] stop = new String[]{"во", "и", "се", "за", "ќе", "да", "од",
                "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                "што", "на", "а", "но", "кој", "ја"};
        TermFrequency tf = new TermFrequency(System.in,
                stop);
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
        System.out.println(tf.mostOften(10));
    }
}
