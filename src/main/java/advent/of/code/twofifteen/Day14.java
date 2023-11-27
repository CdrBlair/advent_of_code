package advent.of.code.twofifteen;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Day14 {

    public int travelDistanceWinner() throws IOException {
        var result = 0;
        var racetime = 2503;
        try (var br = new BufferedReader(new FileReader(
                "/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/reindeers.txt"));) {

            List<Reindeer> reindeers = br.lines().map(l -> {
                var split = l.split(" ");
                return new Reindeer(split[0], Integer.parseInt(split[3]), Integer.parseInt(split[6]), Integer.parseInt(split[13]), 0, 0);
            }).toList();

            result = reindeers.stream().mapToInt(r -> r.getDistance(racetime)).max().orElse(-1);


            for (var i = 1; i <= racetime; i++) {

                var finalI = i;
                System.out.println("Seconds : " + i);
                reindeers = reindeers.stream().map(r -> r.addDistance(r.getDistance(finalI))).toList();
                var maxDis = reindeers.stream().mapToInt(r -> r.distance).max().orElse(-1);
                reindeers = reindeers.stream().map(r -> r.addPoints(r.distance == maxDis ? 1 : 0)).toList();
            }

            System.out.println("Most points: " + reindeers.stream().mapToInt(r -> r.points).max().orElse(-1));


        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }


        return result;
    }


    record Reindeer(String name, int speed, int flyTime, int restTime, int points, int distance) {


        public Reindeer addPoints(int points) {
            System.out.println();
            return new Reindeer(name, speed, flyTime, restTime, this.points + points, distance);
        }

        public Reindeer addDistance(int distance) {
            return new Reindeer(name, speed, flyTime, restTime, points, distance);
        }


        public int getDistance(int time) {
            var result = 0;

            var fullCycles = time / (flyTime + restTime);
            var restTimeLeft = time % (flyTime + restTime);

            result = fullCycles * speed * flyTime;

            if (restTimeLeft > flyTime) {
                result += speed * flyTime;
            } else {
                result += speed * restTimeLeft;
            }

            return result;
        }
    }
}


