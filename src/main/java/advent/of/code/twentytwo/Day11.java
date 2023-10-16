package advent.of.code.twentytwo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class Day11 {

	public long monkeyBusiness() throws IOException {

		try (var br = new BufferedReader(new FileReader(
				"/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/monkeys.txt"));) {

			List<String> input = br.lines().toList();
			List<Monkey> monkeys = new ArrayList<>();

			var numberOfRounds = 10000;
			var monkeyBusiness = 0;
			var dividerWorry = 1;

			for (var i = 0; i <= input.size() - 7; i = i + 7) {

				List<String> currentMonkeyInput = input.subList(i, i + 7);
				monkeys.add(parseMonkey(currentMonkeyInput));

			}

			int modulo = monkeys.stream().mapToInt(Monkey::getTestValue).reduce(1, Math::multiplyExact);

			for (var i = 1; i <= numberOfRounds; i++) {

				System.out.println("Round: " + i);

				for (Monkey monkey : monkeys) {

					var initialNumberOfItems = monkey.getItems().size();
					for (var j = 0; j < initialNumberOfItems; j++) {

						long currentItem = monkey.getItems().get(0);
						var operationValue = monkey.getOperationValue() == -1 ? currentItem
								: monkey.getOperationValue();

						long itemAfterInspection;
						if (monkey.getOperationType().equals("*")) {

							itemAfterInspection = currentItem * operationValue;

						} else {
							itemAfterInspection = currentItem + operationValue;
						}

						long itemAfterWorryDown = Math.floorDiv(itemAfterInspection, dividerWorry) % modulo;

						if (itemAfterWorryDown % monkey.getTestValue() == 0) {
							monkeys.get(monkey.getTrueThrowTo()).getItems().add(itemAfterWorryDown);

						} else {
							monkeys.get(monkey.getFalseThrowTo()).getItems().add(itemAfterWorryDown);
						}

						monkey.getItems().remove(0);
						monkey.incrementItemsInspected();

					}

				}

				var monkeyNumber = 0;
				for (Monkey monkey : monkeys) {
					System.out.println("Monkey " + monkeyNumber + ":" + monkey.getItems());
					monkeyNumber++;
				}

			}

			var monkeyNumber = 0;
			for (Monkey monkey : monkeys) {
				System.out.println("Monkey " + monkeyNumber + ": inspected " + monkey.getItemsInspected() + " Items");
				monkeyNumber++;
			}

			List<Integer> activeMonkeys = monkeys.stream().map(Monkey::getItemsInspected)
					.sorted(Collections.reverseOrder()).limit(2).collect(Collectors.toList());

			return activeMonkeys.get(0) * (long) activeMonkeys.get(1);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	private Monkey parseMonkey(List<String> details) {

		var itemsAsString = StringUtils.substringAfterLast(details.get(1).trim(), ":");

		List<Long> itemsAsInt = Arrays.asList(StringUtils.split(itemsAsString, ",")).stream().map(String::trim)
				.map(Long::valueOf).collect(Collectors.toList());

		String[] operations = StringUtils.split(details.get(2).trim(), " ");

		String operator = operations[4];

		int operationValue;
		try {
			operationValue = Integer.parseInt(operations[5]);
		} catch (NumberFormatException e) {
			operationValue = -1;
		}

		var testValue = Integer.parseInt(StringUtils.split(details.get(3).trim(), " ")[3]);

		var trueValue = Integer.parseInt(StringUtils.split(details.get(4).trim(), " ")[5]);
		var falseValue = Integer.parseInt(StringUtils.split(details.get(5).trim(), " ")[5]);

		return new Monkey(itemsAsInt, operator, operationValue, testValue, falseValue, trueValue);

	}

	private class Monkey {
		List<Long> items;
		String operationType;
		int operationValue;
		int testValue;
		int falseThrowTo;
		int trueThrowTo;

		int itemsInspected;

		public List<Long> getItems() {
			return items;
		}

		public void setItems(List<Long> items) {
			this.items = items;
		}

		public String getOperationType() {
			return operationType;
		}

		public void setOperationType(String operationType) {
			this.operationType = operationType;
		}

		public int getOperationValue() {
			return operationValue;
		}

		public void setOperationValue(int operationValue) {
			this.operationValue = operationValue;
		}

		public int getTestValue() {
			return testValue;
		}

		public void setTestValue(int testValue) {
			this.testValue = testValue;
		}

		public int getFalseThrowTo() {
			return falseThrowTo;
		}

		public void setFalseThrowTo(int falseThrowTo) {
			this.falseThrowTo = falseThrowTo;
		}

		public int getTrueThrowTo() {
			return trueThrowTo;
		}

		public void setTrueThrowTo(int trueThrowTo) {
			this.trueThrowTo = trueThrowTo;
		}

		public int getItemsInspected() {
			return itemsInspected;
		}

		public void setItemsInspected(int itemsInspected) {
			this.itemsInspected = itemsInspected;
		}

		public void incrementItemsInspected() {
			this.itemsInspected++;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result
					+ Objects.hash(falseThrowTo, items, operationType, operationValue, testValue, trueThrowTo);
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
			Monkey other = (Monkey) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			return falseThrowTo == other.falseThrowTo && Objects.equals(items, other.items)
					&& Objects.equals(operationType, other.operationType) && operationValue == other.operationValue
					&& testValue == other.testValue && trueThrowTo == other.trueThrowTo;
		}

		private Day11 getEnclosingInstance() {
			return Day11.this;
		}

		public Monkey(List<Long> items, String operationType, int operationValue, int testValue, int falseThrowTo,
				int trueThrowTo) {
			super();
			this.items = items;
			this.operationType = operationType;
			this.operationValue = operationValue;
			this.testValue = testValue;
			this.falseThrowTo = falseThrowTo;
			this.trueThrowTo = trueThrowTo;
			this.itemsInspected = 0;
		}

		@Override
		public String toString() {
			return "Monkey [items=" + items + ", operationType=" + operationType + ", operationValue=" + operationValue
					+ ", testValue=" + testValue + ", falseThrowTo=" + falseThrowTo + ", trueThrowTo=" + trueThrowTo
					+ "]";
		}

	}

}
