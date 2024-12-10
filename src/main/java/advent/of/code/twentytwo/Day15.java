package advent.of.code.twentytwo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class Day15 {

	public BigInteger numberOfPositionsBeaconNot() throws IOException {
		try (var br = new BufferedReader(new FileReader(
				"/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/sensors.txt"));) {

			List<String> lines = br.lines().toList();

			List<Sensor> sensors = lines.stream().map(this::parseSensor).toList();

			// sensors.stream().forEach(System.out::println);

			Map<Coordinate, String> caveMap = new HashMap<>();

			sensors.forEach(s -> caveMap.put(s.getPosition(), "S"));
			sensors.forEach(s -> caveMap.put(s.getBeacon(), "B"));
			Set<Coordinate> outside = new HashSet<>();

			for (var sIndex = 0; sIndex < sensors.size(); sIndex++) {

				Sensor sensor = sensors.get(sIndex);

				var maxDist = distance(sensor.getPosition(), sensor.getBeacon());

				// topRound
				var yDist = sensor.getPosition().getY();
				for (var i = sensor.getPosition().getX() - maxDist - 1; i < sensor.getPosition().getX() + maxDist
						+ 1; i++) {

					if (i <= 4000000 && i >= 0 && yDist >= 0 && yDist <= 4000000) {

						final Coordinate current = new Coordinate(i, yDist);
						if (sensors.stream().allMatch(
								s -> distance(s.getPosition(), s.getBeacon()) < distance(s.getPosition(), current))) {
							// System.out.println(current);
							outside.add(current);
						}
					}

					if (i < sensor.getPosition().getX()) {
						yDist++;
					} else {
						yDist--;
					}

				}

				// System.out.println(outside.size());

				// bottomRound
				yDist = sensor.getPosition().getY();
				for (var i = sensor.getPosition().getX() - maxDist - 1; i < sensor.getPosition().getX() + maxDist
						+ 1; i++) {

					if (i <= 4000000 && i >= 0 && yDist >= 0 && yDist <= 4000000) {
						final Coordinate current = new Coordinate(i, yDist);
						if (sensors.stream().allMatch(
								s -> distance(s.getPosition(), s.getBeacon()) < distance(s.getPosition(), current))) {
							// System.out.println(current);
							outside.add(current);
						}
					}

					if (i < sensor.getPosition().getX()) {
						yDist--;
					} else {
						yDist++;
					}

				}
			}

			// System.out.println(outside.size());
			BigInteger result = BigInteger.valueOf(outside.stream().toList().get(0).getX())
					.multiply(BigInteger.valueOf(4000000))
					.add(BigInteger.valueOf(outside.stream().toList().get(0).getY()));

			// System.out.println(result);
			return result;

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

		private Day15 getEnclosingInstance() {
			return Day15.this;
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

		private Day15 getEnclosingInstance() {
			return Day15.this;
		}

		@Override
		public String toString() {
			return "Coordinates [x=" + x + ", y=" + y + "]";
		}

	}
}
