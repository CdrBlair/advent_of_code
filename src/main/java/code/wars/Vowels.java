package code.wars;

import org.apache.commons.lang3.StringUtils;

public class Vowels {

	public static int getCount(String str) {

		return str.chars().mapToObj(c -> (char) c).filter(c -> StringUtils.contains("aeiou", c)).toList().size();
	}

}
