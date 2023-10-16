package code.wars;

class Persist {
	public static int persistence(long n) {
		// your code

		var steps = 0;
		while (n >= 10) {
			steps++;
			n = String.valueOf(n).chars().mapToObj(c -> (char) c).mapToInt(c -> Integer.valueOf(c + "")).reduce(1,
					(n1, n2) -> n1 * n2);

		}

		return steps;
	}
}