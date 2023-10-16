package advent.of.code.twentytwo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class Day6 {

	public int startOfData() throws IOException {

		try (var br = new BufferedReader(new FileReader(
				"/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/communication.txt"));) {

			String inputData = br.lines().collect(Collectors.toList()).get(0);

			var result = 0;

			for (int i = 0, endIndex = 14; endIndex < inputData.length(); i++, endIndex++) {

				var currentBlock = inputData.substring(i, endIndex);

				if (currentBlock.chars().mapToObj(c -> (char) c)
						.noneMatch(c -> StringUtils.countMatches(currentBlock, c) > 1)) {
					result = endIndex;
					System.out.println(currentBlock);
					break;
				}

			}

			return result;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}

	}

}
