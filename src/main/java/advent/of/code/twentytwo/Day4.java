package advent.of.code.twentytwo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class Day4 {

	public int numberOfPairs() throws IOException {

		try (var br = new BufferedReader(new FileReader(
				"/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/sections.txt"));) {

			var result = new AtomicInteger();

			br.lines().map(line -> StringUtils.split(line, ",")).map(Arrays::asList).map(this::convert)
					.forEach(pair -> {
						if (CollectionUtils.containsAny(pair.get(0), pair.get(1))) {

							result.getAndIncrement();
						}
					});

			return result.get();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}

	}

	private List<List<Integer>> convert(List<String> input) {

		List<Integer> elve1Sections = new ArrayList<>();
		List<Integer> elve2Sections = new ArrayList<>();

		String[] elve1Boundaries = StringUtils.split(input.get(0), "-");
		String[] elve2Boundaries = StringUtils.split(input.get(1), "-");
		for (var i = Integer.parseInt(elve1Boundaries[0]); i <= Integer.parseInt(elve1Boundaries[1]); i++) {

			elve1Sections.add(i);

		}

		for (var i = Integer.parseInt(elve2Boundaries[0]); i <= Integer.parseInt(elve2Boundaries[1]); i++) {

			elve2Sections.add(i);

		}

		return Arrays.asList(elve1Sections, elve2Sections);

	}

}
