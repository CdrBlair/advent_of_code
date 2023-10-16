package advent.of.code.twentytwo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Day23 {

	public int getSquareSizeOfGrove() throws IOException {
		long start = System.currentTimeMillis();

		try (var br = new BufferedReader(new FileReader(
				"/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/groveMap.txt"))) {

			List<String> linesOfMap = br.lines().toList();

			var minY = 0;
			var minX = 0;
			var maxY = linesOfMap.size() - 1;
			var maxX = linesOfMap.get(0).length() - 1;
			var numberOfRounds = 0;

			Map<Coordinate, String> groveMap = new HashMap<>();
			List<Elve> elves = new ArrayList<>();
			List<String> orderOfMovement = new ArrayList<>();
			orderOfMovement.add("N");
			orderOfMovement.add("S");
			orderOfMovement.add("W");
			orderOfMovement.add("E");

			for (var i = 0; i <= maxY; i++) {
				String currentRow = linesOfMap.get(i);
				for (var j = 0; j <= maxX; j++) {
					groveMap.put(new Coordinate(j, i), currentRow.charAt(j) + "");
				}
			}

			groveMap.entrySet().stream().forEach(e -> {
				if (e.getValue().equals("#")) {
					elves.add(new Elve(e.getKey()));
				}

			});
			var oneMoved = true;
			while (oneMoved) {
				numberOfRounds++;

				// First Half
				for (Elve current : elves) {

					// Checking adjectant fields
					// UP
					if (current.currentPos.y - 1 < minY) {
						minY = current.currentPos.y - 1;
						for (var x = minX; x <= maxX; x++) {
							groveMap.put(new Coordinate(x, minY), ".");
						}

					}
					if (!groveMap.get(new Coordinate(current.currentPos.x, current.currentPos.y - 1)).equals(".")) {
						current.wantsToMove = true;
					}

					// DOWN
					if (current.currentPos.y + 1 > maxY) {
						maxY = current.currentPos.y + 1;
						for (var x = minX; x <= maxX; x++) {
							groveMap.put(new Coordinate(x, maxY), ".");
						}

					}
					if (!groveMap.get(new Coordinate(current.currentPos.x, current.currentPos.y + 1)).equals(".")) {
						current.wantsToMove = true;
					}

					// RIGHT
					if (current.currentPos.x + 1 > maxX) {
						maxX = current.currentPos.x + 1;
						for (var y = minY; y <= maxY; y++) {
							groveMap.put(new Coordinate(maxX, y), ".");
						}

					}
					if (!groveMap.get(new Coordinate(current.currentPos.x + 1, current.currentPos.y)).equals(".")) {
						current.wantsToMove = true;
					}

					// LEFT
					if (current.currentPos.x - 1 < minX) {
						minX = current.currentPos.x - 1;
						for (var y = minY; y <= maxY; y++) {
							groveMap.put(new Coordinate(minX, y), ".");
						}

					}
					if (!groveMap.get(new Coordinate(current.currentPos.x - 1, current.currentPos.y)).equals(".")) {
						current.wantsToMove = true;
					}

					// UPRIGHT
					if (!groveMap.get(new Coordinate(current.currentPos.x + 1, current.currentPos.y - 1)).equals(".")) {
						current.wantsToMove = true;
					}
					// UPLEFT
					if (!groveMap.get(new Coordinate(current.currentPos.x - 1, current.currentPos.y - 1)).equals(".")) {
						current.wantsToMove = true;
					}
					// DOWNRIGHT
					if (!groveMap.get(new Coordinate(current.currentPos.x + 1, current.currentPos.y + 1)).equals(".")) {
						current.wantsToMove = true;
					}
					// DOWNLEFT
					if (!groveMap.get(new Coordinate(current.currentPos.x - 1, current.currentPos.y + 1)).equals(".")) {
						current.wantsToMove = true;
					}

					if (current.wantsToMove) {
						var foundPossiblePos = false;
						for (String direction : orderOfMovement) {
							switch (direction) {
							case "N":
								if (groveMap.get(new Coordinate(current.currentPos.x, current.currentPos.y - 1))
										.equals(".")
										&& groveMap
												.get(new Coordinate(current.currentPos.x + 1, current.currentPos.y - 1))
												.equals(".")
										&& groveMap
												.get(new Coordinate(current.currentPos.x - 1, current.currentPos.y - 1))
												.equals(".")) {
									current.possibleNew = new Coordinate(current.currentPos.x,
											current.currentPos.y - 1);
									foundPossiblePos = true;

								}
								break;
							case "S":
								if (groveMap.get(new Coordinate(current.currentPos.x, current.currentPos.y + 1))
										.equals(".")
										&& groveMap
												.get(new Coordinate(current.currentPos.x + 1, current.currentPos.y + 1))
												.equals(".")
										&& groveMap
												.get(new Coordinate(current.currentPos.x - 1, current.currentPos.y + 1))
												.equals(".")) {

									current.possibleNew = new Coordinate(current.currentPos.x,
											current.currentPos.y + 1);
									foundPossiblePos = true;

								}
								break;
							case "W":
								if (groveMap.get(new Coordinate(current.currentPos.x - 1, current.currentPos.y))
										.equals(".")
										&& groveMap
												.get(new Coordinate(current.currentPos.x - 1, current.currentPos.y - 1))
												.equals(".")
										&& groveMap
												.get(new Coordinate(current.currentPos.x - 1, current.currentPos.y + 1))
												.equals(".")) {

									current.possibleNew = new Coordinate(current.currentPos.x - 1,
											current.currentPos.y);
									foundPossiblePos = true;

								}
								break;
							case "E":
								if (groveMap.get(new Coordinate(current.currentPos.x + 1, current.currentPos.y))
										.equals(".")
										&& groveMap
												.get(new Coordinate(current.currentPos.x + 1, current.currentPos.y - 1))
												.equals(".")
										&& groveMap
												.get(new Coordinate(current.currentPos.x + 1, current.currentPos.y + 1))
												.equals(".")) {

									current.possibleNew = new Coordinate(current.currentPos.x + 1,
											current.currentPos.y);
									foundPossiblePos = true;

								}
								break;
							default:
								throw new IllegalArgumentException("Unexpected value: " + direction);
							}

							if (foundPossiblePos) {
								break;
							}
						}

						if (current.wantsToMove && current.possibleNew == null) {
							current.wantsToMove = false;
						}
					}

				}
				// second half
				for (Elve current : elves) {
					if (current.wantsToMove) {
						if (elves.stream().filter(e -> current.possibleNew.equals(e.possibleNew)).count() == 1) {
							groveMap.put(current.currentPos, ".");
							groveMap.put(current.possibleNew, "#");
							current.currentPos = current.possibleNew;
							current.possibleNew = null;
							current.wantsToMove = false;
							current.moved = true;
						}
					}

				}
				oneMoved = elves.stream().anyMatch(e -> e.moved);

				elves.stream().forEach(e -> {
					e.possibleNew = null;
					e.wantsToMove = false;
					e.moved = false;
				});

				orderOfMovement.add(orderOfMovement.get(0));
				orderOfMovement.remove(0);
			}

			for (var i = minY; i <= maxY; i++) {
				for (var j = minX; j <= maxX; j++) {
					System.out.print(groveMap.get(new Coordinate(j, i)));
				}
				System.out.println();
			}

			var rectMinY = elves.stream().mapToInt(e -> e.currentPos.y).min().getAsInt();
			var rectMaxY = elves.stream().mapToInt(e -> e.currentPos.y).max().getAsInt();
			var rectMinX = elves.stream().mapToInt(e -> e.currentPos.x).min().getAsInt();
			var rectMaxX = elves.stream().mapToInt(e -> e.currentPos.x).max().getAsInt();

			var sumOfFreeTiles = 0;

			for (var i = rectMinY; i <= rectMaxY; i++) {
				for (var j = rectMinX; j <= rectMaxX; j++) {
					if (groveMap.get(new Coordinate(j, i)).equals(".")) {
						sumOfFreeTiles++;
					}
				}
			}
			System.out.println("Part 1 Freetiles: " + sumOfFreeTiles);
			System.out.println("Part 2 Rounds: " + numberOfRounds);
			long end = System.currentTimeMillis();
			System.out.println("Took" + (end - start) + " ms");
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}

	}

	record Coordinate(int x, int y) {

	}

	private class Elve {
		private Coordinate currentPos;
		private Coordinate possibleNew;
		private boolean wantsToMove;
		private boolean moved;

		public Elve(Coordinate currentPos) {
			this.currentPos = currentPos;
			this.possibleNew = null;
			this.wantsToMove = false;
			this.moved = false;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + Objects.hash(currentPos, moved, possibleNew, wantsToMove);
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
			Elve other = (Elve) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			return Objects.equals(currentPos, other.currentPos) && moved == other.moved
					&& Objects.equals(possibleNew, other.possibleNew) && wantsToMove == other.wantsToMove;
		}

		private Day23 getEnclosingInstance() {
			return Day23.this;
		}

		@Override
		public String toString() {
			return "Elve [currentPos=" + currentPos + ", possibleNew=" + possibleNew + ", wantsToMove=" + wantsToMove
					+ ", moved=" + moved + "]";
		}

	}
}
