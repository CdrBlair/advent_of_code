package advent.of.code.twentytwo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class Day7 {

	Node currentNode = null;
	Node topNode = null;
	int disksize = 70000000;
	int spaceForUpdate = 30000000;

	Map<String, Integer> mapOfSizes = new HashMap<>();

	public int sizeOfFolders() throws IOException {

		int result = 0;
		try (var br = new BufferedReader(
				new FileReader("/Users/amv/work/advent_of_code/advent_of_code/src/main/resources/shell.txt"));) {

			List<String> lines = br.lines().collect(Collectors.toList());

			for (String line : lines) {

				String[] inputCommands = line.split(" ");

				// commands
				if (StringUtils.equals(inputCommands[0], "$")) {
					if (StringUtils.equals(inputCommands[1], "ls")) {
						// Do nothing
					} else if (StringUtils.equals(inputCommands[1], "cd")) {

						currentNode = moveToNode(inputCommands[2]);

					}

				} else {

					String type = StringUtils.equals(inputCommands[0], "dir") ? "dir" : "file";
					List<Node> childs = StringUtils.equals(inputCommands[0], "dir") ? new ArrayList<>() : null;
					int size = StringUtils.equals(inputCommands[0], "dir") ? 0 : Integer.valueOf(inputCommands[0]);

					Node newChild = new Node(inputCommands[1], type, currentNode, childs, size);
					currentNode.getChilds().add(newChild);

				}

			}

			calcAllSizes(topNode);
			int freeSize = disksize - mapOfSizes.get("/");
			System.out.println(freeSize);

			result = mapOfSizes.entrySet().stream()
					.filter(e -> disksize - mapOfSizes.get("/") + e.getValue() >= spaceForUpdate)
					.mapToInt(Map.Entry::getValue).peek(System.out::println).min().getAsInt();

		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}

		return result;
	}

	private void calcAllSizes(Node node) {

		if (node.getType().equals("dir")) {
			String parentName = node.getParent() == null ? "" : node.getParent().getName() + "/";
			mapOfSizes.put(parentName + node.getName(), calcSize(node));
			for (Node child : node.getChilds()) {
				calcAllSizes(child);

			}
		}
	}

	private int calcSize(Node node) {
		if (node.getType().equals("file")) {
			return node.getSize();
		} else {
			return node.getChilds().stream().map(this::calcSize).collect(Collectors.summingInt(Integer::intValue));

		}
	}

	private Node moveToNode(String to) {

		if (StringUtils.equals(to, "/")) {
			if (topNode == null) {
				topNode = new Node(to, "dir", null, new ArrayList<>(), 0);

			}
			return topNode;
		} else if (StringUtils.equals(to, "..")) {
			return currentNode.getParent();
		} else {
			for (Node child : currentNode.childs) {
				if (StringUtils.equals(child.getType(), "dir") && StringUtils.equals(child.getName(), to)) {
					return child;
				}

			}
		}

		var newChild = new Node(to, "dir", currentNode, new ArrayList<>(), 0);
		currentNode.getChilds().add(newChild);
		return newChild;
	}

	private class Node {
		String name;
		String type;
		Node parent;
		List<Node> childs;
		int size;

		public String getName() {
			return name;
		}


		public String getType() {
			return type;
		}


		public Node getParent() {
			return parent;
		}



		public List<Node> getChilds() {
			return childs;
		}

	

		public int getSize() {
			return size;
		}

	

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + Objects.hash(childs, name, size, type);
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
			return Objects.equals(childs, other.childs) && Objects.equals(name, other.name)
					&& Objects.equals(parent, other.parent) && size == other.size && Objects.equals(type, other.type);
		}

		private Day7 getEnclosingInstance() {
			return Day7.this;
		}

		@Override
		public String toString() {

			StringBuilder sb;
			if (!name.equals("/")) {
				sb = new StringBuilder("\t - ");
			} else {
				sb = new StringBuilder("- ");
			}

			if (type.equals("dir")) {

				sb.append(name).append(" (dir)");
				sb.append("\r\n");
				for (Node child : childs) {
					sb.append("\t");
					sb.append(child.toString());
				}
				return sb.toString();

			} else {
				sb.append(name).append(" (file, size=");
				sb.append(size);
				sb.append(")");
				sb.append("\r\n");

				return sb.toString();
			}

		}

		public Node(String name, String type, Node parent, List<Node> childs, int size) {
			super();
			this.name = name;
			this.type = type;
			this.parent = parent;
			this.childs = childs;
			this.size = size;
		}

	}
}
