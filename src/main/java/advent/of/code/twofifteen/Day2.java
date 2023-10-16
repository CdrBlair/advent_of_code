package advent.of.code.twofifteen;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class Day2 {

	public int wrapPaper() throws IOException {
		try (var br = new BufferedReader(new FileReader(
				"/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/gift_size.txt"));) {

			List<List<Integer>> sizes = br.lines().map(line -> Arrays.asList(StringUtils.split(line, "x")))
					.map(line -> Arrays.asList(Integer.valueOf(line.get(0)), Integer.valueOf(line.get(1)),
							Integer.valueOf(line.get(2))))
					.collect(Collectors.toList());

			return sizes.stream().collect(Collectors.summingInt(this::wrapSize));

		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}

	}

	private int wrapSize(List<Integer> sizes) {
		int length = sizes.get(0);

		int width = sizes.get(1);
		int height = sizes.get(2);

		List<Integer> areas = Arrays.asList(length, width, height);
		int bow = length * width * height;
		Collections.sort(areas);
		return bow + areas.stream().limit(2).map(l -> 2 * l).collect(Collectors.summingInt(Integer::intValue));

	}
}
