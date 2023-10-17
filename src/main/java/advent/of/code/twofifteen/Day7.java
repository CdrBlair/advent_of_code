package advent.of.code.twofifteen;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class Day7 {

    Map<String, Integer> wires = new HashMap<>();
    Map<String, Instruction> wireInstruction = new HashMap<>();

    public int wireSignal() throws IOException {

        try (var br = new BufferedReader(new FileReader(
                "/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/wires.txt"));) {

            List<String> lines = br.lines().collect(Collectors.toList());

            lines.stream().forEach(l -> {

                String[] splittedLine = l.split(" -> ");
                wires.put(splittedLine[1], null);

                if (splittedLine[0].contains("AND")) {
                    String[] splittedInstruction = splittedLine[0].split(" AND ");
                    wireInstruction.put(splittedLine[1],
                            new Instruction("AND", splittedInstruction[0], splittedInstruction[1]));
                } else if (splittedLine[0].contains("OR")) {
                    String[] splittedInstruction = splittedLine[0].split(" OR ");
                    wireInstruction.put(splittedLine[1],
                            new Instruction("OR", splittedInstruction[0], splittedInstruction[1]));
                } else if (splittedLine[0].contains("LSHIFT")) {
                    String[] splittedInstruction = splittedLine[0].split(" LSHIFT ");
                    wireInstruction.put(splittedLine[1],
                            new Instruction("LSHIFT", splittedInstruction[0], splittedInstruction[1]));
                } else if (splittedLine[0].contains("RSHIFT")) {
                    String[] splittedInstruction = splittedLine[0].split(" RSHIFT ");
                    wireInstruction.put(splittedLine[1],
                            new Instruction("RSHIFT", splittedInstruction[0], splittedInstruction[1]));
                } else if (splittedLine[0].contains("NOT")) {
                    String[] splittedInstruction = splittedLine[0].split("NOT ");
                    wireInstruction.put(splittedLine[1], new Instruction("NOT", splittedInstruction[1], null));
                } else {
                    if (StringUtils.isNumeric(splittedLine[0])) {
                        wires.put(splittedLine[1], Integer.valueOf(splittedLine[0]));
                    } else {
                        wireInstruction.put(splittedLine[1], new Instruction("ASSIGN", splittedLine[0], null));
                    }
                }

            });

            wires.keySet().stream().forEach(w -> {
                if (wires.get(w) == null) {
                    resolveWire(w);
                }
            });

            System.out.println(wires);

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        return wires.get("a");

    }

    public void resolveWire(String wire) {

        Instruction currentInst = wireInstruction.get(wire);
        System.out.println("Resolving wire: " + wire + " with instruction: " + currentInst);
        if (wires.get(currentInst.wireA) == null && !StringUtils.isNumeric(currentInst.wireA)) {
            resolveWire(currentInst.wireA);
        }
        if (currentInst.wireB != null && !StringUtils.isNumeric(currentInst.wireB)
                && wires.get(currentInst.wireB) == null) {
            resolveWire(currentInst.wireB);
        }
        Integer valueWireA = StringUtils.isNumeric(currentInst.wireA) ? Integer.valueOf(currentInst.wireA)
                : wires.get(currentInst.wireA);
        Integer valueWireB = null;

        if (currentInst.wireB != null) {
            valueWireB = StringUtils.isNumeric(currentInst.wireB) ? Integer.valueOf(currentInst.wireB)
                    : wires.get(currentInst.wireB);
        }

        if (currentInst.action.equals("NOT")) {

            System.out.println("Bits value before not: " + Integer.toBinaryString(valueWireA));
            Integer intermediate = ~valueWireA;

            var binString = Integer.toBinaryString(intermediate);
            System.out.println("Bits value after not: " + binString);
            if (binString.length() > 16) {
                binString = binString.substring(binString.length() - 16);
            }
            System.out.println("Bits value after not and slicing: " + binString);
            wires.put(wire, Integer.valueOf(binString, 2));
        } else if (currentInst.action.equals("AND")) {
            Integer intermediate = valueWireA & valueWireB;
            var binString = Integer.toBinaryString(intermediate);
            if (binString.length() > 16) {
                binString = binString.substring(binString.length() - 16);
            }
            wires.put(wire, Integer.valueOf(binString, 2));

        } else if (currentInst.action.equals("OR")) {
            Integer intermediate = valueWireA | valueWireB;
            var binString = Integer.toBinaryString(intermediate);
            if (binString.length() > 16) {
                binString = binString.substring(binString.length() - 16);
            }
            wires.put(wire, Integer.valueOf(binString, 2));

        } else if (currentInst.action.equals("LSHIFT")) {
            Integer intermediate = valueWireA << valueWireB;
            var binString = Integer.toBinaryString(intermediate);
            if (binString.length() > 16) {
                binString = binString.substring(binString.length() - 16);
            }
            wires.put(wire, Integer.valueOf(binString, 2));

        } else if (currentInst.action.equals("RSHIFT")) {
            Integer intermediate = valueWireA >> valueWireB;
            var binString = Integer.toBinaryString(intermediate);
            if (binString.length() > 16) {
                binString = binString.substring(binString.length() - 16);
            }
            wires.put(wire, Integer.valueOf(binString, 2));

        } else if (currentInst.action.equals("ASSIGN")) {
            wires.put(wire, valueWireA);
        }

    }

    record Instruction(String action, String wireA, String wireB) {

    }
}
