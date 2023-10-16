package advent.of.code.twentytwo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class Day25 {

	public int calculatFuel() throws IOException {

		long startTime = System.currentTimeMillis();
		try (var br = new BufferedReader(
				new FileReader("/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/snafu.txt"));) {

			List<String> snafuAsString = br.lines().toList();

			List<Long> snafuAsIntWOSigns = snafuAsString.stream()
					.map(s -> s.chars()
							.mapToObj(c -> Long.valueOf(
									StringUtils.replace(StringUtils.replace((((char) c) + ""), "-", "-1"), "=", "-2")))
							.toList())
					.map(s -> convertToInt(new ArrayList<>(s))).toList();

			AtomicInteger count = new AtomicInteger(0);
			var resultAsInt = snafuAsIntWOSigns.stream().mapToLong(s -> Long.valueOf(s))
					.peek(s -> System.out.println("Number: " + count.getAndIncrement() + " is: " + s))
					.reduce(0, (a, b) -> a + b);

			System.out.println("Result in int: " + resultAsInt);

			var resultInBase5 = Long.toString(resultAsInt, 5);

			System.out.println(Long.toString(resultAsInt, 5));
			String snafuResult = convertToSnafuWOSigns(resultInBase5).stream().collect(Collectors.joining());
			System.out.println("Part 1 result in Snafu is: " + snafuResult);
			return 0;

		} catch (IOException e) {
			e.printStackTrace();
			throw e;

		}
	}

	public Long convertToInt(List<Long> snafu) {

		Collections.reverse(snafu);

		Double temp = Double.valueOf(0);
		for (var i = 0; i < snafu.size(); i++) {
			temp += Math.pow(5, i) * snafu.get(i);
		}
		return temp.longValue();
	}

	public List<String> convertToSnafuWOSigns(String valueInBase5) {
		List<Long> valueB5Int = new ArrayList<>(
				valueInBase5.chars().mapToObj(c -> Long.valueOf(((char) c) + "")).toList());

		System.out.println(valueB5Int);

		Collections.reverse(valueB5Int);

		for (var i = 0; i < valueB5Int.size(); i++) {
			if (valueB5Int.get(i) == 3) {
				valueB5Int.set(i, -2l);
				if (i + 1 >= valueB5Int.size()) {
					valueB5Int.add(1l);
				} else {
					valueB5Int.set(i + 1, valueB5Int.get(i + 1) + 1);
				}

			} else if (valueB5Int.get(i) == 4) {
				valueB5Int.set(i, -1l);
				if (i + 1 >= valueB5Int.size()) {
					valueB5Int.add(1l);
				} else {
					valueB5Int.set(i + 1, valueB5Int.get(i + 1) + 1);
				}
			} else if (valueB5Int.get(i) == 5) {
				valueB5Int.set(i, 0l);
				if (i + 1 >= valueB5Int.size()) {
					valueB5Int.add(1l);
				} else {
					valueB5Int.set(i + 1, valueB5Int.get(i + 1) + 1);
				}

			}

		}
		Collections.reverse(valueB5Int);
		System.out.println(valueB5Int);
		List<String> result = valueB5Int.stream().map(String::valueOf)
				.map(s -> StringUtils.replace(StringUtils.replace(s, "-2", "="), "-1", "-")).toList();
		System.out.println(result);
		return result;
	}

}
