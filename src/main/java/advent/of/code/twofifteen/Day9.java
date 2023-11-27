package advent.of.code.twofifteen;

import org.apache.commons.collections4.CollectionUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Day9 {


    private Set<String> cities = new HashSet<>();
    private Map<Connection, Integer> distances = new HashMap<>();


    public int getLowestWay() throws IOException {
        var result = 0;


        try (var br = new BufferedReader(new FileReader(
                "/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/connections.txt"));) {

            br.lines().forEach(l -> {

                var split = l.split(" ");
                cities.add(split[0]);
                cities.add(split[2]);
                distances.put(new Connection(split[0], split[2]), Integer.parseInt(split[4]));
                distances.put(new Connection(split[2], split[0]), Integer.parseInt(split[4]));

            });

            var permutations = CollectionUtils.permutations(cities);

            System.out.println(permutations);

            result = permutations.stream().mapToInt(p -> {
                var sum = 0;
                for (int i = 0; i < p.size() - 1; i++) {
                    sum += distances.get(new Connection(p.get(i), p.get(i + 1)));
                }
                System.out.println("sum is: " + sum);
                return sum;
            }).max().orElse(-1);


        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }


        return result;


    }

    record Connection(String start, String end) {


    }
}
