package code.wars;

import java.util.List;

public class Kata {

	public static List<Object> filterList(final List<Object> list) {
		// Return the List with the Strings filtered out
		return list.stream().filter(o -> !(o instanceof String)).toList();
	}
}
