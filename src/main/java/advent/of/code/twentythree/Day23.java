package advent.of.code.twentythree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day23 {

    public int getLongestPath() throws IOException {

        var startTime = System.currentTimeMillis();
        try (var br = new BufferedReader(new FileReader(
                "/Users/amv/work/advent_of_code/advent_of_code/src/main/resources/hikemap.txt"))) {

            List<String> linesOfMap = br.lines().toList();

            Map<Coordinate, String> gardeMap = new HashMap<>();

            int maxX = linesOfMap.get(0).strip().length() - 1;
            int maxY = linesOfMap.size() - 1;

            for (var i = 0; i < maxY + 1; i++) {
                String line = linesOfMap.get(i).strip();
                for (var j = 0; j < maxX + 1; j++) {
                    gardeMap.put(new Coordinate(j, i), String.valueOf(line.charAt(j)));
                }

            }

            // print map
            // for (var i = 0; i < maxY + 1; i++) {
            //     for (var j = 0; j < maxX + 1; j++) {
            //         System.out.print(gardeMap.get(new Coordinate(j, i)));
            //     }
            //     System.out.println();
            // }

            Coordinate start = new Coordinate(1, 0);
            Map<Coordinate, Map<Coordinate, Integer>> conjunctions = new HashMap<>();

            Deque<Coordinate> queue = new LinkedList<>();
            queue.add(start);
            Set<Coordinate> visitedStart = new HashSet<>();

            while (!queue.isEmpty()) {

                var currentStart = queue.poll();
                // System.out.println(currentStart);
                if (visitedStart.contains(currentStart)) {
                    continue;
                }
                visitedStart.add(currentStart);
                var visited = new HashSet<Coordinate>();
                var current = currentStart;

                var possible = new ArrayList<Coordinate>();
                List<Coordinate> directions = Arrays.asList(new Coordinate(0, 1), new Coordinate(0, -1),
                        new Coordinate(1, 0), new Coordinate(-1, 0));
                for (var direction : directions) {
                    var next = new Coordinate(current.x + direction.x, current.y + direction.y);
                    if (gardeMap.get(next) != null && !(gardeMap.get(next).equals("#"))) {
                        possible.add(next);
                    }
                }
                // System.err.println("possible: " + possible);
                visited.add(current);
                for (var coord : possible) {
                    boolean foundConjunction = false;
                    var steps = 1;
                    var pathStart = coord;
                    visited.add(pathStart);
                    while (!foundConjunction) {
                        var possibleNext = new ArrayList<Coordinate>();
                        List<Coordinate> directionsInner = Arrays.asList(new Coordinate(0, 1), new Coordinate(0, -1),
                                new Coordinate(1, 0), new Coordinate(-1, 0));
                        for (var direction : directionsInner) {
                            var newCorrd = new Coordinate(pathStart.x + direction.x, pathStart.y + direction.y);
                            // System.out.println("length of directionsInner: " + directionsInner.size());
                            // System.out.println("newCorrd: " + newCorrd);
                            if (gardeMap.get(newCorrd) != null && !(gardeMap.get(newCorrd).equals("#"))
                                    && !visited.contains(newCorrd)) {

                                possibleNext.add(newCorrd);
                            }

                        }
                        if (possibleNext.size() == 1) {
                            pathStart = possibleNext.get(0);
                            visited.add(pathStart);
                            steps++;
                        } else {
                            // System.out.println("possibleNext: " + possibleNext);
                            foundConjunction = true;
                            if (!conjunctions.containsKey(currentStart)) {
                                conjunctions.put(currentStart, new HashMap<>());
                            }
                            conjunctions.get(currentStart).put(pathStart, steps);
                            queue.add(pathStart);

                        }
                    }

                }
            }
            // for (var conj : conjunctions.entrySet()) {
            //     System.out.println(conj.getKey() + " " + conj.getValue());
            // }
            var max = recursiveLengt(new Coordinate(1, 0), new Coordinate(maxX - 1, maxY), conjunctions,
                    new HashSet<>());

            System.err.println("max is: " + max);

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        System.out.println("time: " + (System.currentTimeMillis() - startTime));
        // time in s
        System.out.println("time: " + (System.currentTimeMillis() - startTime) / 1000);
        return 0;

    }

    private int recursiveLengt(Coordinate start, Coordinate end, Map<Coordinate, Map<Coordinate, Integer>> conjunctions,
            Set<Coordinate> before) {
        // System.out.println("start " + start + "end " + end);
        if (conjunctions.get(start).containsKey(end)) {
            // System.out.println("found " + conjunctions.get(start).get(end));
            return conjunctions.get(start).get(end);
        } else {
            var max = -1;
            for (var conj : conjunctions.get(start).keySet()) {
                if (!(before.contains(conj))) {
                    var newBefore = new HashSet<>(before);
                    newBefore.add(conj);
                    int length = (recursiveLengt(conj, end, conjunctions, newBefore))
                            + conjunctions.get(start).get(conj);
                    // System.out.println("length " + length);
                    // System.out.println("max " + max);
                    if (length > max) {
                        max = length;
                    }
                    // System.out.println(i);
                    // System.out.println(conjunctions.get(start).keySet());
                    // System.out.println(before);

                   
                }
            }
            // System.out.println("max " + max);
            return max;
        }
    }

    record Coordinate(int x, int y) {
    }

}
