package advent.of.code.twofifteen;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Day16 {

    private Characteristics rigthSueCharacteristics = new Characteristics(3, 7, 2, 3, 0, 0, 5, 3, 2, 1);

    private Map<Integer, Characteristics> sues = new HashMap<>();

    public int findSue() throws FileNotFoundException, IOException {

        long start = System.nanoTime();

        try (var br = new BufferedReader(new FileReader(
                "/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/listofsues.txt"));) {

            br.lines().forEach(l -> {
                int sueNumber = Integer.parseInt(l.split(":")[0].split(" ")[1]);
                String[] characteristics = l.split(":", 2)[1].split(",");
                int children = -1;
                int cats = -1;
                int samoyeds = -1;
                int pomeranians = -1;
                int akitas = -1;
                int vizslas = -1;
                int goldfish = -1;
                int trees = -1;
                int cars = -1;
                int parfumes = -1;

                for (String characteristic : characteristics) {
                    String[] split_characteristic = characteristic.split(":");
                    switch (split_characteristic[0].trim()) {
                        case "children":
                            children = Integer.parseInt(split_characteristic[1].trim());
                            break;
                        case "cats":
                            cats = Integer.parseInt(split_characteristic[1].trim());
                            break;
                        case "samoyeds":
                            samoyeds = Integer.parseInt(split_characteristic[1].trim());
                            break;
                        case "pomeranians":
                            pomeranians = Integer.parseInt(split_characteristic[1].trim());
                            break;
                        case "akitas":
                            akitas = Integer.parseInt(split_characteristic[1].trim());
                            break;
                        case "vizslas":
                            vizslas = Integer.parseInt(split_characteristic[1].trim());
                            break;
                        case "goldfish":
                            goldfish = Integer.parseInt(split_characteristic[1].trim());
                            break;
                        case "trees":
                            trees = Integer.parseInt(split_characteristic[1].trim());
                            break;
                        case "cars":
                            cars = Integer.parseInt(split_characteristic[1].trim());
                            break;
                        case "perfumes":
                            parfumes = Integer.parseInt(split_characteristic[1].trim());
                            break;
                        default:
                            throw new RuntimeException("Unknown characteristic");
                    }

                }

                sues.put(sueNumber, new Characteristics(children, cats, samoyeds, pomeranians, akitas, vizslas,
                        goldfish, trees, cars, parfumes));

            });
            ;

            for (Map.Entry<Integer, Characteristics> entry : sues.entrySet()) {
                if (matchingSue(entry.getValue())) {

                    System.out.println("result: " + entry.getKey());
                }
                if (matchingSueRanges(entry.getValue())) {
                    System.out.println("result2: " + entry.getKey());
                }
            }

            long end_time = System.nanoTime();
            System.out.println("time: " + (end_time - start) / 1000000 + "ms");

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        return 0;

    }

    private boolean matchingSueRanges(Characteristics sueCharacteristics) {

        if (sueCharacteristics.children != rigthSueCharacteristics.children && sueCharacteristics.children != -1) {
            return false;
        }
        if (sueCharacteristics.cats <= rigthSueCharacteristics.cats && sueCharacteristics.cats != -1) {
            return false;
        }
        if (sueCharacteristics.samoyeds != rigthSueCharacteristics.samoyeds && sueCharacteristics.samoyeds != -1) {
            return false;
        }
        if (sueCharacteristics.pomeranians >= rigthSueCharacteristics.pomeranians
                && sueCharacteristics.pomeranians != -1) {
            return false;
        }
        if (sueCharacteristics.akitas != rigthSueCharacteristics.akitas && sueCharacteristics.akitas != -1) {
            return false;
        }
        if (sueCharacteristics.vizslas != rigthSueCharacteristics.vizslas && sueCharacteristics.vizslas != -1) {
            return false;
        }
        if (sueCharacteristics.goldfish >= rigthSueCharacteristics.goldfish && sueCharacteristics.goldfish != -1) {
            return false;
        }
        if (sueCharacteristics.trees <= rigthSueCharacteristics.trees && sueCharacteristics.trees != -1) {
            return false;
        }
        if (sueCharacteristics.cars != rigthSueCharacteristics.cars && sueCharacteristics.cars != -1) {
            return false;
        }
        if (sueCharacteristics.perfumes != rigthSueCharacteristics.perfumes && sueCharacteristics.perfumes != -1) {
            return false;
        }
        return true;

    }

    private boolean matchingSue(Characteristics sueCharacteristics) {
        if (sueCharacteristics.children != rigthSueCharacteristics.children && sueCharacteristics.children != -1) {
            return false;
        }
        if (sueCharacteristics.cats != rigthSueCharacteristics.cats && sueCharacteristics.cats != -1) {
            return false;
        }
        if (sueCharacteristics.samoyeds != rigthSueCharacteristics.samoyeds && sueCharacteristics.samoyeds != -1) {
            return false;
        }
        if (sueCharacteristics.pomeranians != rigthSueCharacteristics.pomeranians
                && sueCharacteristics.pomeranians != -1) {
            return false;
        }
        if (sueCharacteristics.akitas != rigthSueCharacteristics.akitas && sueCharacteristics.akitas != -1) {
            return false;
        }
        if (sueCharacteristics.vizslas != rigthSueCharacteristics.vizslas && sueCharacteristics.vizslas != -1) {
            return false;
        }
        if (sueCharacteristics.goldfish != rigthSueCharacteristics.goldfish && sueCharacteristics.goldfish != -1) {
            return false;
        }
        if (sueCharacteristics.trees != rigthSueCharacteristics.trees && sueCharacteristics.trees != -1) {
            return false;
        }
        if (sueCharacteristics.cars != rigthSueCharacteristics.cars && sueCharacteristics.cars != -1) {
            return false;
        }
        if (sueCharacteristics.perfumes != rigthSueCharacteristics.perfumes && sueCharacteristics.perfumes != -1) {
            return false;
        }
        return true;

    }

    /**
     * characteristics
     */
    public record Characteristics(int children, int cats, int samoyeds, int pomeranians, int akitas, int vizslas,
            int goldfish, int trees, int cars, int perfumes) {
    }

}
