package LaboratoryExercise01;

import java.util.Scanner;
import java.util.stream.IntStream;

public class RomanConverterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        IntStream.range(0, n)
                .forEach(x -> System.out.println(RomanConverter.toRoman(scanner.nextInt())));
        scanner.close();
    }
}


class RomanConverter {
    /**
     * Roman to decimal converter
     *
     * @param n number in decimal format
     * @return string representation of the number in Roman numeral
     */
    public static String toRoman(int n) {
        StringBuilder sb = new StringBuilder();

        String[] romanRepr = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        int[] arabicRepr = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};

        int tmp;
        for (int i = 0; i < arabicRepr.length; i++) {
            if (n >= arabicRepr[i]) {
                tmp = n / arabicRepr[i];
                n -= tmp * arabicRepr[i];
                while (tmp > 0) {
                    sb.append(romanRepr[i]);
                    tmp--;
                }
            }
        }

        return sb.toString();
    }

}
