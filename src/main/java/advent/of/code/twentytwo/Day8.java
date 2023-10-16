package advent.of.code.twentytwo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Day8 {

	public int numberVisibleTrees() throws IOException {

		try (var br = new BufferedReader(
				new FileReader("/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/trees.txt"));) {

			List<String> lines = br.lines().toList();
			int numberOfTreesTopBottom = lines.get(0).length() * 2;
			int numberOfTreesSides = lines.size() * 2;
			int numberOfVisibleTress = 0;
			int score = 0;

			for (int i = 0; i < lines.size(); i++) {
				String currentRow = lines.get(i);
				for (int j = 0; j < currentRow.length(); j++) {
					boolean treeVisibleRight = true;
					boolean treeVisibleLeft = true;
					boolean treeVisibleUp = true;
					boolean treeVisibleDown = true;

					int viewingDistanceRight = 0;
					int viewingDistanceLeft = 0;
					int viewingDistanceUp = 0;
					int viewingDistanceDown = 0;

					System.out.println("i" + i + "j" + j);
					Integer currentTree = Integer.valueOf("" + currentRow.charAt(j));
					System.out.println(currentTree);

					// looking right
					for (int r = j + 1; r < currentRow.length(); r++) {

						viewingDistanceRight++;

						if (currentTree <= Integer.valueOf("" + currentRow.charAt(r)))
							break;
					}

					// looking left
					for (int l = j - 1; l > -1; l--) {

						viewingDistanceLeft++;
						if (currentTree <= Integer.valueOf("" + currentRow.charAt(l))) {

							break;
						}
					}

					// looking up
					for (int u = i - 1; u > -1; u--) {

						viewingDistanceUp++;
						if (currentTree <= Integer.valueOf("" + lines.get(u).charAt(j))) {

							break;
						}
					}

					for (int d = i + 1; d < lines.size(); d++) {

						viewingDistanceDown++;
						if (currentTree <= Integer.valueOf("" + lines.get(d).charAt(j))) {
							break;

						}
					}

					int currentScore = viewingDistanceDown * viewingDistanceUp * viewingDistanceLeft
							* viewingDistanceRight;

					System.out.println(currentScore);
					if (currentScore > score) {
						score = currentScore;
					}

				}
			}

			return score;

		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}

	}

}
