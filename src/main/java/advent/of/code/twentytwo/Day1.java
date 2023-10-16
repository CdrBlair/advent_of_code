package advent.of.code.twentytwo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class Day1 {

	public int findMostCalories() throws FileNotFoundException {

		try (BufferedReader br = new BufferedReader(new FileReader(
				"/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/calories.txt"));) {

			List<Integer> listOfPackaged = new ArrayList<>();
			listOfPackaged.add(0);

			br.lines().forEach(x -> {
				if (StringUtils.isBlank(x)) {
					listOfPackaged.add(0);

				} else {
					listOfPackaged.set(listOfPackaged.size() - 1,
							listOfPackaged.get(listOfPackaged.size() - 1) + Integer.valueOf(x));
				}
			});

			Collections.sort(listOfPackaged, Collections.reverseOrder());

			System.out.println(listOfPackaged);

			return listOfPackaged.stream().limit(3).collect(Collectors.summingInt(Integer::intValue));

		} catch (IOException ex) {
			ex.printStackTrace();
			return 0;
		}
	}

}
