package advent.of.code.twentytwo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Day20 {

	public int decryptFile() throws IOException {

		try (var br = new BufferedReader(new FileReader(
				"/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/encrypted.txt"));) {

			AtomicInteger index = new AtomicInteger(0);
			List<EncryptionEntry> originalOrder = br.lines()
					.map(l -> new EncryptionEntry(Long.valueOf(l), false, index.getAndIncrement()))
					.collect(Collectors.toList());

			List<EncryptionEntry> mixedState = new ArrayList<>();

			originalOrder.forEach(e -> mixedState.add(new EncryptionEntry(e.value, e.moved, e.originalIndex)));

			boolean allMoved = false;

			while (!allMoved) {
				EncryptionEntry current = null;
				int currentIndex = 0;
				for (var i = 0; i < originalOrder.size(); i++) {

					if (!originalOrder.get(i).moved) {
						current = originalOrder.get(i);
						currentIndex = mixedState.indexOf(current);
						break;
					}

				}

				mixedState.remove(currentIndex);
				var newIndex = Math.floorMod(current.value + currentIndex, mixedState.size());
//				System.out.println(newIndex);
				mixedState.add(newIndex, current);

				current.moved = true;
//				mixedState.forEach(e -> System.out.print(e.value + ", "));
//				System.out.println();
				allMoved = originalOrder.stream().allMatch(e -> e.moved);

			}

			var indexOfZero = mixedState.indexOf(mixedState.stream().filter(e -> e.value == 0).findFirst().get());

			var resultP1 = mixedState.get((indexOfZero + 1000) % mixedState.size()).value
					+ mixedState.get((indexOfZero + 2000) % mixedState.size()).value
					+ mixedState.get((indexOfZero + 3000) % mixedState.size()).value;

			System.out.println("Result Part 1: " + resultP1);

			// Part 2
			long key = 811589153l;
			allMoved = false;

			originalOrder.forEach(e -> e.value = e.value * key);
			originalOrder.forEach(e -> e.moved = false);
			mixedState.clear();

			originalOrder.forEach(e -> mixedState.add(new EncryptionEntry(e.value, e.moved, e.originalIndex)));
			for (var j = 0; j < 10; j++) {
				originalOrder.forEach(e -> e.moved = false);
				allMoved = false;

				while (!allMoved) {
					EncryptionEntry current = null;
					int currentIndex = 0;
					for (var i = 0; i < originalOrder.size(); i++) {

						if (!originalOrder.get(i).moved) {
							current = originalOrder.get(i);
							currentIndex = mixedState.indexOf(current);
							break;
						}

					}

					mixedState.remove(currentIndex);
					var newIndex = Math.floorMod(current.value + currentIndex, mixedState.size());
//							System.out.println(newIndex);
					mixedState.add(newIndex, current);

					current.moved = true;
//					mixedState.forEach(e -> System.out.print(e.value + ", "));
//					System.out.println();
					allMoved = originalOrder.stream().allMatch(e -> e.moved);

				}
				mixedState.forEach(e -> System.out.print(e.value + ", "));
				System.out.println();
			}

			indexOfZero = mixedState.indexOf(mixedState.stream().filter(e -> e.value == 0).findFirst().get());
			System.out.println(mixedState.get((indexOfZero + 1000) % mixedState.size()).value);
			System.out.println(mixedState.get((indexOfZero + 2000) % mixedState.size()).value);
			System.out.println(mixedState.get((indexOfZero + 3000) % mixedState.size()).value);

			var resultP2 = mixedState.get((indexOfZero + 1000) % mixedState.size()).value
					+ mixedState.get((indexOfZero + 2000) % mixedState.size()).value
					+ mixedState.get((indexOfZero + 3000) % mixedState.size()).value;

			System.out.println("Result Part 2: " + resultP2);

			return 0;
		} catch (

		IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	private class EncryptionEntry {
		long value;
		boolean moved;
		int originalIndex;

		public EncryptionEntry(long value, boolean moved, int originalIndex) {
			super();
			this.value = value;
			this.moved = moved;
			this.originalIndex = originalIndex;
		}

		@Override
		public String toString() {
			return "EncryptionEntry [value=" + value + ", moved=" + moved + ", originalIndex=" + originalIndex + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + Objects.hash(moved, originalIndex, value);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			EncryptionEntry other = (EncryptionEntry) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			return moved == other.moved && originalIndex == other.originalIndex && value == other.value;
		}

		private Day20 getEnclosingInstance() {
			return Day20.this;
		}

	}

}
