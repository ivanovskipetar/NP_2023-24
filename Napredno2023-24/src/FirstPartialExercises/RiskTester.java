package FirstPartialExercises;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Round {
    List<Integer> attacker;
    List<Integer> defender;

    public Round(List<Integer> attacker, List<Integer> defender) {
        this.attacker = attacker;
        this.defender = defender;
    }

    public Round(String line) {
        //5 3 4;2 4 1
        String[] parts = line.split(";");
        this.attacker = parseDice(parts[0]);
        this.defender = parseDice(parts[1]);
    }

    private List<Integer> parseDice(String input) {
        return Arrays.stream(input.split("\\s+"))
                .map(dice -> Integer.parseInt(dice))
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return String.format("%d %d", countAttackerPoints(), countDefenderPoints());
    }

    private int countAttackerPoints() {
        int attackerPoints = 0;
        for (int i = 0; i < attacker.size(); i++) {
            if (attacker.get(i) > defender.get(i)) {
                attackerPoints++;
            }
        }
        return attackerPoints;
    }

    private int countDefenderPoints() {
        return attacker.size() - countAttackerPoints();
    }
}

class Risk {

    public void processAttacksData(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        List<Round> rounds = br.lines()
                .map(line -> new Round(line))
                .collect(Collectors.toList());

        rounds.stream().forEach(round -> System.out.println(round));

        br.close();

    }
}

public class RiskTester {
    public static void main(String[] args) {
        Risk risk = new Risk();
        try {
            risk.processAttacksData(System.in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
