package advent.of.code.twentytwo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Day5 {

	public String topCrates() throws IOException {
		try (var br = new BufferedReader(new FileReader(
				"/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/crates.txt"));) {

			List<String> crates = br.lines().limit(8).collect(Collectors.toList());

			List<List<String>> staples = new ArrayList<>();
			staples.addAll(Arrays.asList(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
					new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

			br.lines().limit(2).collect(Collectors.toList());

			for (String line : crates) {
				staples = fillInput(staples, line);

			}

			staples.stream().forEach(Collections::reverse);
			staples.stream().forEach(x -> x.removeIf(String::isBlank));

			for (List<String> row : staples) {
				System.out.println(row);
			}

			List<List<String>> moves = br.lines().map(x -> x.replaceAll("[^0-9]+", " "))
					.map(x -> Arrays.asList(x.trim().split(" "))).collect(Collectors.toList());

			for (List<String> move : moves) {
				moveCrates9001(staples, move);
			}

			System.out.println("");
			for (List<String> row : staples) {
				System.out.println(row);
			}

			return staples.stream().map(x -> x.get(x.size() - 1)).collect(Collectors.joining());

		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}

	}

	public List<List<String>> fillInput(List<List<String>> currentStaples, String currentLine) {

		String crate1 = currentLine.substring(0, 3);
		String crate2 = currentLine.substring(4, 7);
		String crate3 = currentLine.substring(8, 11);
		String crate4 = currentLine.substring(12, 15);
		String crate5 = currentLine.substring(16, 19);
		String crate6 = currentLine.substring(20, 23);
		String crate7 = currentLine.substring(24, 27);
		String crate8 = currentLine.substring(28, 31);
		String crate9 = currentLine.substring(32, 35);

		currentStaples.get(0).add(crate1);
		currentStaples.get(1).add(crate2);
		currentStaples.get(2).add(crate3);
		currentStaples.get(3).add(crate4);
		currentStaples.get(4).add(crate5);
		currentStaples.get(5).add(crate6);
		currentStaples.get(6).add(crate7);
		currentStaples.get(7).add(crate8);
		currentStaples.get(8).add(crate9);

		return currentStaples;

	}

	public void moveCrates(List<List<String>> currentStaples, List<String> moveOrder) {

		List<Integer> moves = moveOrder.stream().map(Integer::parseInt).collect(Collectors.toList());
		List<String> fromStaple = currentStaples.get(moves.get(1) - 1);
		List<String> toStaple = currentStaples.get(moves.get(2) - 1);
		for (var i = 0; i < moves.get(0); i++) {

			if (!fromStaple.isEmpty()) {
				toStaple.add(fromStaple.get(fromStaple.size() - 1));
				fromStaple.remove(fromStaple.size() - 1);
			}

		}
	}

	public void moveCrates9001(List<List<String>> currentStaples, List<String> moveOrder) {

		List<Integer> moves = moveOrder.stream().map(Integer::parseInt).collect(Collectors.toList());
		List<String> fromStaple = currentStaples.get(moves.get(1) - 1);
		List<String> toStaple = currentStaples.get(moves.get(2) - 1);

		for (var i = moves.get(0); i > 0; i--) {

			if (!fromStaple.isEmpty()) {
				toStaple.add(fromStaple.get(fromStaple.size() - i));
				fromStaple.set(fromStaple.size() - i, " ");
			}

		}
		fromStaple.removeIf(String::isBlank);
	}

}
