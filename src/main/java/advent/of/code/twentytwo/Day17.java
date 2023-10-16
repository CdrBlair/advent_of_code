package advent.of.code.twentytwo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Day17 {

	public int maxStepsUntilStop() throws IOException {
		long start = System.currentTimeMillis();
		try (var br = new BufferedReader(
				new FileReader("/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/jets.txt"));) {

			List<String> lines = br.lines().toList();

			List<String> jets = lines.get(0).chars().mapToObj(c -> String.valueOf((char) c)).toList();

			Map<Coordinate, String> playGround = new HashMap<>();

			playGround.put(new Coordinate(0, 0), "+");
			playGround.put(new Coordinate(8, 0), "+");

			var leftBorderX = 0;
			var rightBorderX = 8;
			var lowestTopY = 0;
			var currentStackHigh = 5;

			for (var i = lowestTopY + 1; i < rightBorderX; i++) {
				playGround.put(new Coordinate(i, 0), "-");
			}

			for (var i = lowestTopY + 1; i < 5; i++) {
				playGround.put(new Coordinate(0, i), "|");
				playGround.put(new Coordinate(8, i), "|");

				for (var j = leftBorderX + 1; j < rightBorderX; j++) {
					playGround.put(new Coordinate(j, i), ".");
				}
			}

			List<Shape> shapes = constructShapes();

			var currentBottomLeft = new Coordinate(3, lowestTopY + 4);
			var currentJet = 0;
			var jetMod = jets.size();
			var maxRocks = 1000000000000l;
			var currentRock = 0;
			var rockMod = shapes.size();
			var totalHeigth = 0;
			var rockcounter = 1l;
			var gainedHeight = 0l;
			var cycle = new HashSet<>();
			Map<State, Integer> cycleHeight = new HashMap<>();
			Map<State, Long> cycleCount = new HashMap<>();

			while (rockcounter < maxRocks + 1) {
				Shape currenShape = shapes.get(currentRock);
				// makehight
				for (var i = 0; i < 4; i++) {
					if (i + currentBottomLeft.y > currentStackHigh - 1) {
						playGround.put(new Coordinate(0, i + currentBottomLeft.y), "|");
						playGround.put(new Coordinate(8, i + currentBottomLeft.y), "|");

						for (var j = leftBorderX + 1; j < rightBorderX; j++) {
							playGround.put(new Coordinate(j, i + currentBottomLeft.y), ".");
						}
						currentStackHigh++;
					}
				}

				boolean shapeStopped = false;
				while (!shapeStopped) {

					switch (jets.get(currentJet)) {
					case "<": {
						if (currentBottomLeft.x - 1 == 0) {

						} else {

							var move = true;
							for (var i = 0; i < 4; i++) {
								for (var j = 0; j < 4; j++) {

									Coordinate collidePoint = new Coordinate(j + currentBottomLeft.x - 1,
											i + currentBottomLeft.y);
									if (currenShape.getRocks().get(new Coordinate(j, i))
											&& !playGround.get(collidePoint).equals(".")) {
										move = false;

									}
								}

							}
							if (move) {
								currentBottomLeft = new Coordinate(currentBottomLeft.x - 1, currentBottomLeft.y);
							}
						}
						break;
					}
					case ">": {
						if (currentBottomLeft.x + currenShape.outterX + 1 == 8) {

						} else {

							var move = true;
							for (var i = 0; i < 4; i++) {
								for (var j = 0; j < 4; j++) {

									Coordinate collidePoint = new Coordinate(j + currentBottomLeft.x + 1,
											i + currentBottomLeft.y);
									if (currenShape.getRocks().get(new Coordinate(j, i))
											&& !playGround.get(collidePoint).equals(".")) {
										move = false;

									}
								}

							}
							if (move) {
								currentBottomLeft = new Coordinate(currentBottomLeft.x + 1, currentBottomLeft.y);
							}
						}
						break;
					}
					default:
						break;
					}
					currentJet = (currentJet + 1) % jetMod;

					if (currentBottomLeft.y - 1 == 0) {
						shapeStopped = true;
					} else {

						for (var i = 0; i < 4; i++) {
							for (var j = 0; j < 4; j++) {

								Coordinate collidePoint = new Coordinate(j + currentBottomLeft.x,
										i + currentBottomLeft.y - 1);
								if (currenShape.getRocks().get(new Coordinate(j, i))
										&& !playGround.get(collidePoint).equals(".")) {
									shapeStopped = true;

								}
							}

						}
						if (!shapeStopped) {
							currentBottomLeft = new Coordinate(currentBottomLeft.x, currentBottomLeft.y - 1);
						}

					}

//					for (var i = 0; i < 4; i++) {
//						for (var j = 0; j < 4; j++) {
//
//							if (currenShape.getRocks().get(new Coordinate(j, i))) {
//								playGround.put(new Coordinate(j + currentBottomLeft.x, i + currentBottomLeft.y), "@");
//							}
//						}
//					}
//
//					System.out.println(jets.get(currentJet - 1 >= 0 ? currentJet - 1 : 0));
//					for (var i = currentStackHigh - 1; i >= 0; i--) {
//						for (var j = leftBorderX; j <= rightBorderX; j++) {
//							System.out.print(playGround.get(new Coordinate(j, i)));
//						}
//						System.out.println();
//					}
//					playGround = playGround.entrySet().stream().map(v -> {
//						if (v.getValue().equals("@")) {
//							v.setValue(".");
//						}
//						return v;
//					}).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

				}

				for (var i = 0; i < 4; i++) {
					for (var j = 0; j < 4; j++) {

						if (currenShape.getRocks().get(new Coordinate(j, i))) {
							playGround.put(new Coordinate(j + currentBottomLeft.x, i + currentBottomLeft.y), "#");
						}
					}
				}

				List<String> ground = new ArrayList<>();

				for (var i = 1; i < 8; i++) {
					ground.add(playGround.get(new Coordinate(i, totalHeigth)));
				}

				if (cycle.contains(new State(currentJet, currenShape, ground))) {
					var deltaRocks = rockcounter - cycleCount.get(new State(currentJet, currenShape, ground));
					var deltaHeight = totalHeigth - cycleHeight.get(new State(currentJet, currenShape, ground));

					var comingCycles = (maxRocks - rockcounter) / deltaRocks;

					gainedHeight = gainedHeight + comingCycles * deltaHeight;

					rockcounter = rockcounter + comingCycles * deltaRocks;
					System.out.println(gainedHeight);

				}
				if (rockcounter == 287) {
					cycle.add(new State(currentJet, currenShape, ground));
					cycleHeight.put(new State(currentJet, currenShape, ground), totalHeigth);
					cycleCount.put(new State(currentJet, currenShape, ground), rockcounter);
				}
				currentRock = (currentRock + 1) % rockMod;

				if (currentBottomLeft.y + currenShape.heigth > totalHeigth) {
					totalHeigth = currentBottomLeft.y + currenShape.heigth - 1;
				}
				currentBottomLeft = new Coordinate(3, totalHeigth + 4);
				rockcounter++;

			}

//			for (var i = currentStackHigh - 1; i >= 0; i--) {
//				for (var j = leftBorderX; j <= rightBorderX; j++) {
//					System.out.print(playGround.get(new Coordinate(j, i)));
//				}
//				System.out.println();
//			}

			System.out.println(totalHeigth + gainedHeight);
			long end = System.currentTimeMillis();
			System.out.println("Took" + (end - start) + " ms");
			return 0;
		} catch (

		IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	record State(int jet, Shape shape, List<String> ground) {

	}

	private List<Shape> constructShapes() {

		List<Shape> shapes = new ArrayList<>();

		Map<Coordinate, Boolean> currentMap = new HashMap<>();

		// horizontal line
		for (var i = 0; i < 4; i++) {
			currentMap.put(new Coordinate(i, 0), true);
		}

		for (var i = 1; i < 4; i++) {
			for (var j = 0; j < 4; j++) {
				currentMap.put(new Coordinate(j, i), false);
			}
		}

		Shape horizontal = new Shape(currentMap, 3, 1);
		shapes.add(horizontal);

		// cross line
		currentMap = new HashMap<>();

		// rocks
		currentMap.put(new Coordinate(1, 0), true);
		currentMap.put(new Coordinate(0, 1), true);
		currentMap.put(new Coordinate(1, 1), true);
		currentMap.put(new Coordinate(2, 1), true);
		currentMap.put(new Coordinate(1, 2), true);

		// air
		for (var i = 0; i < 4; i++) {
			for (var j = 0; j < 4; j++) {

				Coordinate currentCoord = new Coordinate(j, i);
				if (!Boolean.TRUE.equals(currentMap.get(currentCoord))) {

					currentMap.put(currentCoord, false);
				}
			}
		}

		Shape cross = new Shape(currentMap, 2, 3);

		shapes.add(cross);

		// L line
		currentMap = new HashMap<>();

		// rocks
		currentMap.put(new Coordinate(0, 0), true);
		currentMap.put(new Coordinate(1, 0), true);
		currentMap.put(new Coordinate(2, 0), true);
		currentMap.put(new Coordinate(2, 1), true);
		currentMap.put(new Coordinate(2, 2), true);

		// air
		for (var i = 0; i < 4; i++) {
			for (var j = 0; j < 4; j++) {

				Coordinate currentCoord = new Coordinate(j, i);
				if (!Boolean.TRUE.equals(currentMap.get(currentCoord))) {

					currentMap.put(currentCoord, false);
				}
			}
		}
		Shape l = new Shape(currentMap, 2, 3);
		shapes.add(l);

		// vertical line
		currentMap = new HashMap<>();

		// rocks
		currentMap.put(new Coordinate(0, 0), true);
		currentMap.put(new Coordinate(0, 1), true);
		currentMap.put(new Coordinate(0, 2), true);
		currentMap.put(new Coordinate(0, 3), true);

		// air
		for (var i = 0; i < 4; i++) {
			for (var j = 0; j < 4; j++) {

				Coordinate currentCoord = new Coordinate(j, i);
				if (!Boolean.TRUE.equals(currentMap.get(currentCoord))) {

					currentMap.put(currentCoord, false);
				}
			}
		}
		Shape vertical = new Shape(currentMap, 0, 4);
		shapes.add(vertical);

		// block line
		currentMap = new HashMap<>();

		// rocks
		currentMap.put(new Coordinate(0, 0), true);
		currentMap.put(new Coordinate(1, 0), true);
		currentMap.put(new Coordinate(0, 1), true);
		currentMap.put(new Coordinate(1, 1), true);

		// air
		for (var i = 0; i < 4; i++) {
			for (var j = 0; j < 4; j++) {

				Coordinate currentCoord = new Coordinate(j, i);
				if (!Boolean.TRUE.equals(currentMap.get(currentCoord))) {

					currentMap.put(currentCoord, false);
				}
			}
		}
		Shape block = new Shape(currentMap, 1, 2);
		shapes.add(block);

		return shapes;

	}

	private class Shape {

		private Map<Coordinate, Boolean> rocks;
		private int outterX;
		private int heigth;

		public Shape(Map<Coordinate, Boolean> rocks, int outterX, int height) {
			super();
			this.rocks = rocks;
			this.outterX = outterX;
			this.heigth = height;
		}

		public Map<Coordinate, Boolean> getRocks() {
			return rocks;
		}

		public void setRocks(Map<Coordinate, Boolean> rocks) {
			this.rocks = rocks;
		}

		public int getOutterX() {
			return outterX;
		}

		public void setOutterX(int outterX) {
			this.outterX = outterX;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();

			for (var i = 3; i >= 0; i--) {
				for (var j = 0; j < 4; j++) {
					sb.append(rocks.get(new Coordinate(j, i))).append(" ");
				}
				sb.append("\r\n");
			}
			return sb.toString();

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

		private Day17 getEnclosingInstance() {
			return Day17.this;
		}

		@Override
		public String toString() {
			return "Coordinates [x=" + x + ", y=" + y + "]";
		}

	}
}
