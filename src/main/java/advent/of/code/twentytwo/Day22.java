package advent.of.code.twentytwo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class Day22 {

	public int pathFinde() throws IOException {
		try (var br = new BufferedReader(new FileReader(
				"/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/monkeyRiddle.txt"))) {

			Map<Coordinate, String> mapJungle = new HashMap<>();
			Map<Coordinate, String> mapJunglep2 = new HashMap<>();

			// filling Upwardsmap
			Map<Coordinate, Edge> upwardsMap = new HashMap<>();
			for (var x = 1; x < 51; x++) {
				upwardsMap.put(new Coordinate(x, 101), new Edge(new Coordinate(51, x + 50), ">"));
			}

			for (var x = 51; x < 101; x++) {
				upwardsMap.put(new Coordinate(x, 1), new Edge(new Coordinate(1, x + 100), ">"));
			}

			for (var x = 101; x < 151; x++) {
				upwardsMap.put(new Coordinate(x, 1), new Edge(new Coordinate(x - 100, 200), "^"));
			}

			// filling downMap
			Map<Coordinate, Edge> downMap = new HashMap<>();
			for (var x = 1; x < 51; x++) {
				downMap.put(new Coordinate(x, 200), new Edge(new Coordinate(x + 100, 1), "v"));
			}

			for (var x = 51; x < 101; x++) {
				downMap.put(new Coordinate(x, 150), new Edge(new Coordinate(50, x + 100), "<"));
			}

			for (var x = 101; x < 151; x++) {
				downMap.put(new Coordinate(x, 50), new Edge(new Coordinate(100, x - 50), "<"));
			}

			// filling rightMap
			Map<Coordinate, Edge> rightMap = new HashMap<>();
			for (var y = 1; y < 51; y++) {
				rightMap.put(new Coordinate(150, y), new Edge(new Coordinate(100, 151 - y), "<"));
			}

			for (var y = 51; y < 101; y++) {
				rightMap.put(new Coordinate(100, y), new Edge(new Coordinate(y + 50, 50), "^"));
			}

			for (var y = 101; y < 151; y++) {
				rightMap.put(new Coordinate(100, y), new Edge(new Coordinate(150, 151 - y), "<"));
			}

			for (var y = 151; y < 201; y++) {
				rightMap.put(new Coordinate(50, y), new Edge(new Coordinate(y - 100, 150), "^"));
			}

			// filling leftMap
			Map<Coordinate, Edge> leftMap = new HashMap<>();
			for (var y = 1; y < 51; y++) {
				leftMap.put(new Coordinate(51, y), new Edge(new Coordinate(1, 151 - y), ">"));
			}

			for (var y = 51; y < 101; y++) {
				leftMap.put(new Coordinate(51, y), new Edge(new Coordinate(y - 50, 101), "v"));
			}

			for (var y = 101; y < 151; y++) {
				leftMap.put(new Coordinate(1, y), new Edge(new Coordinate(51, 151 - y), ">"));
			}

			for (var y = 151; y < 201; y++) {
				leftMap.put(new Coordinate(1, y), new Edge(new Coordinate(y - 100, 1), "v"));
			}

			List<String> linesMap = br.lines().limit(200).toList();

			// empty line can be ignored
			br.lines().limit(1).toList();
			String path = br.lines().toList().get(0);

			var maxX = linesMap.stream().mapToInt(String::length).max().getAsInt();
			var maxY = linesMap.size();

			for (var i = 1; i <= maxX; i++) {
				for (var j = 1; j <= maxY; j++) {

					mapJungle.put(new Coordinate(i, j), " ");
				}
			}

			for (var i = 1; i <= maxY; i++) {
				String currentRow = linesMap.get(i - 1);
				for (var j = 1; j <= currentRow.length(); j++) {
					String currentPoint = currentRow.charAt(j - 1) + "";
					mapJungle.put(new Coordinate(j, i), currentPoint);

				}
			}

			for (var i = 1; i <= maxX; i++) {
				for (var j = 1; j <= maxY; j++) {

					mapJunglep2.put(new Coordinate(i, j), " ");
				}
			}

			for (var i = 1; i <= maxY; i++) {
				String currentRow = linesMap.get(i - 1);
				for (var j = 1; j <= currentRow.length(); j++) {
					String currentPoint = currentRow.charAt(j - 1) + "";
					mapJunglep2.put(new Coordinate(j, i), currentPoint);

				}
			}

			path = StringUtils.replace(StringUtils.replace(path, "R", ";R:"), "L", ";L:");

			List<String> directionsAsString = new ArrayList<>(Arrays.asList(StringUtils.split(path, ";")));

			List<Direction> instructions = new ArrayList<>();

			instructions.add(new Direction(Integer.valueOf(directionsAsString.get(0)), "N"));
			directionsAsString.remove(0);

			directionsAsString.stream().map(d -> StringUtils.split(d, ":"))
					.forEach(d -> instructions.add(new Direction(Integer.valueOf(d[1]), d[0])));

			var current = new Coordinate(StringUtils.indexOf(linesMap.get(0), ".") + 1, 1);
			var currentDirection = ">";

			for (Direction instruction : instructions) {
				// Rotate
				switch (instruction.rotation) {
				case "N":
					// Do nothing at start
					break;
				case "R":
					if (currentDirection.equals(">")) {
						currentDirection = "v";
					} else if (currentDirection.equals("v")) {
						currentDirection = "<";

					} else if (currentDirection.equals("<")) {
						currentDirection = "^";
					} else if (currentDirection.equals("^")) {
						currentDirection = ">";

					}
					break;
				case "L":
					if (currentDirection.equals(">")) {
						currentDirection = "^";
					} else if (currentDirection.equals("v")) {
						currentDirection = ">";

					} else if (currentDirection.equals("<")) {
						currentDirection = "v";
					} else if (currentDirection.equals("^")) {
						currentDirection = "<";

					}
					break;

				default:
					throw new IllegalArgumentException();
				}

				for (var s = 0; s < instruction.steps; s++) {
					var touchedRock = false;
					switch (currentDirection) {
					case "<": {
						var xToCheck = current.x - 1 == 0 ? linesMap.get(current.y - 1).length() : current.x - 1;
						var possiblePlace = new Coordinate(xToCheck, current.y);
						var possibleTile = mapJungle.get(possiblePlace);

						if (possibleTile.equals(" ")) {
							xToCheck = linesMap.get(current.y - 1).length();
							possiblePlace = new Coordinate(xToCheck, current.y);
							possibleTile = mapJungle.get(possiblePlace);
						}

						if (possibleTile.equals(".") || StringUtils.contains("<>^v", possibleTile)) {
							mapJungle.put(current, currentDirection);
							current = possiblePlace;

						}
						if (possibleTile.equals("#")) {
							touchedRock = true;
						}

						break;
					}
					case ">": {
						var xToCheck = current.x + 1 > maxX
								? Math.min(StringUtils.indexOf(linesMap.get(current.y - 1), "#") + 1,
										StringUtils.indexOf(linesMap.get(current.y - 1), ".") + 1)
								: current.x + 1;
						var possiblePlace = new Coordinate(xToCheck, current.y);
						var possibleTile = mapJungle.get(possiblePlace);

						if (possibleTile.equals(" ")) {
							xToCheck = Math.min(StringUtils.indexOf(linesMap.get(current.y - 1), "#") + 1,
									StringUtils.indexOf(linesMap.get(current.y - 1), ".") + 1);
							possiblePlace = new Coordinate(xToCheck, current.y);
							possibleTile = mapJungle.get(possiblePlace);
						}

						if (possibleTile.equals(".") || StringUtils.contains("<>^v", possibleTile)) {
							mapJungle.put(current, currentDirection);
							current = possiblePlace;

						}
						if (possibleTile.equals("#")) {
							touchedRock = true;
						}
						break;
					}
					case "v": {
						var topY = 0;
						for (var y = 0; y < maxY; y++) {
							if (linesMap.get(y).length() >= current.x
									&& !((linesMap.get(y).charAt(current.x - 1) + "").equals(" "))) {
								topY = y + 1;
								break;
							}

						}

						var yToCheck = current.y + 1 > maxY ? topY : current.y + 1;
						var possiblePlace = new Coordinate(current.x, yToCheck);
						var possibleTile = mapJungle.get(possiblePlace);

						if (possibleTile.equals(" ")) {
							yToCheck = topY;
							possiblePlace = new Coordinate(current.x, yToCheck);
							possibleTile = mapJungle.get(possiblePlace);
						}

						if (possibleTile.equals(".") || StringUtils.contains("<>^v", possibleTile)) {
							mapJungle.put(current, currentDirection);
							current = possiblePlace;

						}
						if (possibleTile.equals("#")) {
							touchedRock = true;
						}
						break;
					}
					case "^": {
						var lowY = 0;
						for (var y = maxY - 1; y > 0; y--) {
							if (linesMap.get(y).length() >= current.x
									&& !((linesMap.get(y).charAt(current.x - 1) + "").equals(" "))) {
								lowY = y + 1;
								break;
							}

						}

						var yToCheck = current.y - 1 == 0 ? lowY : current.y - 1;
						var possiblePlace = new Coordinate(current.x, yToCheck);
						var possibleTile = mapJungle.get(possiblePlace);

						if (possibleTile.equals(" ")) {
							yToCheck = lowY;
							possiblePlace = new Coordinate(current.x, yToCheck);
							possibleTile = mapJungle.get(possiblePlace);
						}

						if (possibleTile.equals(".") || StringUtils.contains("<>^v", possibleTile)) {
							mapJungle.put(current, currentDirection);
							current = possiblePlace;

						}
						if (possibleTile.equals("#")) {
							touchedRock = true;
						}
						break;
					}

					default:
						throw new IllegalArgumentException("Unexpected value: " + currentDirection);
					}

					if (touchedRock) {
						break;
					}
				}

			}

			mapJungle.put(current, currentDirection);

			for (var i = 1; i <= maxY; i++) {
				for (var j = 1; j <= maxX; j++) {

					System.out.print(mapJungle.get(new Coordinate(j, i)));
				}
				System.out.println();
			}

			var valueForDir = 0;

			switch (currentDirection) {
			case "<":
				valueForDir = 2;
				break;
			case ">":
				valueForDir = 0;
				break;
			case "^":
				valueForDir = 3;
				break;
			case "v":
				valueForDir = 1;
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + currentDirection);
			}

			var result = 1000 * current.y + 4 * current.x + valueForDir;

			System.out.println("Result for P1: " + result);

			current = new Coordinate(StringUtils.indexOf(linesMap.get(0), ".") + 1, 1);
			currentDirection = ">";

			for (Direction instruction : instructions) {
				// Rotate
				switch (instruction.rotation) {
				case "N":
					// Do nothing at start
					break;
				case "R":
					if (currentDirection.equals(">")) {
						currentDirection = "v";
					} else if (currentDirection.equals("v")) {
						currentDirection = "<";

					} else if (currentDirection.equals("<")) {
						currentDirection = "^";
					} else if (currentDirection.equals("^")) {
						currentDirection = ">";

					}
					break;
				case "L":
					if (currentDirection.equals(">")) {
						currentDirection = "^";
					} else if (currentDirection.equals("v")) {
						currentDirection = ">";

					} else if (currentDirection.equals("<")) {
						currentDirection = "v";
					} else if (currentDirection.equals("^")) {
						currentDirection = "<";

					}
					break;

				default:
					throw new IllegalArgumentException();
				}

				for (var s = 0; s < instruction.steps; s++) {
					var touchedRock = false;
					switch (currentDirection) {
					case "<": {
						Coordinate possiblePlace;
						var edge = false;
						String possibleNewDirection = "";
						if (leftMap.containsKey(current)) {
							possiblePlace = leftMap.get(current).newCoord;

							edge = true;
							possibleNewDirection = leftMap.get(current).newDirection;
						} else {
							possiblePlace = new Coordinate(current.x - 1, current.y);
						}
						var possibleTile = mapJunglep2.get(possiblePlace);

						if (possibleTile.equals(".") || StringUtils.contains("<>^v", possibleTile)) {
							mapJunglep2.put(current, currentDirection);
							current = possiblePlace;
							if (edge) {
								currentDirection = possibleNewDirection;
							}

						}
						if (possibleTile.equals("#")) {
							touchedRock = true;
						}

						break;
					}
					case ">": {
						Coordinate possiblePlace;
						var edge = false;
						String possibleNewDirection = "";
						if (rightMap.containsKey(current)) {
							possiblePlace = rightMap.get(current).newCoord;

							edge = true;
							possibleNewDirection = rightMap.get(current).newDirection;
						} else {
							possiblePlace = new Coordinate(current.x + 1, current.y);
						}
						var possibleTile = mapJunglep2.get(possiblePlace);

						if (possibleTile.equals(".") || StringUtils.contains("<>^v", possibleTile)) {
							mapJunglep2.put(current, currentDirection);
							current = possiblePlace;
							if (edge) {
								currentDirection = possibleNewDirection;
							}

						}
						if (possibleTile.equals("#")) {
							touchedRock = true;
						}

						break;
					}
					case "v": {
						Coordinate possiblePlace;
						var edge = false;
						String possibleNewDirection = "";
						if (downMap.containsKey(current)) {
							possiblePlace = downMap.get(current).newCoord;

							edge = true;
							possibleNewDirection = downMap.get(current).newDirection;
						} else {
							possiblePlace = new Coordinate(current.x, current.y + 1);
						}
						var possibleTile = mapJunglep2.get(possiblePlace);

						if (possibleTile.equals(".") || StringUtils.contains("<>^v", possibleTile)) {
							mapJunglep2.put(current, currentDirection);
							current = possiblePlace;
							if (edge) {
								currentDirection = possibleNewDirection;
							}

						}
						if (possibleTile.equals("#")) {
							touchedRock = true;
						}

						break;
					}
					case "^": {
						Coordinate possiblePlace;
						var edge = false;
						String possibleNewDirection = "";
						if (upwardsMap.containsKey(current)) {
							possiblePlace = upwardsMap.get(current).newCoord;

							edge = true;
							possibleNewDirection = upwardsMap.get(current).newDirection;
						} else {
							possiblePlace = new Coordinate(current.x, current.y - 1);
						}
						var possibleTile = mapJunglep2.get(possiblePlace);

						if (possibleTile.equals(".") || StringUtils.contains("<>^v", possibleTile)) {
							mapJunglep2.put(current, currentDirection);
							current = possiblePlace;
							if (edge) {
								currentDirection = possibleNewDirection;
							}

						}
						if (possibleTile.equals("#")) {
							touchedRock = true;
						}

						break;
					}

					default:
						throw new IllegalArgumentException("Unexpected value: " + currentDirection);
					}

					if (touchedRock) {
						break;
					}

//					for (var i = 1; i <= maxY; i++) {
//						for (var j = 1; j <= maxX; j++) {
//
//							System.out.print(mapJunglep2.get(new Coordinate(j, i)));
//						}
//						System.out.println();
//
//					}
//					System.out.println();
				}

			}

			mapJunglep2.put(current, currentDirection);

			for (var i = 1; i <= maxY; i++) {
				for (var j = 1; j <= maxX; j++) {

					System.out.print(mapJunglep2.get(new Coordinate(j, i)));
				}
				System.out.println();
			}

			valueForDir = 0;

			switch (currentDirection) {
			case "<":
				valueForDir = 2;
				break;
			case ">":
				valueForDir = 0;
				break;
			case "^":
				valueForDir = 3;
				break;
			case "v":
				valueForDir = 1;
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + currentDirection);
			}

			var resultP2 = 1000 * current.y + 4 * current.x + valueForDir;

			System.out.println("Result for P2: " + resultP2);

			return 0;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	record Direction(int steps, String rotation) {

	}

	record Coordinate(int x, int y) {

	}

	record Edge(Coordinate newCoord, String newDirection) {

	}

}
