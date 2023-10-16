package code.wars;

import java.util.Arrays;

public class FindOutlier {
	static int find(int[] integers) {

		// even number is searched if more than two are odd
		boolean even = (integers[0] % 2 != 0 && integers[1] % 2 != 0) || integers[2] % 2 != 0;

		return Arrays.stream(integers).filter(i -> even == (i % 2 == 0)).findFirst().getAsInt();
	}
}