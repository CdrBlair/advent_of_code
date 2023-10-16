package advent.of.code.twentytwo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class Day13 {

	public int sumOfInts() throws IOException {
		try (var br = new BufferedReader(new FileReader(
				"/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/data_packages.txt"));) {

			List<String> lines = br.lines().toList();
			List<List<String>> pairsAsString = new ArrayList<>();
			var result = 0;

			for (var i = 0; i < lines.size(); i = i + 3) {

				pairsAsString.add(lines.subList(i, i + 2));

			}

			List<List<List<Object>>> parsedPairs = new ArrayList<>();
			for (List<String> pair : pairsAsString) {
				List<List<Object>> parsedPair = new ArrayList<>();
				for (String values : pair) {
					List<Object> parsedValue = parse(values);
					parsedPair.add(parsedValue);
				}
				parsedPairs.add(parsedPair);
			}

			var indexOfCompaired = 1;
			List<List<Object>> all = new ArrayList<>();
			for (List<List<Object>> pairsToCompare : parsedPairs) {
				var compareResult = comparePairs(pairsToCompare.get(0), pairsToCompare.get(1));

				System.out.println("left: " + pairsToCompare.get(0));
				System.out.println("right: " + pairsToCompare.get(1));
				String booleanPrint = (compareResult == 0 || compareResult == 1) ? "true" : "false";
				System.out.println("result of compare: " + booleanPrint);

				if (compareResult == 0 || compareResult == 1) {

					result = result + indexOfCompaired;

				}
				indexOfCompaired++;
				all.addAll(pairsToCompare);
			}

			List<Object> new2 = Arrays.asList(Arrays.asList(Integer.valueOf(2)));
			List<Object> new6 = Arrays.asList(Arrays.asList(Integer.valueOf(6)));

			all.add(new2);
			all.add(new6);

			System.out.println();
			all.stream().forEach(System.out::println);
			Collections.sort(all, new DecodeComparator());

			System.out.println();
			all.stream().forEach(System.out::println);

			result = (all.indexOf(new2) + 1) * (all.indexOf(new6) + 1);

			return result;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	private class DecodeComparator implements Comparator<List<Object>> {

		@Override
		public int compare(List<Object> o1, List<Object> o2) {
			return -comparePairs(o1, o2);
		}

	}

	private int comparePairs(List<Object> left, List<Object> right) {
		var indexRight = 0;
		for (Object leftObject : left) {
			if (indexRight > right.size() - 1) {
				return -1;
			}

			if (leftObject instanceof List<?> && right.get(indexRight) instanceof List<?>) {

				List<Object> leftList = (List<Object>) leftObject;
				List<Object> rightList = (List<Object>) right.get(indexRight);

				var subResult = comparePairs(leftList, rightList);

				if (subResult == -1) {
					return -1;
				} else if (subResult != 0) {
					return 1;
				}
			} else if (leftObject instanceof Integer && right.get(indexRight) instanceof Integer) {
				if ((Integer) leftObject < (Integer) right.get(indexRight)) {
					return 1;
				} else if ((Integer) leftObject > (Integer) right.get(indexRight)) {
					return -1;
				}
			} else if (leftObject instanceof Integer) {

				var subResult = comparePairs(Arrays.asList(leftObject), (List<Object>) right.get(indexRight));
				if (subResult == -1) {
					return -1;
				} else if (subResult != 0) {
					return 1;
				}

			} else {
				var subResult = comparePairs((List<Object>) leftObject, Arrays.asList(right.get(indexRight)));
				if (subResult == -1) {
					return -1;
				} else if (subResult != 0) {
					return 1;
				}
			}

			indexRight++;
		}

		if (left.size() == right.size())
			return 0;
		else
			return 1;
	}

	private List<Object> parse(String message) {

		List<Object> result = new ArrayList<>();

		message = StringUtils.removeEnd(message, "]");

		message = StringUtils.removeStart(message, "[");

		if (StringUtils.isBlank(message)) {
			// leave message empty
		} else if (StringUtils.containsNone(message, "[")) {
			List<String> values = Arrays.asList(StringUtils.split(message, ","));

			values.stream().forEach(n -> result.add(Integer.valueOf(n)));

		} else {

			String remaining = message;
			while (StringUtils.isNotBlank(remaining)) {
				if (remaining.substring(0, 1).equals("[")) {

					var bracketCount = 0;
					var indexOfBracketOpen = 0;
					var indexOfBracketClose = 1;

					List<Character> charsOfRemaining = remaining.chars().mapToObj(c -> (char) c)
							.collect(Collectors.toList());
					for (Character current : charsOfRemaining) {
						if (StringUtils.equals(current.toString(), "[")) {
							bracketCount++;
						} else if (StringUtils.equals(current.toString(), "]")) {
							bracketCount--;
						}

						if (bracketCount == 0) {
							break;
						} else {
							indexOfBracketClose++;
						}
					}

					result.add(parse(StringUtils.substring(remaining, indexOfBracketOpen, indexOfBracketClose)));
					remaining = StringUtils.removeStart(
							StringUtils.substring(remaining, indexOfBracketClose, remaining.length()), ",");
				} else if (StringUtils.containsNone(remaining, "[")) {
					List<String> values = Arrays.asList(StringUtils.split(remaining, ","));

					values.stream().forEach(n -> result.add(Integer.valueOf(n)));
					break;
				} else {
					List<String> values = Arrays.asList(StringUtils
							.split(StringUtils.removeEnd(StringUtils.substringBefore(remaining, "["), ","), ","));

					values.stream().forEach(n -> result.add(Integer.valueOf(n)));

					remaining = "[" + StringUtils.substringAfter(remaining, "[");
				}
			}

		}

		return result;
	}
}
