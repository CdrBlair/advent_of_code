package advent.of.code.twentytwo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class Day18 {
	Map<Coordinate, Boolean> lavaGrid = new HashMap<>();
	Map<Coordinate, Boolean> air = new HashMap<>();
	Set<Coordinate> seen = new HashSet<>();

	public int surfaceObsidian() throws IOException {
		try (var br = new BufferedReader(new FileReader(
				"/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/obsidian.txt"));) {

			List<Coordinate> lava = br.lines().map(s -> StringUtils.split(s, ","))
					.map(c -> new Coordinate(Integer.valueOf(c[0]), Integer.valueOf(c[1]), Integer.valueOf(c[2])))
					.toList();

			var maxX = lava.stream().mapToInt(Coordinate::x).max().getAsInt() + 1;
			var maxY = lava.stream().mapToInt(Coordinate::y).max().getAsInt() + 1;
			var maxZ = lava.stream().mapToInt(Coordinate::z).max().getAsInt() + 1;

			var minX = lava.stream().mapToInt(Coordinate::x).min().getAsInt() - 1;
			var minY = lava.stream().mapToInt(Coordinate::y).min().getAsInt() - 1;
			var minZ = lava.stream().mapToInt(Coordinate::z).min().getAsInt() - 1;

			lava.forEach(l -> lavaGrid.put(l, true));

			var sumSurfaces = 0;

			for (Entry<Coordinate, Boolean> entry : lavaGrid.entrySet()) {

				var surfaces = 6;
				Coordinate currentBlock = entry.getKey();
				if (Boolean.TRUE.equals(lavaGrid.get(currentBlock))) {
					// UP
					var up = new Coordinate(currentBlock.x, currentBlock.y, currentBlock.z + 1);
					if (Boolean.TRUE.equals(lavaGrid.get(up))) {
						surfaces--;
					} else {
						air.put(up, false);
					}

					// DOWN
					var down = new Coordinate(currentBlock.x, currentBlock.y, currentBlock.z - 1);
					if (Boolean.TRUE.equals(lavaGrid.get(down))) {
						surfaces--;
					} else {
						air.put(down, false);
					}

					// left
					var left = new Coordinate(currentBlock.x - 1, currentBlock.y, currentBlock.z);
					if (Boolean.TRUE.equals(lavaGrid.get(left))) {
						surfaces--;
					} else {
						air.put(left, false);
					}

					// right
					var right = new Coordinate(currentBlock.x + 1, currentBlock.y, currentBlock.z);
					if (Boolean.TRUE.equals(lavaGrid.get(right))) {
						surfaces--;
					} else {
						air.put(right, false);
					}

					// behind
					var behind = new Coordinate(currentBlock.x, currentBlock.y - 1, currentBlock.z);
					if (Boolean.TRUE.equals(lavaGrid.get(behind))) {
						surfaces--;
					} else {
						air.put(behind, false);
					}

					// before
					var before = new Coordinate(currentBlock.x, currentBlock.y + 1, currentBlock.z);
					if (Boolean.TRUE.equals(lavaGrid.get(before))) {
						surfaces--;
					} else {
						air.put(before, false);
					}

					sumSurfaces = sumSurfaces + surfaces;
				}
			}

			System.out.println("Part 1, Surfaces:" + sumSurfaces);
			Deque<Coordinate> stack = new ArrayDeque<>();
			stack.add(new Coordinate(minX, minY, minZ));

			var surfaceTest = 0;
			while (!stack.isEmpty()) {
				var current = stack.pop();

				if (minX <= current.x && current.x <= maxX && minY <= current.y && current.y <= maxY
						&& minZ <= current.z && current.z <= maxZ && !seen.contains(current)) {
					lavaGrid.put(current, false);
					// UP
					var up = new Coordinate(current.x, current.y, current.z + 1);
					if (!Boolean.TRUE.equals(lavaGrid.get(up))) {
						stack.add(up);
					} else {
						surfaceTest++;
					}

					// right
					var right = new Coordinate(current.x + 1, current.y, current.z);
					if (!Boolean.TRUE.equals(lavaGrid.get(right))) {
						stack.add(right);
					} else {
						surfaceTest++;
					}

					// before
					var before = new Coordinate(current.x, current.y + 1, current.z);
					if (!Boolean.TRUE.equals(lavaGrid.get(before))) {
						stack.add(before);
					} else {
						surfaceTest++;
					}

					// DWON
					var down = new Coordinate(current.x, current.y, current.z - 1);
					if (!Boolean.TRUE.equals(lavaGrid.get(down))) {
						stack.add(down);
					} else {
						surfaceTest++;
					}

					// left
					var left = new Coordinate(current.x - 1, current.y, current.z);
					if (!Boolean.TRUE.equals(lavaGrid.get(left))) {
						stack.add(left);
					} else {
						surfaceTest++;
					}

					// behind
					var behind = new Coordinate(current.x, current.y - 1, current.z);
					if (!Boolean.TRUE.equals(lavaGrid.get(behind))) {
						stack.add(behind);
					} else {
						surfaceTest++;
					}

					seen.add(current);

				}
			}
			System.out.println("Part 2, Surfaces:" + surfaceTest);
			return sumSurfaces;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	record Coordinate(int x, int y, int z) {

	}


}
