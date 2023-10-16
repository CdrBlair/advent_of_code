package code.wars;

import java.io.IOException;
import java.util.List;

public class Runner {

	public static void main(String[] args) throws IOException {

		System.out.println(Kata.filterList(List.of(1, 2, 3, "1", "2", "3")));

		System.out.println(Persist.persistence(999999999));
	}

}
