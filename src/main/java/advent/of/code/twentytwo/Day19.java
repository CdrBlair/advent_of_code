package advent.of.code.twentytwo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class Day19 {

	public int getSumQualityLevel() throws IOException {

		try (var br = new BufferedReader(new FileReader(
				"/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/blueprints.txt"));) {
			int answer = 0;
			List<BluePrint> blueprints = new ArrayList<>();

			List<String> lines = br.lines().toList();
			lines.stream().map(s -> StringUtils.split(s, " "))
					.forEach(l -> blueprints
							.add(new BluePrint(Integer.valueOf(l[6]), Integer.valueOf(l[12]), Integer.valueOf(l[18]),
									Integer.valueOf(l[27]), Integer.valueOf(l[21]), Integer.valueOf(l[30]))));
			for (var i = 0; i < blueprints.size(); i++) {
				BluePrint.m = 0;
				answer += (i + 1) * blueprints.get(i).result(24, new int[] { 1, 0, 0, 0 }, new int[4], new HashMap<>());
			}

			System.out.println("part 1 answer: " + answer);

			answer = 1;

			for (var i = 0; i < 3; i++) {
				answer *= blueprints.get(i).result(32, new int[] { 1, 0, 0, 0 }, new int[4], new HashMap<>());
			}
			System.out.println("part 2 answer: " + answer);
			return answer;

		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}

	}

	private class BluePrint {
		static int m = 0;
		int[] oreR = new int[4];
		int[] clayR = new int[4];
		int[] obsidianR = new int[4];
		int[] geodeR = new int[4];

		public BluePrint(int o1, int o2, int o3, int o4, int c3, int ob4) {
			oreR[0] = o1;
			clayR[0] = o2;
			obsidianR[0] = o3;
			geodeR[0] = o4;
			obsidianR[1] = c3;
			geodeR[2] = ob4;
		}

		public int result(int min, int[] currR, int[] currRes, Map<String, Integer> seen) {
			String hash = min + " " + currR[0] + " " + currR[1] + " " + currR[2] + " " + currR[3] + " " + currRes[0]
					+ " " + currRes[1] + " " + currRes[2] + " " + currRes[3] + " ";
			if (seen.keySet().contains(hash)) {
				return seen.get(hash);
			}
			int answer = 0;
			if (min == 0) {
				if (currRes[3] > m) {
					m = currRes[3];
				}
				return currRes[3];
			}
			if (m > currRes[3] + (min * (currR[3] + min))) {
				return -1;
			}
			List<int[]> possibleR = new ArrayList<>();
			List<int[]> possibleRes = new ArrayList<>();
			boolean geodeRpossible = possible(currRes, geodeR);
			boolean obsidianRpossible = possible(currRes, obsidianR);
			boolean clayRpossible = possible(currRes, clayR);
			boolean oreRpossible = possible(currRes, oreR);

			if (geodeRpossible) {
				int[] newRes4 = subtract(currRes, geodeR);
				possibleRes.add(newRes4);
				possibleR.add(new int[] { currR[0], currR[1], currR[2], currR[3] + 1 });
			} else {
				if (obsidianRpossible) {
					if (!(currRes[2] > min * (geodeR[2] - currR[2]))) {
						int[] newRes4 = subtract(currRes, obsidianR);
						possibleRes.add(newRes4);
						possibleR.add(new int[] { currR[0], currR[1], currR[2] + 1, currR[3] });
					}

				}
				if (clayRpossible) {
					if (!(currRes[1] > min * (obsidianR[1] - currR[1]))) {
						int[] newRes4 = subtract(currRes, clayR);
						possibleRes.add(newRes4);
						possibleR.add(new int[] { currR[0], currR[1] + 1, currR[2], currR[3] });
					}

				}
				if (oreRpossible) {
					int max = Math.max(Math.max(oreR[0], clayR[0]), Math.max(obsidianR[0], geodeR[0]));
					if (!(currRes[0] > min * (max - currR[0]))) {
						int[] newRes4 = subtract(currRes, oreR);
						possibleRes.add(newRes4);
						possibleR.add(new int[] { currR[0] + 1, currR[1], currR[2], currR[3] });
					}
				}
				possibleRes.add(currRes);
				possibleR.add(currR);
			}

			for (int i = 0; i < possibleR.size(); i++) {
				int[] newRes = new int[currR.length];
				for (int j = 0; j < currRes.length; j++) {
					newRes[j] += currR[j] + possibleRes.get(i)[j];
				}
				answer = Math.max(answer, result(min - 1, possibleR.get(i), newRes, seen));
			}
			seen.put(hash, answer);
			return answer;
		}

		public int[] subtract(int[] currRes, int[] R) {
			int[] newRes = new int[currRes.length];
			for (int i = 0; i < currRes.length; i++) {
				newRes[i] = currRes[i] - R[i];
			}
			return newRes;
		}

		public boolean possible(int[] currRes, int[] R) {
			for (int i = 0; i < currRes.length; i++) {
				if (currRes[i] < R[i]) {
					return false;
				}
			}
			return true;
		}

	}
}
