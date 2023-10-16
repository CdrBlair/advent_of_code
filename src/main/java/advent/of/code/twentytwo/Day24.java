package advent.of.code.twentytwo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day24 {

	Map<Character, Coordinate> directions = new HashMap<>();

	public int getFewestMinutes() throws IOException {

		long startTime = System.currentTimeMillis();
		try (var br = new BufferedReader(new FileReader(
				"/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/blizzards.txt"));) {

			directions.put('>', new Coordinate(0, 1));
			directions.put('v', new Coordinate(1, 0));
			directions.put('<', new Coordinate(0, -1));
			directions.put('^', new Coordinate(-1, 0));
			List<String> lines = br.lines().toList();

			Coordinate mapsize = new Coordinate(lines.size(), lines.get(0).length());

			Coordinate start = new Coordinate(0, 1);
			Coordinate exit = new Coordinate(lines.size() - 1, lines.get(0).length() - 2);

			List<Coordinate> hurricanePos = new ArrayList<>();
			List<Coordinate> hurricaDir = new ArrayList<>();

			for (var i = 1; i < lines.size() - 1; i++) {
				String blizzard = lines.get(i);
				for (var j = 1; j < blizzard.length() - 1; j++) {
					if (blizzard.charAt(j) != '.') {
						hurricanePos.add(new Coordinate(i, j));
						hurricaDir.add(directions.get(blizzard.charAt(j)));
					}
				}
			}

			Set<Coordinate> paths = new HashSet<>();
			var min = 0;

			// Find exit
			paths.clear();
			paths.add(start);
			while (!paths.contains(exit)) {
				hurricanePos = updateHurricaneCoordinates(mapsize, hurricanePos, hurricaDir);
				paths = updateElvePositions(mapsize, exit, paths, hurricanePos);
				min++;
			}

			System.out.println("Reached Exit in " + min + "  minutes");
			long endTime = System.currentTimeMillis();
			System.out.println("part 1 took " + (endTime - startTime) + " ms");

			paths.clear();
			paths.add(exit);
			while (!paths.contains(start)) {
				hurricanePos = updateHurricaneCoordinates(mapsize, hurricanePos, hurricaDir);
				paths = updateElvePositions(mapsize, start, paths, hurricanePos);
				min++;
			}

			System.out.println("Reached Start again in " + min + "  minutes");
			endTime = System.currentTimeMillis();
			System.out.println("part 2.1 took " + (endTime - startTime) + " ms");

			// Find exit again
			paths.clear();
			paths.add(start);
			while (!paths.contains(exit)) {
				hurricanePos = updateHurricaneCoordinates(mapsize, hurricanePos, hurricaDir);
				paths = updateElvePositions(mapsize, exit, paths, hurricanePos);
				min++;
			}

			System.out.println("Reached Exit AGAIN! in " + min + "  minutes");
			endTime = System.currentTimeMillis();
			System.out.println("part 2 took " + (endTime - startTime) + " ms");
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	private static List<Coordinate> updateHurricaneCoordinates(final Coordinate mapsize,
			final List<Coordinate> hurricanPos, List<Coordinate> hurricaneDirs) {

		List<Coordinate> nextIteration = new ArrayList<>();
		for (int h = 0; h < hurricanPos.size(); h++) {
			Coordinate position = hurricanPos.get(h);
			Coordinate direction = hurricaneDirs.get(h);

			Coordinate newLocation = position.move(direction);
			// Conserve energy:
			nextIteration.add(new Coordinate(Math.floorMod(newLocation.x - 1, mapsize.x - 2) + 1,
					Math.floorMod(newLocation.y - 1, mapsize.y - 2) + 1));
		}
		return nextIteration;
	}

	private Set<Coordinate> updateElvePositions(final Coordinate mapsize, final Coordinate exit,
			final Set<Coordinate> paths, final List<Coordinate> blizzards) {

		Set<Coordinate> deepPaths = new HashSet<>();
		for (Coordinate pos : paths) {
			if (!blizzards.contains(pos)) {
				deepPaths.add(pos);
			}

			for (Coordinate direction : directions.values()) {
				Coordinate newCoord = pos.move(direction);
				if (newCoord.equals(exit)) {
					deepPaths.add(exit);
				}

				if (newCoord.x <= 0 || newCoord.x >= mapsize.x - 1 || newCoord.y <= 0 || newCoord.y >= mapsize.y - 1) {
					continue;
				}
				if (!blizzards.contains(newCoord)) {
					deepPaths.add(newCoord);
				}
			}
		}
		return deepPaths;
	}

	record Coordinate(int x, int y) {

		public Coordinate move(Coordinate direction) {
			return new Coordinate(x + direction.x, y + direction.y);
		}
	}

}
