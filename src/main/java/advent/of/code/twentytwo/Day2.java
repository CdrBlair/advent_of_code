package advent.of.code.twentytwo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;

public class Day2 {
	public int calculateAllRounds() throws FileNotFoundException {

		try (var br = new BufferedReader(
				new FileReader("/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/rps.txt"));) {

			return br.lines().map(x -> calculateRound(x.substring(0, 1), x.substring(2, 3)))
					.collect(Collectors.summingInt(Integer::intValue));

		} catch (IOException ex) {

			throw new FileNotFoundException();
		}

	}

	private int calculateRound(String oponent, String outcome) {

		var points = 0;

		var me = "";

		switch (oponent.concat(outcome)) {
		case "AY", "BX", "CZ":
			me = "X";
			break;

		case "AZ", "BY", "CX":
			me = "Y";
			break;

		case "AX", "BZ", "CY":
			me = "Z";
			break;
		default:
			break;
		}

		String fight = oponent.concat(me);

		switch (me) {

		case "X":
			points += 1;
			break;
		case "Y":
			points += 2;
			break;
		case "Z":
			points += 3;
			break;
		default:
			break;

		}

		switch (fight) {

		case "AX", "BY", "CZ":
			points += 3;
			break;
		case "AZ", "BX", "CY":
			points += 0;
			break;
		case "AY", "BZ", "CX":
			points += 6;
			break;
		default:
			break;

		}
		System.out.println("oponent: " + oponent + " me: " + me + " result: " + points);

		return points;

	}

}
