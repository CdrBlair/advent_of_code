package advent.of.code.twofifteen;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day13 {


    List<String> people = new ArrayList<>();
    Map<Relation, Integer> affection = new HashMap<>();

    public int calculateHappiness() throws IOException {
        var result = 0;

        try (var br = new BufferedReader(new FileReader(
                "/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/happines.txt"));) {

            br.lines().forEach(l -> {
                l = StringUtils.remove(l, ".").trim();
                var split = l.split(" ");
                people.add(split[0]);
                people.add(split[10]);
                var value = Integer.parseInt(split[3]);
                if (split[2].equals("lose")) {
                    value = -value;
                }

                affection.put(new Relation(split[0], split[10]), value);
            });

            people = new ArrayList<>(people.stream().distinct().toList());

            var me = "me";
            people.forEach(p -> {
                affection.put(new Relation(me, p), 0);
                affection.put(new Relation(p, me), 0);
            });
            people.add(me);

            System.out.println("people: " + people.size());
            var permutations = CollectionUtils.permutations(people);
            System.out.println("permutations: " + permutations.size());
            result = permutations.stream().mapToInt(this::calculateHappiness).max().orElse(-1);


        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        return result;
    }

    private int calculateHappiness(List<String> people) {

        var result = 0;
        for (int i = 0; i < people.size(); i++) {
            if (i == 0) {
                result += affection.get(new Relation(people.get(i), people.get(people.size() - 1)));
            } else {
                result += affection.get(new Relation(people.get(i), people.get(i - 1)));
            }
            if (i == people.size() - 1) {
                result += affection.get(new Relation(people.get(i), people.get(0)));
            } else {
                result += affection.get(new Relation(people.get(i), people.get(i + 1)));
            }

        }
        return result;
    }

    record Relation(String person, String neighbour) {
    }
}


