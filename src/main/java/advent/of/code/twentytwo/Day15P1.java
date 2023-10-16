package advent.of.code.twentytwo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

public class Day15P1 {

	public int numberOfPositionsBeaconNot() throws IOException {
		try (var br = new BufferedReader(new FileReader(
				"/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/sensors.txt"));) {

			List<String> lines = br.lines().toList();

			List<Sensor> sensors = lines.stream().map(this::parseSensor).toList();

			sensors.stream().forEach(System.out::println);

			Map<Coordinate, String> caveMap = new HashMap<>();
			var counter = 0;

			var lineNumber = 2000000;

			sensors.forEach(s -> caveMap.put(s.getPosition(), "S"));
			sensors.forEach(s -> caveMap.put(s.getBeacon(), "B"));

			List<Sensor> sensorsInRange = sensors.stream().filter(s -> distance(s.getPosition(),
					s.getBeacon()) >= distance(s.getPosition(), new Coordinate(s.getPosition().getX(), lineNumber)))
					.toList();

			System.out.println(sensorsInRange);

			var maxX = sensorsInRange.stream()
					.mapToInt(s -> s.getPosition().getX() + distance(s.getPosition(), s.getBeacon())).max().getAsInt();

			var minX = sensorsInRange.stream()
					.mapToInt(s -> s.getPosition().getX() - distance(s.getPosition(), s.getBeacon())).min().getAsInt();

			var minY = sensorsInRange.stream().mapToInt(s -> s.getPosition().getY()).min().getAsInt();

			var maxY = sensorsInRange.stream().mapToInt(s -> s.getPosition().getX()).max().getAsInt();

			System.out.println("minX: " + minX + " maxX: " + maxX + " minY: " + minY + " maxY: " + maxY);

			for (var i = minX; i <= maxX; i++) {
				Coordinate currentCoordinate = new Coordinate(i, lineNumber);
				if (sensorsInRange.stream().anyMatch(
						s -> distance(s.getPosition(), currentCoordinate) <= distance(s.getPosition(), s.getBeacon()))
						&& !caveMap.containsKey(currentCoordinate)) {

					counter++;
				}
			}

			return counter;

		} catch (

		IOException e) {
			e.printStackTrace();

			throw e;
		}

	}

	private int distance(Coordinate sensor, Coordinate beacon) {

		return Math.abs(sensor.getX() - beacon.getX()) + Math.abs(sensor.getY() - beacon.getY());

	}

	private Sensor parseSensor(String line) {

		String[] splittedLine = StringUtils.split(line, " ");

		var xPos = Integer.valueOf(StringUtils.substringAfter(StringUtils.remove(splittedLine[2], ","), "="));
		var yPos = Integer.valueOf(StringUtils.substringAfter(StringUtils.remove(splittedLine[3], ":"), "="));
		var beaconXPos = Integer.valueOf(StringUtils.substringAfter(StringUtils.remove(splittedLine[8], ","), "="));
		var beaconYPos = Integer.valueOf(StringUtils.substringAfter(splittedLine[9], "="));

		return new Sensor(new Coordinate(xPos, yPos), new Coordinate(beaconXPos, beaconYPos));

	}

	private class Sensor {
		Coordinate position;
		Coordinate beacon;

		public Sensor(Coordinate position, Coordinate beacon) {
			super();
			this.position = position;
			this.beacon = beacon;
		}

		public Coordinate getPosition() {
			return position;
		}

		public Coordinate getBeacon() {
			return beacon;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + Objects.hash(beacon, position);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Sensor other = (Sensor) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			return Objects.equals(beacon, other.beacon) && Objects.equals(position, other.position);
		}

		private Day15P1 getEnclosingInstance() {
			return Day15P1.this;
		}

		@Override
		public String toString() {
			return "Sensor [position=" + position + ", beacon=" + beacon + "]";
		}

	}

	private class Coordinate {
		int x;
		int y;

		public Coordinate(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + Objects.hash(x, y);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Coordinate other = (Coordinate) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			return x == other.x && y == other.y;
		}

		private Day15P1 getEnclosingInstance() {
			return Day15P1.this;
		}

		@Override
		public String toString() {
			return "Coordinates [x=" + x + ", y=" + y + "]";
		}

	}
}
