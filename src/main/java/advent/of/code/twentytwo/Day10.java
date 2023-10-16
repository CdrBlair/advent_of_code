package advent.of.code.twentytwo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Day10 {

	public int sumSignalStretgh() throws IOException {
		try (var br = new BufferedReader(new FileReader(
				"/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/cpuinstructions.txt"));) {

			List<String> commands = br.lines().toList();

			int numberOfCycles = commands.stream().mapToInt(c -> StringUtils.startsWith(c, "n") ? 1 : 2).sum();

			int registerX = 1;
			int valueV = 0;

			System.out.println(numberOfCycles);

			int currentCommand = 0;
			int sumSignalStrength = 0;
			int cycleToPeak = 39;

			boolean firstAddCycleDone = false;
			String pixel = ".";
			int pixelPos = 0;

			for (int i = 0; i < numberOfCycles; i++) {

				if (pixelPos >= registerX - 1 && pixelPos <= registerX + 1) {
					pixel = "#";
					pixelPos++;
				} else {
					pixel = ".";
					pixelPos++;
				}

				System.out.print(pixel);
				if (i == cycleToPeak) {
					pixelPos = 0;
					sumSignalStrength = sumSignalStrength + registerX * cycleToPeak;
					cycleToPeak = cycleToPeak + 40;
					System.out.println();
				}

				if (StringUtils.equals(commands.get(currentCommand), "noop")) {
					currentCommand++;
				} else if (!firstAddCycleDone) {
					firstAddCycleDone = true;
					valueV = Integer.valueOf(StringUtils.split(commands.get(currentCommand), " ")[1]);
				} else {
					firstAddCycleDone = false;
					registerX = registerX + valueV;
					currentCommand++;

				}
			}

			return sumSignalStrength;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

}
