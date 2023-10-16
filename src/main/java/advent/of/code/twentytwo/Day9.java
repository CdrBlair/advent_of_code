package advent.of.code.twentytwo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;

public class Day9 {

	public int numberOfVisitedPositions() throws IOException {
		try (var br = new BufferedReader(new FileReader(
				"/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/ropeDirections.txt"));) {

			Set<Position> visitedPosition = new HashSet<>();
			// add start Position
			visitedPosition.add(new Position(0, 0));

//			Position head = new Position(0, 0);
//			Position tail = new Position(0, 0);
//			List<Position> rope = Arrays.asList(head, tail);

			List<Position> rope = new ArrayList<>();
			IntStream.range(0, 10).forEach(i -> rope.add(new Position(0, 0)));

			List<String> directions = br.lines().toList();
			System.out.println(rope);

			for (String direction : directions) {
				String[] directionArray = StringUtils.split(direction, " ");

				for (int i = 0; i < Integer.valueOf(directionArray[1]); i++) {
					rope.set(0, moveHead(directionArray[0], rope.get(0)));
					for (int j = 1; j < rope.size(); j++) {

						if (Math.abs(rope.get(j).getX() - rope.get(j - 1).getX()) <= 1
								&& Math.abs(rope.get(j).getY() - rope.get(j - 1).getY()) <= 1) {
						} else if (Math.abs(rope.get(j).getX() - rope.get(j - 1).getX())
								+ Math.abs(rope.get(j).getY() - rope.get(j - 1).getY()) <= 2) {

							rope.set(j, moveTailStraight(rope.get(j - 1), rope.get(j)));

						} else {

							rope.set(j, moveTailDiagonal(rope.get(j - 1), rope.get(j)));
						}

					}
					System.out.println(rope.get(rope.size() - 1));

					visitedPosition.add(rope.get(rope.size() - 1));
				}
			}

			System.out.println(visitedPosition);
			return visitedPosition.size();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}

	}

	private Position moveTailDiagonal(Position head, Position tail) {

		int yPos;
		int xPos;

		if (head.getY() - tail.getY() > 0) {
			yPos = tail.getY() + 1;
		} else {
			yPos = tail.getY() - 1;
		}

		if (head.getX() - tail.getX() > 0) {
			xPos = tail.getX() + 1;
		} else {
			xPos = tail.getX() - 1;
		}

		return new Position(xPos, yPos);
	}

	private Position moveTailStraight(Position head, Position tail) {
		Position newPosition;

		if (head.getX() == tail.getX()) {
			if (head.getY() - tail.getY() > 0) {
				newPosition = new Position(tail.getX(), tail.getY() + 1);
			} else {
				newPosition = new Position(tail.getX(), tail.getY() - 1);
			}
		} else {
			if (head.getX() - tail.getX() > 0) {
				newPosition = new Position(tail.getX() + 1, tail.getY());
			} else {
				newPosition = new Position(tail.getX() - 1, tail.getY());
			}
		}

		return newPosition;

	}

	private Position moveHead(String direction, Position current) {

		Position newPosition;

		switch (direction) {
		case "U":
			newPosition = new Position(current.getX(), current.getY() + 1);
			break;
		case "D":
			newPosition = new Position(current.getX(), current.getY() - 1);
			break;
		case "L":
			newPosition = new Position(current.getX() - 1, current.getY());
			break;
		case "R":
			newPosition = new Position(current.getX() + 1, current.getY());
			break;
		default:
			newPosition = current;
			break;

		}
		return newPosition;
	}

	private class Position {
		private int x;
		private int y;

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
			Position other = (Position) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			return x == other.x && y == other.y;
		}

		private Day9 getEnclosingInstance() {
			return Day9.this;
		}

		@Override
		public String toString() {
			return "Position [x=" + x + ", y=" + y + "]";
		}

		public Position(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}

	}

}
