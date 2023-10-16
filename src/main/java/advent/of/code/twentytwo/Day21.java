package advent.of.code.twentytwo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class Day21 {

	Map<String, Monkey> allMonkeys = new HashMap<>();

	public Long getRootNumber() throws IOException {
		try (var br = new BufferedReader(new FileReader(
				"/Users/amv/work/workspaces/advent_of_code/playground/src/main/resources/monkeyNumbers.txt"));) {

			List<String> lines = br.lines().toList();

			lines.stream().forEach(m -> allMonkeys.put(StringUtils.substringBefore(m, ":"), parseMonkey(m)));

			var result = calculateMonkeyNumber("root");
			System.out.println("Result Part1 : " + result);

			// part 2

			var firstChildStaysSame = true;
			var secondChildStaysSame = true;
			var firstChildOfRoot = calculateMonkeyNumber(allMonkeys.get("root").firstOperationName);
			var secondChildOfRoot = calculateMonkeyNumber(allMonkeys.get("root").secondOperationName);

			for (var i = 0; i < 51; i++) {
				allMonkeys.forEach((k, v) -> {
					if (v.firstOperationName != null) {
						v.value = null;
					}
				});

				allMonkeys.get("humn").value = Long.valueOf(i);

				if (!firstChildOfRoot.equals(calculateMonkeyNumber(allMonkeys.get("root").firstOperationName))) {

					firstChildStaysSame = false;
				}
				if (!secondChildOfRoot.equals(calculateMonkeyNumber(allMonkeys.get("root").secondOperationName))) {
					secondChildStaysSame = false;
				}

			}

			if (firstChildStaysSame) {
				result = getWishedNumber(allMonkeys.get("root").secondOperationName, firstChildOfRoot);
			} else if (secondChildStaysSame) {
				result = getWishedNumber(allMonkeys.get("root").firstOperationName, secondChildOfRoot);
			}

			System.out.println("Result Part 2: " + result);
			return result;
		} catch (

		IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public Long getWishedNumber(String name, Long wishedValue) {

		if (name.equals("humn")) {
			System.out.println("Human Wished value: " + wishedValue);
			allMonkeys.get(name).value = wishedValue;
			return wishedValue;
		} else {
			var current = allMonkeys.get(name);
			var firstChildStaysSame = true;
			var secondChildStaysSame = true;
			var firstChildOfRoot = calculateMonkeyNumber(current.firstOperationName);
			var secondChildOfRoot = calculateMonkeyNumber(current.secondOperationName);

			for (var i = 0; i < 51; i++) {
				allMonkeys.forEach((k, v) -> {
					if (v.firstOperationName != null) {
						v.value = null;
					}
				});

				allMonkeys.get("humn").value = Long.valueOf(i);

				if (!firstChildOfRoot.equals(calculateMonkeyNumber(current.firstOperationName))) {

					firstChildStaysSame = false;
				}
				if (!secondChildOfRoot.equals(calculateMonkeyNumber(current.secondOperationName))) {
					secondChildStaysSame = false;
				}

			}

			if (firstChildStaysSame && !secondChildStaysSame) {

				switch (current.operation) {
				case "+":
					return getWishedNumber(current.secondOperationName, wishedValue - firstChildOfRoot);
				case "-":
					return getWishedNumber(current.secondOperationName, firstChildOfRoot - wishedValue);
				case "*":
					return getWishedNumber(current.secondOperationName, wishedValue / firstChildOfRoot);
				case "/":
					return getWishedNumber(current.secondOperationName, firstChildOfRoot / wishedValue);
				default:
					throw new IllegalArgumentException("Unexpected value: " + current.operation);
				}

			} else if (!firstChildStaysSame && secondChildStaysSame) {

				switch (current.operation) {
				case "+":
					return getWishedNumber(current.firstOperationName, wishedValue - secondChildOfRoot);
				case "-":
					return getWishedNumber(current.firstOperationName, wishedValue + secondChildOfRoot);
				case "*":
					return getWishedNumber(current.firstOperationName, wishedValue / secondChildOfRoot);
				case "/":
					return getWishedNumber(current.firstOperationName, wishedValue * secondChildOfRoot);
				default:
					throw new IllegalArgumentException("Unexpected value: " + current.operation);
				}
			} else {
				throw new IllegalArgumentException();
			}

		}

	}

	public Long calculateMonkeyNumber(String name) {

		Monkey monkey = allMonkeys.get(name);
		if (monkey.value != null) {
			return monkey.value;
		} else {
			Long firstMonkey = calculateMonkeyNumber(monkey.firstOperationName);
			Long secondMonkey = calculateMonkeyNumber(monkey.secondOperationName);

			switch (monkey.operation) {
			case "+":
				return firstMonkey + secondMonkey;
			case "-":
				return firstMonkey - secondMonkey;
			case "*":
				return firstMonkey * secondMonkey;
			case "/":
				return firstMonkey / secondMonkey;
			default:
				throw new IllegalArgumentException("Unexpected value: " + monkey.operation);
			}
		}

	}

	public Monkey parseMonkey(String lineToParse) {
		String name = StringUtils.substringBefore(lineToParse, ":");

		String valueOrOperation = StringUtils.substringAfter(lineToParse, ":").trim();

		if (StringUtils.isNumeric(valueOrOperation)) {
			var value = Integer.valueOf(valueOrOperation);
			return new Monkey(name, value);
		} else {
			String[] operation = StringUtils.split(valueOrOperation, " ");
			String firstOp = operation[0];
			String secondOp = operation[2];
			String operator = operation[1];

			return new Monkey(name, firstOp, secondOp, operator);
		}

	}

	private class Monkey {
		private String name;
		private Long value;
		private String firstOperationName;
		private String secondOperationName;
		private String operation;

		public Monkey(String name, long value) {
			this.name = name;
			this.value = value;
		}

		public Monkey(String name, String firstOp, String secondOp, String op) {

			this.name = name;
			this.firstOperationName = firstOp;
			this.secondOperationName = secondOp;
			this.operation = op;
			this.value = null;

		}

		@Override
		public String toString() {
			return "Monkey [name=" + name + ", value=" + value + ", firstOperationName=" + firstOperationName
					+ ", secondOperationName=" + secondOperationName + ", operation=" + operation + "]";
		}

	}
}
