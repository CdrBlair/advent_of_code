package advent.of.code.twentytwo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.lang3.StringUtils;

public class Day16 {

	Map<String, Valve> valveMap = new HashMap<>();

	public int getMaxPressure() throws IOException {

		try (var br = new BufferedReader(new FileReader(
				"/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/valves.txt"));) {

			List<String> valvesAsString = br.lines().toList();

			valvesAsString
					.forEach(v -> valveMap.put(StringUtils.split(v, " ")[1], new Valve(StringUtils.split(v, " ")[1],
							Integer.valueOf(StringUtils.substringBetween(v, "=", ";")), new HashMap<>(), false)));

			for (String valveAsString : valvesAsString) {

				List<String> targets = Arrays.asList(StringUtils.split(
						StringUtils.appendIfMissing(
								StringUtils.remove(StringUtils.substringAfter(valveAsString, "valves"), " "), ","),
						","));

				for (String target : targets) {
					valveMap.get(StringUtils.split(valveAsString, " ")[1]).getFollowUps().put(valveMap.get(target), 1);
				}

			}

			Graph startGraph = new Graph();

			valveMap.forEach((k, v) -> startGraph.addValve(v));

			for (Entry<String, Valve> entry : valveMap.entrySet()) {

				valveMap.forEach((k, v) -> v.setDistance(Integer.MAX_VALUE));
				valveMap.forEach((k, v) -> v.setShortestPath(new LinkedList<>()));
				Graph current = Dijkstra.calculateShortestPathFromSource(startGraph, entry.getValue());

				current.getValves().stream().filter(v -> !StringUtils.equals(v.getName(), entry.getKey()))
						.forEach(v -> v.getMapShortest().put(entry.getKey(), v.getShortestPath()));
				current.getValves().forEach(v -> valveMap.put(v.getName(), v));

			}

			var maxtime = 30;
			var pressureSum = 0;
			var timeToOpen = 1;
			var best = 0;

			Valve startValve = valveMap.get("AA");

			List<Valve> valvesWithPressure = valveMap.entrySet().stream().map(e -> e.getValue())
					.filter(v -> v.getFlowRate() > 0).toList();

			for (Valve cv : valveMap.values()) {

				cv.getMapShortest().entrySet().removeIf(e -> valveMap.get(e.getKey()).getFlowRate() == 0);
			}

			StateP1 start = new StateP1(startValve, 30, 0, new HashSet<>());
			Stack<StateP1> stack = new Stack<>();
			stack.add(start);
			while (!stack.isEmpty()) {

				StateP1 currentState = stack.pop();
				boolean terminate = true;

				for (var path : currentState.current().getMapShortest().entrySet()) {

					Valve target = valveMap.get(path.getKey());
					int dist = path.getValue().size();
					if (currentState.timeAtValve - dist - timeToOpen > 0 && !currentState.opened().contains(target)) {
						HashSet<Valve> newOpened = new HashSet<>(currentState.opened());
						newOpened.add(target);
						int newPressure = (currentState.timeAtValve() - dist - timeToOpen) * target.getFlowRate();
						terminate = false;
						stack.add(new StateP1(target, currentState.timeAtValve() - dist - timeToOpen,
								currentState.pressureCombined() + newPressure, newOpened));
					}

				}

				if (terminate) {
					if (currentState.pressureCombined > best) {
						best = currentState.pressureCombined();
					}
				}

			}

			// part 2: same but different, state is more complex
			Player startHuman = new Player(startValve, 26);
			Player startElephant = new Player(startValve, 26);
			List<StateP2> stack2 = new LinkedList<>();
			stack2.add(new StateP2(startHuman, startElephant, 0, new ArrayList<>()));
			best = 0;
			while (!stack2.isEmpty()) {
				StateP2 state = stack2.remove(0);

				// always move the player that is behind in time, or the human if tied
				boolean moveElephant = state.elephant.minutes > state.human.minutes;
				boolean terminal = addMoves(stack2, state, moveElephant);
				// if the mover cannot move, maybe the other Player still can
				if (terminal) {
					terminal = addMoves(stack2, state, !moveElephant);
				}

				if (terminal) {
					if (state.released > best) {
						System.out.println("new best: " + state);
						best = state.released;
					}
				}
			}
			System.out.println("part 2: " + best);

			return best;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	private boolean addMoves(List<StateP2> stack2, StateP2 state, boolean moveElephant) {
		boolean terminal = true;
		var timeToOpen = 1;
		Player mover = moveElephant ? state.elephant : state.human;
		for (var way : mover.cur.mapShortest.entrySet()) {
			Integer dist = way.getValue().size();
			Valve target = valveMap.get(way.getKey());
			if (mover.minutes - dist - timeToOpen > 0 && !state.opened.contains(target)) {
				ArrayList<Valve> newOpened = new ArrayList<>(state.opened);
				newOpened.add(target);
				int newPressure = (mover.minutes - dist - timeToOpen) * target.flowRate;
				terminal = false;
				Player newPlayer = new Player(target, mover.minutes - dist - timeToOpen);
				stack2.add(new StateP2(moveElephant ? state.human : newPlayer,
						moveElephant ? newPlayer : state.elephant, state.released + newPressure, newOpened));
			}
		}
		return terminal;
	}

	record StateP1(Valve current, int timeAtValve, int pressureCombined, Set<Valve> opened) {

	}

	record Player(Valve cur, int minutes) {
		@Override
		public String toString() {
			return cur + "_" + minutes;
		}
	}

	record StateP2(Player human, Player elephant, int released, Collection<Valve> opened) {
	}

	private class Valve {

		private String name;
		private int flowRate;
		private Map<Valve, Integer> followUps;
		private boolean open;
		private Map<String, LinkedList<Valve>> mapShortest = new HashMap<>();
		private Integer distance = Integer.MAX_VALUE;
		private LinkedList<Valve> shortestPath = new LinkedList<>();

		public LinkedList<Valve> getShortestPath() {
			return shortestPath;
		}

		public void setShortestPath(LinkedList<Valve> shortestPath) {
			this.shortestPath = shortestPath;
		}

		public Valve(String name, int flowRate, Map<Valve, Integer> followUps) {
			super();
			this.name = name;
			this.flowRate = flowRate;
			this.followUps = followUps;
		}

		public Valve(String name, int flowRate, Map<Valve, Integer> followUps, boolean open) {
			super();
			this.name = name;
			this.flowRate = flowRate;
			this.followUps = followUps;
			this.open = open;
		}

		public Integer getDistance() {
			return distance;
		}

		public void setDistance(Integer distance) {
			this.distance = distance;
		}

		public Map<String, LinkedList<Valve>> getMapShortest() {
			return this.mapShortest;
		}

		public void setMapShortest(Map<String, LinkedList<Valve>> mapShortest) {
			this.mapShortest = mapShortest;
		}

		public boolean isOpen() {
			return open;
		}

		public void setOpen(boolean open) {
			this.open = open;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getFlowRate() {
			return flowRate;
		}

		public void setFlowRate(int flowRate) {
			this.flowRate = flowRate;
		}

		public Map<Valve, Integer> getFollowUps() {
			return followUps;
		}

		public void setFollowUps(Map<Valve, Integer> followUps) {
			this.followUps = followUps;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Valve other = (Valve) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			return flowRate == other.flowRate && Objects.equals(followUps, other.followUps)
					&& Objects.equals(name, other.name) && open == other.open;
		}

		private Day16 getEnclosingInstance() {
			return Day16.this;
		}

		@Override
		public String toString() {

			StringBuilder sb = new StringBuilder();
			for (Entry<Valve, Integer> valve : followUps.entrySet()) {
				sb.append(valve.getKey().getName()).append(", ");
			}

			var followUps = sb.toString();

			return "Valve [name=" + name + ", flowRate=" + flowRate + ", followUps=" + followUps + "]";
		}

	}

	private class Graph {
		private Set<Valve> valves = new HashSet<>();

		public void addValve(Valve valve) {
			valves.add(valve);
		}

		public Set<Valve> getValves() {
			return valves;
		}

		public void setValves(Set<Valve> valves) {
			this.valves = valves;
		}

		@Override
		public String toString() {
			return "Graph [valves=" + valves + "]";
		}

	}

	private class Dijkstra {

		public static Graph calculateShortestPathFromSource(Graph graph, Valve source) {

			source.setDistance(0);

			Set<Valve> settledNodes = new HashSet<>();
			Set<Valve> unsettledNodes = new HashSet<>();
			unsettledNodes.add(source);

			while (unsettledNodes.size() != 0) {
				Valve currentNode = getLowestDistanceNode(unsettledNodes);
				unsettledNodes.remove(currentNode);
				for (Entry<Valve, Integer> adjacencyPair : currentNode.getFollowUps().entrySet()) {
					Valve adjacentNode = adjacencyPair.getKey();
					Integer edgeWeigh = adjacencyPair.getValue();

					if (!settledNodes.contains(adjacentNode)) {
						CalculateMinimumDistance(adjacentNode, edgeWeigh, currentNode);
						unsettledNodes.add(adjacentNode);
					}
				}
				settledNodes.add(currentNode);
			}
			return graph;
		}

		private static void CalculateMinimumDistance(Valve evaluationNode, Integer edgeWeigh, Valve sourceNode) {
			Integer sourceDistance = sourceNode.getDistance();
			if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
				evaluationNode.setDistance(sourceDistance + edgeWeigh);
				LinkedList<Valve> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
				shortestPath.add(sourceNode);
				evaluationNode.setShortestPath(shortestPath);
			}
		}

		private static Valve getLowestDistanceNode(Set<Valve> unsettledNodes) {
			Valve lowestDistanceNode = null;
			int lowestDistance = Integer.MAX_VALUE;
			for (Valve node : unsettledNodes) {
				int nodeDistance = node.getDistance();
				if (nodeDistance < lowestDistance) {
					lowestDistance = nodeDistance;
					lowestDistanceNode = node;
				}
			}
			return lowestDistanceNode;
		}
	}

}
