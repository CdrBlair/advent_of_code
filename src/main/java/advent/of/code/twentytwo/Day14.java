package advent.of.code.twentytwo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class Day14 {

	public int stepsToRest() throws IOException {
		try (var br = new BufferedReader(
				new FileReader("/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/rocks.txt"));) {

			List<String> lines = br.lines().toList();
			Map<Coordinate, String> caveMap = new HashMap<>();

			List<List<String>> pathsAsStrings = lines.stream()
					.map(l -> Arrays.asList(StringUtils.splitByWholeSeparator(l, "->"))).collect(Collectors.toList());

			List<List<Coordinate>> paths = new ArrayList<>();
			var minY = 0;
			var minX = 0;
			var maxY = 0;
			var maxX = 0;

			for (List<String> pathAsString : pathsAsStrings) {

				List<Coordinate> path = pathAsString.stream().map(c -> StringUtils.split(c, ","))
						.map(s -> new Coordinate(Integer.valueOf(s[0].trim()), Integer.valueOf(s[1].trim()))).toList();

				paths.add(path);
			}

			minX = paths.stream().map(p -> p.stream().mapToInt(c -> c.getX()).min().getAsInt())
					.mapToInt(Integer::valueOf).min().getAsInt() - 1;

			maxX = paths.stream().map(p -> p.stream().mapToInt(c -> c.getX()).max().getAsInt())
					.mapToInt(Integer::valueOf).max().getAsInt() + 1;

			maxY = paths.stream().map(p -> p.stream().mapToInt(c -> c.getY()).max().getAsInt())
					.mapToInt(Integer::valueOf).max().getAsInt() + 1;

			for (var i = minX; i < maxX + 1; i++) {
				for (var j = minY; j < maxY + 1; j++) {
					caveMap.put(new Coordinate(i, j), ".");
				}
				caveMap.put(new Coordinate(i, maxY + 1), "#");
			}

			// sand Start
			caveMap.put(new Coordinate(500, 0), "+");

			System.out.println("Min Y: " + minY + " Max Y: " + maxY + " Min X: " + minX + " Max X: " + maxX);

			for (List<Coordinate> currentPath : paths) {

				for (var i = 0; i < currentPath.size() - 1; i++) {

					Coordinate currentPos = currentPath.get(i);
					caveMap.put(currentPos, "#");
					if (currentPos.getX() == currentPath.get(i + 1).getX()) {
						var startY = 0;
						var endY = 0;
						if (currentPos.getY() > currentPath.get(i + 1).getY()) {
							startY = currentPath.get(i + 1).getY();
							endY = currentPos.getY() - 1;
						} else {
							endY = currentPath.get(i + 1).getY();
							startY = currentPos.getY() + 1;
						}

						for (var y = startY; y <= endY; y++) {
							caveMap.put(new Coordinate(currentPos.getX(), y), "#");
						}
					} else {
						var startX = 0;
						var endX = 0;
						if (currentPos.getX() > currentPath.get(i + 1).getX()) {
							startX = currentPath.get(i + 1).getX();
							endX = currentPos.getX() - 1;
						} else {
							endX = currentPath.get(i + 1).getX();
							startX = currentPos.getX() + 1;
						}

						for (var x = startX; x <= endX; x++) {
							caveMap.put(new Coordinate(x, currentPos.getY()), "#");
						}
					}

				}

			}

			boolean sandStopped = false;
			var stepCounter = 0;
			Coordinate sandPosition = new Coordinate(500, 0);

			while (!sandStopped) {
				boolean currentSandStopped = false;

				sandPosition.setX(500);
				sandPosition.setY(0);
				while (!currentSandStopped) {

					Coordinate down = new Coordinate(sandPosition.getX(), sandPosition.getY() + 1);
					Coordinate downLeft = new Coordinate(sandPosition.getX() - 1, sandPosition.getY() + 1);
					Coordinate downRight = new Coordinate(sandPosition.getX() + 1, sandPosition.getY() + 1);

					if (downRight.getX() > maxX) {
						maxX = downRight.getX();
						for (var i = minY; i < maxY + 1; i++) {
							caveMap.put(new Coordinate(maxX, i), ".");
						}
						caveMap.put(new Coordinate(maxX, maxY + 1), "#");

					}

					if (downLeft.getX() < minX) {
						minX = downLeft.getX();
						for (var i = minY; i < maxY + 1; i++) {
							caveMap.put(new Coordinate(minX, i), ".");
						}
						caveMap.put(new Coordinate(minX, maxY + 1), "#");

					}

					if (StringUtils.equals(caveMap.get(down), ".")) {
						sandPosition = down;
					} else if (StringUtils.equals(caveMap.get(downLeft), ".")) {
						sandPosition = downLeft;
					} else if (StringUtils.equals(caveMap.get(downRight), ".")) {
						sandPosition = downRight;
					} else {
						caveMap.put(sandPosition, "o");
						currentSandStopped = true;
					}

				}

				if (sandPosition.equals(new Coordinate(500, 0))) {
					stepCounter++;
					break;
				}

				stepCounter++;
			}

			for (var i = minY; i < maxY + 2; i++) {
				System.out.print(i + "  ");
				for (var j = minX; j < maxX + 1; j++) {

					System.out.print(caveMap.get(new Coordinate(j, i)));
				}
				System.out.println();
			}

			return stepCounter;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
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

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
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

		private Day14 getEnclosingInstance() {
			return Day14.this;
		}

		@Override
		public String toString() {
			return "Coordinates [x=" + x + ", y=" + y + "]";
		}

	}

}
