package advent.of.code.twofifteen;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day6 {

    final String TURNED_ON = "*";
    final String TOGGLE = "toggle";
    final String TURN_ON = "+";
    final String TURN_OFF = "-";

    public int lightSwitching() throws IOException {

        Map<Coordinate, Integer> lightGrid = new HashMap<>();

        for (var i = 0; i < 1000; i++) {
            for (var j = 0; j < 1000; j++) {
                lightGrid.put(new Coordinate(i, j), 0);
            }

        }


        try (var br = new BufferedReader(new FileReader(
                "/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/lightToggle.txt"));) {

            List<Instruction> instructions = new ArrayList<>();

            br.lines().forEach(l -> {
                String action;
                String[] splittedLine = l.split(" ");

                if (splittedLine[0].equals("toggle")) {
                    action = TOGGLE;
                } else if (splittedLine[1].equals("on")) {
                    action = TURN_ON;
                } else {
                    action = TURN_OFF;
                }

                if (action.equals(TOGGLE)) {
                    instructions.add(new Instruction(action,
                            new Coordinate(Integer.parseInt(splittedLine[1].split(",")[0]),
                                    Integer.parseInt(splittedLine[1].split(",")[1])),
                            new Coordinate(Integer.parseInt(splittedLine[3].split(",")[0]),
                                    Integer.parseInt(splittedLine[3].split(",")[1]))));
                } else {
                    instructions.add(new Instruction(action,
                            new Coordinate(Integer.parseInt(splittedLine[2].split(",")[0]),
                                    Integer.parseInt(splittedLine[2].split(",")[1])),
                            new Coordinate(Integer.parseInt(splittedLine[4].split(",")[0]),
                                    Integer.parseInt(splittedLine[4].split(",")[1]))));
                }

            });

            instructions.forEach(inst -> {
                System.out.println(inst);
                for (var i = inst.start().x(); i <= inst.end().x(); i++) {
                    for (var j = inst.start().y(); j <= inst.end().y(); j++) {

                        if (inst.action().equals(TOGGLE)) {
                            lightGrid.put(new Coordinate(i, j), lightGrid.get(new Coordinate(i, j)) + 2);
                        } else if (inst.action().equals(TURN_ON)) {
                            lightGrid.put(new Coordinate(i, j), lightGrid.get(new Coordinate(i, j)) + 1);
                        } else {
                            if (lightGrid.get(new Coordinate(i, j)) > 0) {
                                lightGrid.put(new Coordinate(i, j), lightGrid.get(new Coordinate(i, j)) - 1);
                            }
                        }

                    }
                }

                System.out.println(lightGrid.values().stream().filter(s -> s.equals(TURNED_ON)).toList().size());

            });

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        return lightGrid.values().stream().mapToInt(Integer::valueOf).sum();

    }

    record Coordinate(int x, int y) {
    }

    record Instruction(String action, Coordinate start, Coordinate end) {
    }
}
