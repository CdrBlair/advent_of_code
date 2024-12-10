package advent.of.code.twentytwo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

public class Day12 {

	public int minSteps() throws IOException {

		try (var br = new BufferedReader(
				new FileReader("/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/map.txt"));) {

			List<String> lines = br.lines().toList();
			List<List<Integer>> map = new ArrayList<>();
			Node startPos = new Node(-1, -1);
			Node targetPos = new Node(-1, -1);
			List<Integer> lengthOfPathsFromA = new ArrayList<>();
			List<Node> startingNodes = new ArrayList<>();

			var maxY = lines.size();
			var maxX = lines.get(0).length();

			for (var i = 0; i < lines.size(); i++) {

				String currentRow = lines.get(i);
				List<Integer> currentMappedRow = new ArrayList<>();

				for (var j = 0; j < currentRow.length(); j++) {

					String currentPoint = currentRow.substring(j, j + 1);
					if (currentPoint.equals("S")) {

						startPos = new Node(j, i);
						startingNodes.add(startPos);

					} else if (currentPoint.equals("E")) {
						targetPos = new Node(j, i);
					}

					currentMappedRow.add(mapToHeight(currentPoint));
					if (mapToHeight(currentPoint) == 1) {
						startingNodes.add(new Node(j, i));
					}

				}

				map.add(currentMappedRow);
			}

			// for (List<Integer> row : map) {
			// for (Integer point : row) {
			// System.out.print(point);
			// System.out.print(" ");
			// }
			// System.out.println();

			// }

			if (startPos.getxPos() == -1 || targetPos.getxPos() == -1) {
				throw new IllegalStateException();
			}

			Map<Integer, Node> lengthMap = new HashMap<>();

			for (Node node : startingNodes) {
				lengthOfPathsFromA.add(shortestToTarget(node, targetPos, maxX, maxY, map));
				lengthMap.put(lengthOfPathsFromA.get(lengthOfPathsFromA.size() - 1), targetPos);
			}

			var minLength = lengthOfPathsFromA.stream().filter(i -> i > 0).mapToInt(Integer::intValue).min().getAsInt();

			// List<Node> way = getWay(lengthMap.get(minLength));

			// AtomicInteger count = new AtomicInteger(0);

			// way.stream().forEach(n -> System.out.println("Step" + count.getAndIncrement()
			// + " : " + n));

			return minLength;

		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}

	}

	// private List<Node> getWay(Node target) {

	// var lastNode = false;
	// List<Node> way = new ArrayList<>();
	// Node current = target;

	// while (!lastNode) {

	// way.add(current);

	// if (current.getParent() == null) {
	// lastNode = true;
	// } else {
	// current = current.getParent();
	// }

	// }
	// Collections.reverse(way);

	// return way;

	// }

	private int shortestToTarget(Node startPos, Node targetPos, int maxX, int maxY, List<List<Integer>> map) {
		Queue<Node> nodeQueue = new ArrayDeque<>();
		Set<Node> visited = new HashSet<>();
		nodeQueue.add(startPos);
		visited.add(startPos);
		boolean reached = false;
		var steps = 0;

		while (!reached) {
			if (nodeQueue.isEmpty()) {
				// no path to target
				steps = 0;
				break;
			}
			Node currentNode = nodeQueue.poll();

			var heightPos = map.get(currentNode.getyPos()).get(currentNode.getxPos());

			List<Node> sourroundingNodes = new ArrayList<>();
			if (currentNode.getxPos() - 1 >= 0) {
				Node left = new Node(currentNode.getxPos() - 1, currentNode.getyPos(), currentNode.currentSteps + 1);
				left.setParent(currentNode);
				sourroundingNodes.add(left);
			}
			if (currentNode.getxPos() + 1 < maxX) {
				Node right = new Node(currentNode.getxPos() + 1, currentNode.getyPos(), currentNode.currentSteps + 1);
				right.setParent(currentNode);
				sourroundingNodes.add(right);
			}

			if (currentNode.getyPos() - 1 >= 0) {
				Node up = new Node(currentNode.getxPos(), currentNode.getyPos() - 1, currentNode.currentSteps + 1);
				up.setParent(currentNode);
				sourroundingNodes.add(up);
			}
			if (currentNode.getyPos() + 1 < maxY) {
				Node down = new Node(currentNode.getxPos(), currentNode.getyPos() + 1, currentNode.currentSteps + 1);
				down.setParent(currentNode);
				sourroundingNodes.add(down);
			}

			for (Node node : sourroundingNodes) {

				if (map.get(node.getyPos()).get(node.getxPos()) <= heightPos + 1) {

					if (node.equals(targetPos)) {
						targetPos.setParent(node.getParent());
						reached = true;
						steps = node.getCurrentSteps();
						break;

					} else if (!visited.contains(node)) {
						visited.add(node);
						nodeQueue.add(node);
					}

				}

			}

		}
		return steps;
	}

	private int mapToHeight(String itemString) {

		var item = itemString.charAt(0);

		if (Character.isLowerCase(item)) {
			return item - 96;
		} else {
			if (Character.toString(item).equals("S")) {
				return 1;
			} else {
				return 26;
			}
		}

	}

	private class Node {
		private int xPos;
		private int yPos;
		private int currentSteps;
		private Node parent;

		public Node(int xPos, int yPos) {
			super();
			this.xPos = xPos;
			this.yPos = yPos;
			this.currentSteps = 0;
		}

		public Node(int xPos, int yPos, int currentSteps) {
			super();
			this.xPos = xPos;
			this.yPos = yPos;
			this.currentSteps = currentSteps;
		}

		public int getCurrentSteps() {
			return currentSteps;
		}

		public int getxPos() {
			return xPos;
		}

		public int getyPos() {
			return yPos;
		}

		@Override
		public String toString() {
			return "Node [xPos=" + xPos + ", yPos=" + yPos + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + Objects.hash(xPos, yPos);
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
			Node other = (Node) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			return xPos == other.xPos && yPos == other.yPos;
		}

		private Day12 getEnclosingInstance() {
			return Day12.this;
		}

		public Node getParent() {
			return parent;
		}

		public void setParent(Node parent) {
			this.parent = parent;
		}

	}
}
