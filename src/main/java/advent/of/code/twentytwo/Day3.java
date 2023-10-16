package advent.of.code.twentytwo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Day3 {

	public int sumOfPrios() throws IOException {
		try (var br = new BufferedReader(new FileReader(
				"/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/rucksacks.txt"));) {

			List<String> rucksackItems = br.lines().collect(Collectors.toList());

			List<List<String>> elveGroups = new ArrayList<>();
			for (var i = 0; i < rucksackItems.size(); i += 3) {

				int endIndex = Math.min(i + 3, rucksackItems.size());
				elveGroups.add(rucksackItems.subList(i, endIndex));

			}
			System.out.println(elveGroups);

			var sumOfPrios = 0;
			for (List<String> rucksack : elveGroups) {

				Optional<Character> optChar = rucksack.get(0).chars().mapToObj(c -> (char) c)
						.filter(x -> rucksack.get(1).contains(x.toString()))
						.filter(x -> rucksack.get(2).contains(x.toString())).findFirst();

				if (optChar.isPresent()) {
					System.out.println("Sum before: " + sumOfPrios);
					System.out.println(optChar.get());
					sumOfPrios += getPrioOfChar(optChar.get());
					System.out.println("Sum after: " + sumOfPrios);

				}

			}

			return sumOfPrios;
		} catch (IOException e) {

			e.printStackTrace();
			throw e;
		}

	}

	private int getPrioOfChar(Character item) {

		if (Character.isLowerCase(item)) {
			return (int) item - 96;
		} else {
			return (int) item - 38;
		}

	}
}
